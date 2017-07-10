package com.topifish.vertx.ssdb.functions;

import java.util.Objects;

@FunctionalInterface
public interface VoidConsumer
{
    void accept();

    default VoidConsumer andThen(VoidConsumer after)
    {
        Objects.requireNonNull(after);
        return () -> {
            accept();
            after.accept();
        };
    }
}
