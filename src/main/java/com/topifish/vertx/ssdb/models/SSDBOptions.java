package com.topifish.vertx.ssdb.models;

/**
 * @author FanYongquan
 * @copyright (c) topifish game studio, create on 2017/7/7.
 */
public class SSDBOptions
{
    private String host;

    private int port;

    private String auth;

    private int bufferSize = 4096;

    private int poolSize = 20;

    public SSDBOptions(String host, int port, String auth)
    {
        this.host = host;
        this.port = port;
        this.auth = auth;
    }

    public SSDBOptions(String host, int port)
    {
        this(host, port, null);
    }

    public int getBufferSize()
    {
        return bufferSize;
    }

    public SSDBOptions setBufferSize(int bufferSize)
    {
        this.bufferSize = bufferSize;
        return this;
    }

    public SSDBOptions setPoolSize(int poolSize)
    {
        this.poolSize = poolSize;
        return this;
    }

    public int getPoolSize()
    {
        return poolSize;
    }

    public String getHost()
    {
        return host;
    }

    public int getPort()
    {
        return port;
    }

    public String getAuth()
    {
        return auth;
    }
}
