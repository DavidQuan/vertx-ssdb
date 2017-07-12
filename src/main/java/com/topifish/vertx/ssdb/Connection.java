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
import java.util.List;
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

    private Buffer binaryCommand(Buffer buffer, String key, Object[] params)
    {
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
        pendings.add(handlePending(handler));

        netSocket.write(binaryCommand(Buffer.buffer(128), key, params));
    }

    public void execute(List<BatchCommand> batches, Handler<AsyncResult<Void>> handler)
    {
        int size = batches.size() - 1;
        Buffer buffer = Buffer.buffer();
        for (int i = 0; i < size; ++i) {
            BatchCommand batchCommand = batches.get(i);
            binaryCommand(buffer, batchCommand.getKey(), batchCommand.getParams());
            pendings.add(handlePending(batchCommand.getHandler()));
        }

        BatchCommand batchCommand = batches.get(size);
        binaryCommand(buffer, batchCommand.getKey(), batchCommand.getParams());
        pendings.add(handlePending(event -> {
            batchCommand.getHandler()
                        .handle(event);
            vertx.runOnContext(ev -> handler.handle(F.succeededFuture()));
        }));

        netSocket.write(buffer);
    }

    private Handler<AsyncResult<Queue<byte[]>>> handlePending(Handler<AsyncResult<Queue<byte[]>>> handler)
    {
        return event -> {
            if (event.failed()) {
                handler.handle(event);
                return;
            }
            Queue<byte[]> queue = event.result();
            ReplyStatus replyStatus = ReplyStatus.parseFrom(new String(queue.poll()));
            switch (replyStatus) {
                case Ok:
                case NotFound:
                    handler.handle(event);
                    break;
                default:
                    handler.handle(F.failedFuture(queue.isEmpty() ? replyStatus.toString() : new String(queue.poll())));
                    break;
            }
        };
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
        expandPending();
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

    private int search(byte[] data, int size, int start, byte[] bytes)
    {
        int maxSize = size - bytes.length;
        boolean found;
        for (int i = start; i <= maxSize; ++i) {
            found = true;
            for (int j = 0; j < bytes.length; ++j) {
                if (data[i + j] != bytes[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    private void expandPending()
    {
        int idx = 0, subIdx;

        try {
            while (size > idx) {
                Queue<byte[]> v = new LinkedList<>();
                subIdx = idx;
                while (true) {
                    int nIdx = search(recvBuffer, size, subIdx, CTRL_N);
                    if (nIdx == -1) {
                        return;
                    }

                    if (recvBuffer[nIdx - 1] == CTRL_R) {
                        subIdx = nIdx + 1;
                        break;
                    }

                    if (nIdx + 1 >= size) {
                        return;
                    }

                    if (recvBuffer[nIdx + 1] == CTRL_N) {
                        subIdx = nIdx + 2;
                        break;
                    }

                    int length = Integer.parseInt(new String(recvBuffer, subIdx, nIdx - subIdx));

                    subIdx = nIdx + 1;

                    if (subIdx + length + 1 >= size) {
                        return;
                    }

                    byte[] data = Arrays.copyOfRange(recvBuffer, subIdx, subIdx + length);
                    subIdx += length + 1;
                    v.add(data);
                }
                idx = subIdx;
                Handler<AsyncResult<Queue<byte[]>>> penging = pendings.poll();
                vertx.runOnContext(event -> penging.handle(F.succeededFuture(v)));
            }
        } finally {
            if (idx > 0) {
                if (size > idx) {
                    System.arraycopy(recvBuffer, idx, recvBuffer, 0, size -= idx);
                } else {
                    size = 0;
                }
            }
        }
    }
}