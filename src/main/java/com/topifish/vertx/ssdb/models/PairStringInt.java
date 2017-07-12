package com.topifish.vertx.ssdb.models;

/**
 * @author FanYongquan
 * @copyright (c) topifish game studio, create on 2017/7/7.
 */
public class PairStringInt
{
    private final String key;

    private final int value;

    public PairStringInt(String key, int value)
    {
        this.key = key;
        this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public int getValue()
    {
        return value;
    }
}
