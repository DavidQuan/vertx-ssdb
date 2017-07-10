package com.topifish.vertx.ssdb;

import com.topifish.vertx.ssdb.exceptions.ResetException;
import com.topifish.vertx.ssdb.models.ReplyStatus;
import com.topifish.vertx.ssdb.models.SSDBOptions;
import com.topifish.vertx.ssdb.util.F;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static com.topifish.vertx.ssdb.SSDBClient.CTRL_N;
import static com.topifish.vertx.ssdb.SSDBClient.CTRL_R;

/**
 * @author FanYongquan
 * @copyright (c) topifish game studio, create on 2017/7/7.
 */
class Connection
{
    private final Vertx vertx;

    private final NetSocket netSocket;

    private Queue<Handler<AsyncResult<Queue<byte[]>>>> pendings = new LinkedList<>();

    private final int bufferSize;

    private byte[] recvBuffer;

    private int size;

    Connection(Vertx vertx, SSDBOptions options, NetSocket netSocket)
    {
        this.vertx = vertx;
        this.bufferSize = options.getBufferSize();
        this.recvBuffer = new byte[bufferSize];
        this.netSocket = netSocket;
        this.netSocket.handler(this::onReceive);
    }

    public boolean isAlive()
    {
        return netSocket != null;
    }

    public void clearPendings()
    {
        Handler<AsyncResult<Queue<byte[]>>> pending;
        Throwable reset = new ResetException();
        while ((pending = pendings.poll()) != null) {
            pending.handle(F.failedFuture(reset));
        }
    }

    private void append(Buffer buffer, String str)
    {
        byte[] bytes = str.getBytes();
        buffer.appendString(Integer.toString(bytes.length))
              .appendByte(CTRL_N)
              .appendBytes(bytes)
              .appendByte(CTRL_N);
    }

    private Buffer binaryCommand(String key, Object[] params)
    {
        Buffer buffer = Buffer.buffer(128);
        append(buffer, key);
        if (params != null) {
            for (Object str : params) {
                append(buffer, str.toString());
            }
        }
        buffer.appendByte(CTRL_N);
        return buffer;
    }

    public void execute(String key, Object[] params, Handler<AsyncResult<Queue<byte[]>>> handler)
    {
        pendings.add(F.ofSucceededVoid(handler, (Queue<byte[]> queue) -> {
                    ReplyStatus replyStatus = ReplyStatus.parseFrom(new String(queue.poll()));
                    switch (replyStatus) {
                        case Ok:
                        case NotFound:
                            handler.handle(F.succeededFuture(queue));
                            break;
                        default:
                            handler.handle(F.failedFuture(queue.isEmpty() ? replyStatus.toString() : new String(queue.poll())));
                            break;
                    }
                }
        ));

        netSocket.write(binaryCommand(key, params));
    }

    private void onReceive(Buffer buffer)
    {
        if (bufferSize - size - buffer.length() >= 0) {
            buffer.getBytes(recvBuffer, size);
        } else {
            int newSize = bufferSize * (int) Math.ceil((size + buffer.length()) * 1.f / bufferSize);
            byte[] newBuffer = new byte[newSize];
            System.arraycopy(recvBuffer, 0, newBuffer, 0, size);
            buffer.getBytes(newBuffer, size);
            recvBuffer = newBuffer;
        }
        size += buffer.length();
        tryExecPending();
    }

    private int search(byte[] data, int size, int start, byte c)
    {
        for (int i = start; i < size; ++i) {
            if (data[i] == c) {
                return i;
            }
        }
        return -1;
    }

    private void tryExecPending()
    {
        Queue<byte[]> v = new LinkedList<>();
        int idx = 0;

        while (true) {
            int pos = search(recvBuffer, size, idx, CTRL_N);
            if (pos == -1) {
                return;
            }

            if (pos == idx || (pos == idx + 1 && recvBuffer[idx] == CTRL_R)) {
                if (v.isEmpty()) {
                    ++idx;
                    continue;
                } else {
                    break;
                }
            }

            String str = new String(recvBuffer, idx, pos - idx);
            int len = Integer.parseInt(str);
            idx = pos + 1;
            if (idx + len >= size) {
                break;
            }
            byte[] data = Arrays.copyOfRange(recvBuffer, idx, idx + len);
            idx += len + 1;
            v.add(data);
        }

        if (v.isEmpty()) {
            return;
        }

        if (size > idx) {
            System.arraycopy(recvBuffer, idx, recvBuffer, 0, size - idx);
            size -= idx;
        }

        Handler<AsyncResult<Queue<byte[]>>> penging = pendings.poll();
        vertx.runOnContext(event -> penging.handle(F.succeededFuture(v)));
    }
}