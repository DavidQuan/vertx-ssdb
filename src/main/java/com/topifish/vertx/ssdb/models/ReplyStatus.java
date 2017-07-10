package com.topifish.vertx.ssdb.models;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FanYongquan
 * @copyright (c) topifish game studio, create on 2017/7/9.
 */
public enum ReplyStatus
{
    Unknown(""),
    Ok("ok"),
    NotFound("not_found"),
    Error("error"),
    Fail("fail"),
    ClientError("client_error");

    private static final Map<String, ReplyStatus> STATUS_MAP = new HashMap<>();

    static {
        ReplyStatus[] values = ReplyStatus.values();
        for (ReplyStatus value : values) {
            STATUS_MAP.put(value.string, value);
        }
    }

    private final String string;

    ReplyStatus(String strStatus)
    {
        this.string = strStatus;
    }

    public static ReplyStatus parseFrom(String key)
    {
        return STATUS_MAP.getOrDefault(key, Unknown);
    }

    @Override
    public String toString()
    {
        return string;
    }
}
