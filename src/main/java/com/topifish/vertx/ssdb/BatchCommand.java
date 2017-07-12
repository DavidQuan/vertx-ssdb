package com.topifish.vertx.ssdb;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.Queue;

/**
 * @author FanYongquan
 * @copyright (c) topifish game studio, create on 2017/7/11.
 */
class BatchCommand
{
    private final String key;

    private final Object[] params;

    private final Handler<AsyncResult<Queue<byte[]>>> handler;

    public BatchCommand(String key, Object[] params, Handler<AsyncResult<Queue<byte[]>>> handler)
    {
        this.key = key;
        this.params = params;
        this.handler = handler;
    }

    public String getKey()
    {
        return key;
    }

    public Object[] getParams()
    {
        return params;
    }

    public Handler<AsyncResult<Queue<byte[]>>> getHandler()
    {
        return handler;
    }
}
