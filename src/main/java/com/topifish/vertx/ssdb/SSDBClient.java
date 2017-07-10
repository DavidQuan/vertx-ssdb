package com.topifish.vertx.ssdb;

import com.topifish.vertx.ssdb.models.PairStringInt;
import com.topifish.vertx.ssdb.models.PairStringString;
import com.topifish.vertx.ssdb.models.SSDBOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.List;
import java.util.Map;

/**
 * @author FanYongquan
 * @copyright (c) topifish game studio, create on 2017/7/7.
 */
public interface SSDBClient
{
    byte CTRL_N = (byte) '\n';

    byte CTRL_R = (byte) '\r';

    Integer INT0 = 0;

    Integer INT_1 = -1;

    Long LONG0 = 0l;

    static SSDBClient create(Vertx vertx, SSDBOptions options)
    {
        return new SSDBClientImpl(vertx, options);
    }

    void close(Handler<AsyncResult<Void>> handler);

    void dbsize(Handler<AsyncResult<Long>> handler);

    default void flushdb(Handler<AsyncResult<Void>> handler)
    {
        throw new UnsupportedOperationException();
    }

    default void info(Handler<AsyncResult<Map<String, String>>> handler)
    {
        info(null, handler);
    }

    void info(String cmd, Handler<AsyncResult<Map<String, String>>> handler);

    void auth(String authKey, Handler<AsyncResult<Boolean>> handler);

    void listAllowIp(Handler<AsyncResult<List<String>>> handler);

    void addAllowIp(String prefixIp, Handler<AsyncResult<Void>> handler);

    void delAllowIp(String prefixIp, Handler<AsyncResult<Void>> handler);

    void listDenyIp(Handler<AsyncResult<List<String>>> handler);

    void addDenyIp(String prefixIp, Handler<AsyncResult<Void>> handler);

    void delDenyIp(String prefixIp, Handler<AsyncResult<Void>> handler);

    void set(String key, String value, Handler<AsyncResult<Void>> handler);

    void setx(String key, String value, int secondsTTL, Handler<AsyncResult<Void>> handler);

    void setnx(String key, String value, Handler<AsyncResult<Integer>> handler);

    void expire(String key, int secondsTTL, Handler<AsyncResult<Integer>> handler);

    void ttl(String key, Handler<AsyncResult<Integer>> handler);

    void get(String key, Handler<AsyncResult<String>> handler);

    void getset(String key, String value, Handler<AsyncResult<String>> handler);

    void del(String key, Handler<AsyncResult<Void>> handler);

    void incr(String key, int incrValue, Handler<AsyncResult<Integer>> handler);

    default void incr(String key, Handler<AsyncResult<Integer>> handler)
    {
        incr(key, 1, handler);
    }

    void exists(String key, Handler<AsyncResult<Boolean>> handler);

    void getbit(String key, int offset, Handler<AsyncResult<Integer>> handler);

    void setbit(String key, int offset, int bitValue, Handler<AsyncResult<Integer>> handler);

    void bitcount(String key, int start, int end, Handler<AsyncResult<Integer>> handler);

    default void bitcount(String key, Handler<AsyncResult<Integer>> handler)
    {
        bitcount(key, 0, -1, handler);
    }

    default void bitcount(String key, int start, Handler<AsyncResult<Integer>> handler)
    {
        bitcount(key, start, -1, handler);
    }

    void countbit(String key, int start, int end, Handler<AsyncResult<Integer>> handler);

    void substr(String key, int start, int size, Handler<AsyncResult<String>> handler);

    void strlen(String key, Handler<AsyncResult<Integer>> handler);

