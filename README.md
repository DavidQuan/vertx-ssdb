# Vert.x SSDB Client
  ===================

SSDB - A fast NoSQL database, an alternative to Redis [http://ssdb.io](http://ssdb.io/) [https://github.com/ideawu/ssdb](https://github.com/ideawu/ssdb)

基本用法
```
SSDBOptions options = new SSDBOptions("127.0.0.1", 8888);
SSDBClient client = SSDBClient.create(vertx, options);
```

```
client.get("a", ev -> {
    if (ev.failed()) {
        System.out.println(ev.cause());
        return;
    }
    System.out.println(ev.result());
});
```

```
client.setAutoClose(false) // 关闭自动关闭连接
      .dbsize(ev -> {
      })
      .set("a", ev -> {
      })
      .get("a", ev -> client.close(F.noneHandle()));
```

```
client.beginBatch()
      .set("a", "a", F.noneHandle())
      .get("a", F.noneHandle())
      .setx("k", "k", 10, F.noneHandle())
      .scan(10, F.noneHandle())
      .setbit("b", 0, 0, F.noneHandle())
      .getbit("b", 0, F.noneHandle())
      .del("a", F.noneHandle())
      .exists("a", F.noneHandle())
      .expire("a", 10, F.noneHandle())
      .hclear("k", F.noneHandle())
      .endBatch(F.noneHandle());
```

```
SSDBPool pool = new SSDBPool(vertx, options);
pool.getClient(event -> {
    SSDBClient client = event.result();
    client.dbsize(ev -> {
        System.out.println(ev.result());
    });
});
```

```
pool.getClient(event -> {
    SSDBClient client = event.result();
    client.setAutoClose(false) // 关闭自动返回连接池
          .beginBatch()
          .dbsize(ev -> {
              System.out.println(ev.result());
          })
          .set("a", "a", F.noneHandle())
          .endBatch(ev -> client.close(F.noneHandle()));
});
```