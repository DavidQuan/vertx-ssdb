package com.topifish.vertx.ssdb;

import com.topifish.vertx.ssdb.exceptions.SSDBClosedException;
import com.topifish.vertx.ssdb.models.PairStringInt;
import com.topifish.vertx.ssdb.models.PairStringString;
import com.topifish.vertx.ssdb.models.SSDBOptions;
import com.topifish.vertx.ssdb.util.F;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

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
    enum Status
    {
        Normal,
        Once,
        Unusable
    }

    private final Vertx vertx;

    private final Connecter connecter;

    private Status status = Status.Once;

    private List<BatchCommand> batches;

    CompositeClient(Vertx vertx, SSDBOptions options)
    {
        this.vertx = vertx;
        this.connecter = new Connecter(vertx, options);
    }

    @Override
    public SSDBClient setAutoClose(boolean autoClose)
    {
        if (autoClose) {
            status = Status.Once;
        } else {
            status = Status.Normal;
        }
        return this;
    }

    void sendCommand(Handler<AsyncResult<Queue<byte[]>>> handler, String key, Object... params)
    {
        if (batches != null) {
            batches.add(new BatchCommand(key, params, handler));
            return;
        }

        switch (status) {
            case Normal:
                connecter.tryGetConnection(F.ofSucceededVoid(handler, conn -> conn.execute(key, params, handler)));
                break;
            case Once:
                connecter.tryGetConnection(F.ofSucceededVoid(handler, conn -> conn.execute(key, params, F.ofSucceeded(handler, () -> {
                    status = Status.Unusable;
                    vertx.runOnContext(event -> close(F.noneHandle()));
                }, queue -> queue))));
                break;
            case Unusable:
                handler.handle(F.failedFuture(new SSDBClosedException()));
                break;
        }
    }

    void initBatch()
    {
        batches = new ArrayList<>(5);
    }

    void sendCommands(Handler<AsyncResult<Void>> handler)
    {
        switch (status) {
            case Normal:
                connecter.tryGetConnection(F.ofSucceededVoid(handler, conn -> {
                    conn.execute(batches, handler);
                    batches = null;
                }));
                break;
            case Once:
                connecter.tryGetConnection(F.ofSucceededVoid(handler, conn -> {
                    conn.execute(batches, F.ofSucceeded(handler, () -> {
                        status = Status.Unusable;
                        vertx.runOnContext(event -> close(F.noneHandle()));
                    }, queue -> queue));
                    batches = null;
                }));
                break;
            case Unusable:
                handler.handle(F.failedFuture(new SSDBClosedException()));
                break;
        }
    }

    List<String> listValue(Queue<byte[]> queue)
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

    Map<String, String> mapValue(Queue<byte[]> queue)
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

    List<PairStringString> listPairValue(Queue<byte[]> queue)
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

    List<PairStringInt> listPairStringIntValue(Queue<byte[]> queue)
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

    Integer intValue(Queue<byte[]> queue)
    {
        if (queue.isEmpty()) {
            return INT0;
        }

        return new Integer(new String(queue.poll()));
    }

    Integer intValue_1(Queue<byte[]> queue)
    {
        if (queue.isEmpty()) {
            return INT_1;
        }

        return new Integer(new String(queue.poll()));
    }

    Long longValue(Queue<byte[]> queue)
    {
        if (queue.isEmpty()) {
            return LONG0;
        }

        return new Long(new String(queue.poll()));
    }

    String stringValue(Queue<byte[]> queue)
    {
        if (queue.isEmpty()) {
            return null;
        }

        return new String(queue.poll());
    }

    @SuppressWarnings("unused")
    Void voidValue(Queue<byte[]> ignore)
    {
        return null;
    }

    Boolean booleanValue(Queue<byte[]> queue)
    {
        if (queue.isEmpty()) {
            return Boolean.FALSE;
        }

        byte[] bytes = queue.poll();
        return bytes[0] == '1' ? Boolean.TRUE : Boolean.FALSE;
    }

    <K, V> Object[] toArray(Map<K, V> keyValues)
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
