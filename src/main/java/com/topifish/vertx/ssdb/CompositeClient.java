package com.topifish.vertx.ssdb;

import com.topifish.vertx.ssdb.models.PairStringInt;
import com.topifish.vertx.ssdb.models.PairStringString;
import com.topifish.vertx.ssdb.models.ReplyStatus;
import com.topifish.vertx.ssdb.models.SSDBOptions;
import com.topifish.vertx.ssdb.util.F;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

/**
 * @author FanYongquan
 * @copyright (c) topifish game studio, create on 2017/7/10.
 */
abstract class CompositeClient implements SSDBClient
{
    private final Connecter ssdb;

    protected CompositeClient(Vertx vertx, SSDBOptions options)
    {
        this.ssdb = new Connecter(vertx, options);
    }

    private void append(Buffer buffer, String str)
    {
        byte[] bytes = str.getBytes();
        buffer.appendString(Integer.toString(bytes.length))
              .appendByte(CTRL_N)
              .appendBytes(bytes)
              .appendByte(CTRL_N);
    }

    protected void sendCommand(Handler<AsyncResult<Queue<byte[]>>> handler, String key, Object... cmdVs)
    {
        ssdb.tryGetConnection(event -> {
            if (event.failed()) {
                handler.handle(F.failedFuture(event.cause()));
                return;
            }
            Buffer buffer = Buffer.buffer(128);
            append(buffer, key);
            for (Object str : cmdVs) {
                append(buffer, str.toString());
            }
            buffer.appendByte(CTRL_N);
            event.result()
                 .write(buffer, event1 -> {
                     if (event1.failed()) {
                         handler.handle(F.failedFuture(event1.cause()));
                         return;
                     }
                     Queue<byte[]> result = event1.result();
                     ReplyStatus replyStatus = ReplyStatus.parseFrom(new String(result.poll()));
                     switch (replyStatus) {
                         case Ok:
                         case NotFound:
                             handler.handle(event1);
                             break;
                         default:
                             handler.handle(F.failedFuture(new String(result.poll())));
                             break;
                     }
                 });
        });
    }

    protected List<String> listValue(Queue<byte[]> queue)
    {
        if (queue.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> v = new ArrayList<>();
        for (byte[] bytes : queue) {
            v.add(new String(bytes));
        }

        return v;
    }

    protected Map<String, String> mapValue(Queue<byte[]> queue)
    {
        if (queue.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, String> v = new HashMap<>();

        for (byte[] key = queue.poll(), value = queue.poll(); key != null && value != null; key = queue.poll(), value = queue.poll()) {
            v.put(new String(key), new String(value));
        }

        return v;
    }

    protected List<PairStringString> listPairValue(Queue<byte[]> queue)
    {
        if (queue.isEmpty()) {
            return Collections.emptyList();
        }

        List<PairStringString> v = new ArrayList<>();

        for (byte[] key = queue.poll(), value = queue.poll(); key != null && value != null; key = queue.poll(), value = queue.poll()) {
            v.add(new PairStringString(new String(key), new String(value)));
        }

        return v;
    }

    protected List<PairStringInt> listPairStringIntValue(Queue<byte[]> queue)
    {
        if (queue.isEmpty()) {
            return Collections.emptyList();
        }

        List<PairStringInt> v = new ArrayList<>();

        for (byte[] key = queue.poll(), value = queue.poll(); key != null && value != null; key = queue.poll(), value = queue.poll()) {
            v.add(new PairStringInt(new String(key), Integer.parseInt(new String(value))));
        }

        return v;
    }

    protected Integer intValue(Queue<byte[]> queue)
    {
        if (queue.isEmpty()) {
            return INT0;
        }

        return new Integer(new String(queue.poll()));
    }

    protected Integer intValue_1(Queue<byte[]> queue)
    {
        if (queue.isEmpty()) {
            return INT_1;
        }

        return new Integer(new String(queue.poll()));
    }

    protected Long longValue(Queue<byte[]> queue)
    {
        if (queue.isEmpty()) {
            return LONG0;
        }

        return new Long(new String(queue.poll()));
    }

    protected String stringValue(Queue<byte[]> queue)
    {
        if (queue.isEmpty()) {
            return null;
        }

        return new String(queue.poll());
    }

    @SuppressWarnings("unused")
    protected Void voidValue(Queue<byte[]> ignore)
    {
        return null;
    }

    protected Boolean booleanValue(Queue<byte[]> queue)
    {
        if (queue.isEmpty()) {
            return Boolean.FALSE;
        }

        byte[] bytes = queue.poll();
        return bytes[0] == '1' ? Boolean.TRUE : Boolean.FALSE;
    }

    protected <K, V> Object[] toArray(Map<K, V> keyValues)
    {
        int idx = 0;
        Object[] v = new Object[keyValues.size() << 1];

        for (Entry<K, V> entry : keyValues.entrySet()) {
            v[idx++] = entry.getKey();
            v[idx++] = entry.getValue();
        }

        return v;
    }
}