    void keys(String keyStart, String keyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    default void keys(int limit, Handler<AsyncResult<List<String>>> handler)
    {
        keys("", "", limit, handler);
    }

    void rkeys(String keyStart, String keyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    default void rkeys(int limit, Handler<AsyncResult<List<String>>> handler)
    {
        rkeys("", "", limit, handler);
    }

    void scan(String keyStart, String keyEnd, int limit, Handler<AsyncResult<List<PairStringString>>> handler);

    default void scan(int limit, Handler<AsyncResult<List<PairStringString>>> handler)
    {
        scan("", "", limit, handler);
    }

    void rscan(String keyStart, String keyEnd, int limit, Handler<AsyncResult<List<PairStringString>>> handler);

    default void rscan(int limit, Handler<AsyncResult<List<PairStringString>>> handler)
    {
        rscan("", "", limit, handler);
    }

    void multiSet(Map<String, String> keyValues, Handler<AsyncResult<Void>> handler);

    void multiGet(List<String> keys, Handler<AsyncResult<Map<String, String>>> handler);

    void multiDel(List<String> keys, Handler<AsyncResult<Void>> handler);

    void hset(String hashKey, String fieldKey, String fieldValue, Handler<AsyncResult<Integer>> handler);

    void hget(String hashKey, String fieldKey, Handler<AsyncResult<String>> handler);

    void hdel(String hashKey, String fieldKey, Handler<AsyncResult<Integer>> handler);

    void hincr(String hashKey, String fieldKey, int incrValue, Handler<AsyncResult<Long>> handler);

    default void hincr(String hashKey, String fieldKey, Handler<AsyncResult<Long>> handler)
    {
        hincr(hashKey, fieldKey, 1, handler);
    }

    void hexists(String hashKey, String fieldKey, Handler<AsyncResult<Boolean>> handler);

    void hsize(String hashKey, Handler<AsyncResult<Long>> handler);

    void hlist(String hashKeyStart, String hashKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    default void hlist(int limit, Handler<AsyncResult<List<String>>> handler)
    {
        hlist("", "", limit, handler);
    }

    void hrlist(String hashKeyStart, String hashKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    default void hrlist(int limit, Handler<AsyncResult<List<String>>> handler)
    {
        hrlist("", "", limit, handler);
    }

    void hkeys(String hashKey, String fieldKeyStart, String fieldKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    default void hkeys(String hashKey, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        hkeys(hashKey, "", "", limit, handler);
    }

    void hgetall(String hashKey, Handler<AsyncResult<Map<String, String>>> handler);

    void hscan(String hashKey, String fieldKeyStart, String fieldKeyEnd, int limit, Handler<AsyncResult<Map<String, String>>> handler);

    default void hscan(String hashKey, int limit, Handler<AsyncResult<Map<String, String>>> handler)
    {
        hscan(hashKey, "", "", limit, handler);
    }

    void hrscan(String hashKey, String fieldKeyStart, String fieldKeyEnd, int limit, Handler<AsyncResult<List<PairStringString>>> handler);

    default void hrscan(String hashKey, int limit, Handler<AsyncResult<List<PairStringString>>> handler)
    {
        hrscan(hashKey, "", "", limit, handler);
    }

    void hclear(String hashKey, Handler<AsyncResult<Long>> handler);

    void multiHset(String hashKey, Map<String, String> fields, Handler<AsyncResult<Void>> handler);

    void multiHget(String hashKey, List<String> fieldKeys, Handler<AsyncResult<Map<String, String>>> handler);

    void multiHdel(String hashKey, List<String> fieldKeys, Handler<AsyncResult<Void>> handler);

    void zset(String setKey, String itemKey, int score, Handler<AsyncResult<Integer>> handler);

    void zget(String setKey, String itemKey, Handler<AsyncResult<String>> handler);

    void zdel(String setKey, String itemKey, Handler<AsyncResult<Integer>> handler);

    void zincr(String setKey, String itemKey, int incrValue, Handler<AsyncResult<Integer>> handler);

    void zsize(String setKey, Handler<AsyncResult<Long>> handler);

    void zlist(String setKeyStart, String setKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    default void zlist(int limit, Handler<AsyncResult<List<String>>> handler)
    {
        zlist("", "", limit, handler);
    }

    void zrlist(String setKeyStart, String setKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    default void zrlist(int limit, Handler<AsyncResult<List<String>>> handler)
    {
        zrlist("", "", limit, handler);
    }

    void zexists(String setKey, String itemKey, Handler<AsyncResult<Boolean>> handler);

    void zkeys(String setKey, String itemKeyStart, int scoreStart, int scoreEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    void zscan(String setKey, String itemKeyStart, int scoreStart, int scoreEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    void zrscan(String setKey, String itemKeyStart, int scoreStart, int scoreEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    void zrank(String setKey, String itemKey, Handler<AsyncResult<Integer>> handler);

    void zrrank(String setKey, String itemKey, Handler<AsyncResult<Integer>> handler);

    void zrange(String setKey, int offset, int limit, Handler<AsyncResult<List<PairStringInt>>> handler);

    void zrrange(String setKey, int offset, int limit, Handler<AsyncResult<List<PairStringInt>>> handler);

    void zclear(String setKey, Handler<AsyncResult<Long>> handler);

    void zcount(String setKey, int scoreStart, int scoreEnd, Handler<AsyncResult<Long>> handler);

    void zsum(String setKey, int scoreStart, int scoreEnd, Handler<AsyncResult<Long>> handler);

    void zavg(String setKey, int scoreStart, int scoreEnd, Handler<AsyncResult<Long>> handler);

    void zremRangeByRank(String setKey, int rankStart, int rankEnd, Handler<AsyncResult<Integer>> handler);

    void zremRangeByScore(String setKey, int scoreStart, int scoreEnd, Handler<AsyncResult<Integer>> handler);

    void zpopFront(String setKey, int limit, Handler<AsyncResult<List<PairStringInt>>> handler);

    void zpopBack(String setKey, int limit, Handler<AsyncResult<List<PairStringInt>>> handler);

    void multiZset(String setKey, Map<String, Integer> items, Handler<AsyncResult<Void>> handler);

    void multiZget(String setKey, List<String> itemKeys, Handler<AsyncResult<List<PairStringInt>>> handler);

    void multiZdel(String setKey, List<String> itemKeys, Handler<AsyncResult<Void>> handler);

    void qpushFront(String listKey, String item, Handler<AsyncResult<Integer>> handler);

    void qpushFront(String listKey, List<String> items, Handler<AsyncResult<Integer>> handler);

    void qpushBack(String listKey, String item, Handler<AsyncResult<Long>> handler);

    void qpushBack(String listKey, List<String> items, Handler<AsyncResult<Long>> handler);

    void qpopFront(String listKey, int size, Handler<AsyncResult<List<String>>> handler);

    default void qpopFront(String listKey, Handler<AsyncResult<List<String>>> handler)
    {
        qpopFront(listKey, 1, handler);
    }

    void qpopBack(String listKey, int size, Handler<AsyncResult<List<String>>> handler);

    default void qpopBack(String listKey, Handler<AsyncResult<List<String>>> handler)
    {
        qpopBack(listKey, 1, handler);
    }

    default void qpush(String listKey, List<String> items, Handler<AsyncResult<Long>> handler)
    {
        qpushBack(listKey, items, handler);
    }

    default void qpush(String listKey, String item, Handler<AsyncResult<Long>> handler)
    {
        qpushBack(listKey, item, handler);
    }

    default void qpop(String listKey, int size, Handler<AsyncResult<List<String>>> handler)
    {
        qpopFront(listKey, size, handler);
    }

    default void qpop(String listKey, Handler<AsyncResult<List<String>>> handler)
    {
        qpop(listKey, 1, handler);
    }

    void qfront(String listKey, Handler<AsyncResult<String>> handler);

    void qback(String listKey, Handler<AsyncResult<String>> handler);

    void qsize(String listKey, Handler<AsyncResult<Long>> handler);

    void qclear(String listKey, Handler<AsyncResult<Void>> handler);

    void qget(String listKey, int index, Handler<AsyncResult<String>> handler);

    void qset(String listKey, int index, String newValue, Handler<AsyncResult<Void>> handler);

    void qrange(String listKey, int offset, int limit, Handler<AsyncResult<List<String>>> handler);

    void qslice(String listKey, int begin, int end, Handler<AsyncResult<List<String>>> handler);

    void qtrimFront(String listKey, int size, Handler<AsyncResult<Integer>> handler);

    void qtrimBack(String listKey, int size, Handler<AsyncResult<Integer>> handler);

    void qlist(String listKeyStart, String listKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    default void qlist(int limit, Handler<AsyncResult<List<String>>> handler)
    {
        qlist("", "", limit, handler);
    }

    void qrlist(String listKeyStart, String listKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    SSDBClient setAutoClose(boolean autoClose);
}
