package com.topifish.vertx.ssdb.util;

import com.topifish.vertx.ssdb.functions.VoidConsumer;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author FanYongquan
 * @copyright (c) topifish game studio, create on 2017/7/7.
 */
public class F
{
    public static <T> Future<T> succeededFuture()
    {
        return Future.succeededFuture();
    }

    public static <T> Future<T> succeededFuture(T result)
    {
        return result == null ? succeededFuture() : Future.succeededFuture(result);
    }

    public static <T> Future<T> failedFuture(String string)
    {
        return Future.failedFuture(string);
    }

    public static <T> Future<T> failedFuture(Throwable throwable)
    {
        return Future.failedFuture(throwable);
    }

    public static <A, B> Handler<AsyncResult<A>> ofSucceededVoid(Handler<AsyncResult<B>> handler, Consumer<A> consumer)
    {
        return ofSucceededVoid(handler, null, consumer);
    }

    public static <A, B> Handler<AsyncResult<A>> ofSucceededVoid(Handler<AsyncResult<B>> handler, VoidConsumer alwaysDo, Consumer<A> consumer)
    {
        return e -> {
            if (alwaysDo != null) {
                alwaysDo.accept();
            }

            if (e.failed()) {
                handler.handle(failedFuture(e.cause()));
                return;
            }
            consumer.accept(e.result());
        };
    }

    public static <A, B> Handler<AsyncResult<A>> ofSucceeded(Handler<AsyncResult<B>> handler, Function<A, B> consumer)
    {
        return ofSucceeded(handler, null, consumer);
    }

    public static <A, B> Handler<AsyncResult<A>> ofSucceeded(Handler<AsyncResult<B>> handler, VoidConsumer alwaysDo, Function<A, B> consumer)
    {
        return e -> {
            if (alwaysDo != null) {
                alwaysDo.accept();
            }
            if (e.failed()) {
                handler.handle(failedFuture(e.cause()));
                return;
            }
            B v = consumer.apply(e.result());
            handler.handle(v == null ? succeededFuture() : succeededFuture(v));
        };
    }

    public static <T> Handler<AsyncResult<T>> noneHandle()
    {
        return ev -> {
        };
    }
}
