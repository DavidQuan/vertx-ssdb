package com.topifish.vertx.ssdb;

import com.topifish.vertx.ssdb.models.SSDBOptions;
import com.topifish.vertx.ssdb.util.F;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;

/**
 * @author FanYongquan
 * @copyright (c) topifish game studio, create on 2017/7/7.
 */
class Connecter
{
    private final Vertx vertx;

    private final SSDBOptions options;

    private NetClient netClient;

    private Connection conn;

    Connecter(Vertx vertx, SSDBOptions options)
    {
        this.vertx = vertx;
        this.options = options;
    }

    void tryGetConnection(Handler<AsyncResult<Connection>> handler)
    {
        if (conn != null && conn.isAlive()) {
            handler.handle(F.succeededFuture(conn));
            return;
        }

        if (netClient == null) {
            netClient = vertx.createNetClient();
        }

        netClient.connect(options.getPort(), options.getHost(), F.ofSucceeded(handler, netSocket -> {
            conn = new Connection(vertx, options, netSocket);
            netSocket.closeHandler(event -> {
                conn.clearPendings();
                Connecter.this.conn = null;
            })
                     .exceptionHandler(exception -> {
                         conn.clearPendings();
                         Connecter.this.conn = null;
                         netSocket.close();
                     });
            return conn;
        }));
    }
}
