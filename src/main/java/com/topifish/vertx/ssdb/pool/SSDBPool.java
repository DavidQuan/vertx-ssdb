package com.topifish.vertx.ssdb.pool;

import com.topifish.vertx.ssdb.SSDBClient;
import com.topifish.vertx.ssdb.SSDBClientImpl;
import com.topifish.vertx.ssdb.exceptions.ResetException;
import com.topifish.vertx.ssdb.models.SSDBOptions;
import com.topifish.vertx.ssdb.util.F;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.nio.channels.ClosedChannelException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author FanYongquan
 * @copyright (c) topifish game studio, create on 2017/7/10.
 */
public class SSDBPool
{
    private final Vertx vertx;

    private final SSDBOptions options;

    private final int poolSize;

    private int count;

    private final Queue<SSDBClient> queue;

    private final Queue<Handler<AsyncResult<SSDBClient>>> waitQueue;

    private boolean shutdown;

    private int shutdownCount;

    private Handler<AsyncResult<Void>> closeHandler;

    public SSDBPool(Vertx vertx, SSDBOptions options)
    {
        this.vertx = vertx;
        this.options = options;
        this.poolSize = options.getPoolSize();
        this.queue = new LinkedList<>();
        this.waitQueue = new LinkedList<>();
    }

    public void getClient(Handler<AsyncResult<SSDBClient>> handler)
    {
        SSDBClient client;

        if (shutdown) {
            handler.handle(F.failedFuture(new ClosedChannelException()));
            return;
        }

        if (queue.isEmpty()) {
            if (count >= poolSize) {
                waitQueue.add(handler);
                return;
            }
            ++count;
            client = new SSDBClientImpl(vertx, options)
            {
                @Override
                public void close(Handler<AsyncResult<Void>> handler)
                {
                    if (shutdown) {
                        super.close(F.ofSucceeded(handler, () -> {
                            if (++shutdownCount >= count) {
                                if (SSDBPool.this.closeHandler != null) {
                                    vertx.runOnContext(event -> SSDBPool.this.closeHandler.handle(F.succeededFuture()));
                                }
                            }
                        }, F.VOID_NULL));
                        return;
                    }

                    setAutoClose(true);

                    if (waitQueue.isEmpty()) {
                        queue.add(this);
                        return;
                    }

                    Handler<AsyncResult<SSDBClient>> first = waitQueue.poll();
                    vertx.runOnContext(event -> first.handle(F.succeededFuture(this)));
                }
            };
        } else {
            client = queue.poll();
        }

        handler.handle(F.succeededFuture(client));
    }

    public void close(Handler<AsyncResult<Void>> handler)
    {
        this.shutdown = true;
        this.closeHandler = handler;

        ResetException exception = new ResetException();
        Handler<AsyncResult<SSDBClient>> first;

        while ((first = waitQueue.poll()) != null) {
            first.handle(F.failedFuture(exception));
        }


        SSDBClient client;
        while ((client = queue.poll()) != null) {
            client.close(F::succeededFuture);
        }
    }
}
