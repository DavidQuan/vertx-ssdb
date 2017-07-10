package com.topifish.vertx.ssdb;

import com.topifish.vertx.ssdb.models.SSDBOptions;
import com.topifish.vertx.ssdb.util.F;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;

import java.util.Queue;

/**
 * @author FanYongquan
 * @copyright (c) topifish game studio, create on 2017/7/7.
 */
class Connecter
{
    private final Vertx vertx;

    private final SSDBOptions options;

    private NetClient netClient;

    private Connection connection;

    Connecter(Vertx vertx, SSDBOptions options)
    {
        this.vertx = vertx;
        this.options = options;
    }

    void tryGetConnection(Handler<AsyncResult<Connection>> handler)
    {
        if (connection != null && connection.isAlive()) {
            handler.handle(F.succeededFuture(connection));
            return;
        }

        if (netClient == null) {
            netClient = vertx.createNetClient();
        }

        netClient.connect(options.getPort(), options.getHost(), F.ofSucceededVoid(handler, netSocket -> {
            connection = new Connection(vertx, options, netSocket);
            netSocket.closeHandler(event -> {
                connection.clearPendings();
                Connecter.this.connection = null;
            })
                     .exceptionHandler(exception -> {
                         connection.clearPendings();
                         Connecter.this.connection = null;
                         netSocket.close();
                     });
            String auth = options.getAuth();
            if (auth == null) {
                handler.handle(F.succeededFuture(connection));
                return;
            }

            connection.execute("auth", new Object[]{auth}, F.ofSucceeded(handler, (Queue<byte[]> queue) -> connection));
        }));
    }
}
