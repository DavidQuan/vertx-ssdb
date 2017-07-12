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

    byte CTRL_R = (byte) '\n';

    Integer INT0 = 0;

    Integer INT_1 = -1;

    Long LONG0 = 0l;

    static SSDBClient create(Vertx vertx, SSDBOptions options)
    {
        return new SSDBClientImpl(vertx, options);
    }

    void close(Handler<AsyncResult<Void>> handler);

    SSDBClient ping(Handler<AsyncResult<Void>> handler);

    SSDBClient dbsize(Handler<AsyncResult<Long>> handler);

    default SSDBClient flushdb(Handler<AsyncResult<Void>> handler)
    {
        throw new UnsupportedOperationException();
    }

    SSDBClient info(String cmd, Handler<AsyncResult<Map<String, String>>> handler);

    default SSDBClient info(Handler<AsyncResult<Map<String, String>>> handler)
    {
        return info(null, handler);
    }

    SSDBClient auth(String authKey, Handler<AsyncResult<Boolean>> handler);

    SSDBClient listAllowIp(Handler<AsyncResult<List<String>>> handler);

    SSDBClient addAllowIp(String prefixIp, Handler<AsyncResult<Void>> handler);

    SSDBClient delAllowIp(String prefixIp, Handler<AsyncResult<Void>> handler);

    SSDBClient listDenyIp(Handler<AsyncResult<List<String>>> handler);

    SSDBClient addDenyIp(String prefixIp, Handler<AsyncResult<Void>> handler);

    SSDBClient delDenyIp(String prefixIp, Handler<AsyncResult<Void>> handler);

    SSDBClient set(String key, String value, Handler<AsyncResult<Void>> handler);

    SSDBClient setx(String key, String value, int secondsTTL, Handler<AsyncResult<Void>> handler);

    SSDBClient setnx(String key, String value, Handler<AsyncResult<Integer>> handler);

    SSDBClient expire(String key, int secondsTTL, Handler<AsyncResult<Integer>> handler);

    SSDBClient ttl(String key, Handler<AsyncResult<Integer>> handler);

    SSDBClient get(String key, Handler<AsyncResult<String>> handler);

    SSDBClient getset(String key, String value, Handler<AsyncResult<String>> handler);

    SSDBClient del(String key, Handler<AsyncResult<Void>> handler);

    SSDBClient incr(String key, int incrValue, Handler<AsyncResult<Integer>> handler);

    default SSDBClient incr(String key, Handler<AsyncResult<Integer>> handler)
    {
        return incr(key, 1, handler);
    }

    SSDBClient exists(String key, Handler<AsyncResult<Boolean>> handler);

    SSDBClient getbit(String key, int offset, Handler<AsyncResult<Integer>> handler);

    SSDBClient setbit(String key, int offset, int bitValue, Handler<AsyncResult<Integer>> handler);

    SSDBClient bitcount(String key, int start, int end, Handler<AsyncResult<Integer>> handler);

    default SSDBClient bitcount(String key, Handler<AsyncResult<Integer>> handler)
    {
        return bitcount(key, 0, -1, handler);
    }

    default SSDBClient bitcount(String key, int start, Handler<AsyncResult<Integer>> handler)
    {
        return bitcount(key, start, -1, handler);
    }

    SSDBClient countbit(String key, int start, int end, Handler<AsyncResult<Integer>> handler);

    SSDBClient substr(String key, int start, int size, Handler<AsyncResult<String>> handler);

    SSDBClient strlen(String key, Handler<AsyncResult<Integer>> handler);

    SSDBClient keys(String keyStart, String keyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    default SSDBClient keys(int limit, Handler<AsyncResult<List<String>>> handler)
    {
        return keys("", "", limit, handler);
    }

    SSDBClient rkeys(String keyStart, String keyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    default SSDBClient rkeys(int limit, Handler<AsyncResult<List<String>>> handler)
    {
        return rkeys("", "", limit, handler);
    }

    SSDBClient scan(String keyStart, String keyEnd, int limit, Handler<AsyncResult<List<PairStringString>>> handler);

    default SSDBClient scan(int limit, Handler<AsyncResult<List<PairStringString>>> handler)
    {
        return scan("", "", limit, handler);
    }

    SSDBClient rscan(String keyStart, String keyEnd, int limit, Handler<AsyncResult<List<PairStringString>>> handler);

    default SSDBClient rscan(int limit, Handler<AsyncResult<List<PairStringString>>> handler)
    {
        return rscan("", "", limit, handler);
    }

    SSDBClient multiSet(Map<String, String> keyValues, Handler<AsyncResult<Void>> handler);

    SSDBClient multiGet(List<String> keys, Handler<AsyncResult<Map<String, String>>> handler);

    SSDBClient multiDel(List<String> keys, Handler<AsyncResult<Void>> handler);

    SSDBClient hset(String hashKey, String fieldKey, String fieldValue, Handler<AsyncResult<Integer>> handler);

    SSDBClient hget(String hashKey, String fieldKey, Handler<AsyncResult<String>> handler);

    SSDBClient hdel(String hashKey, String fieldKey, Handler<AsyncResult<Integer>> handler);

    SSDBClient hincr(String hashKey, String fieldKey, int incrValue, Handler<AsyncResult<Long>> handler);

    default SSDBClient hincr(String hashKey, String fieldKey, Handler<AsyncResult<Long>> handler)
    {
        return hincr(hashKey, fieldKey, 1, handler);
    }

    SSDBClient hexists(String hashKey, String fieldKey, Handler<AsyncResult<Boolean>> handler);

    SSDBClient hsize(String hashKey, Handler<AsyncResult<Long>> handler);

    SSDBClient hlist(String hashKeyStart, String hashKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    default SSDBClient hlist(int limit, Handler<AsyncResult<List<String>>> handler)
    {
        return hlist("", "", limit, handler);
    }

    SSDBClient hrlist(String hashKeyStart, String hashKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    default SSDBClient hrlist(int limit, Handler<AsyncResult<List<String>>> handler)
    {
        return hrlist("", "", limit, handler);
    }

    SSDBClient hkeys(String hashKey, String fieldKeyStart, String fieldKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    default SSDBClient hkeys(String hashKey, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        return hkeys(hashKey, "", "", limit, handler);
    }

    SSDBClient hgetall(String hashKey, Handler<AsyncResult<Map<String, String>>> handler);

    SSDBClient hscan(String hashKey, String fieldKeyStart, String fieldKeyEnd, int limit, Handler<AsyncResult<Map<String, String>>> handler);

    default SSDBClient hscan(String hashKey, int limit, Handler<AsyncResult<Map<String, String>>> handler)
    {
        return hscan(hashKey, "", "", limit, handler);
    }

    SSDBClient hrscan(String hashKey, String fieldKeyStart, String fieldKeyEnd, int limit, Handler<AsyncResult<List<PairStringString>>> handler);

    default SSDBClient hrscan(String hashKey, int limit, Handler<AsyncResult<List<PairStringString>>> handler)
    {
        return hrscan(hashKey, "", "", limit, handler);
    }

    SSDBClient hclear(String hashKey, Handler<AsyncResult<Long>> handler);

    SSDBClient multiHset(String hashKey, Map<String, String> fields, Handler<AsyncResult<Void>> handler);

    SSDBClient multiHget(String hashKey, List<String> fieldKeys, Handler<AsyncResult<Map<String, String>>> handler);

    SSDBClient multiHdel(String hashKey, List<String> fieldKeys, Handler<AsyncResult<Void>> handler);

    SSDBClient zset(String setKey, String itemKey, int score, Handler<AsyncResult<Integer>> handler);

    SSDBClient zget(String setKey, String itemKey, Handler<AsyncResult<String>> handler);

    SSDBClient zdel(String setKey, String itemKey, Handler<AsyncResult<Integer>> handler);

    SSDBClient zincr(String setKey, String itemKey, int incrValue, Handler<AsyncResult<Integer>> handler);

    SSDBClient zsize(String setKey, Handler<AsyncResult<Long>> handler);

    SSDBClient zlist(String setKeyStart, String setKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    default SSDBClient zlist(int limit, Handler<AsyncResult<List<String>>> handler)
    {
        return zlist("", "", limit, handler);
    }

    SSDBClient zrlist(String setKeyStart, String setKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    default SSDBClient zrlist(int limit, Handler<AsyncResult<List<String>>> handler)
    {
        return zrlist("", "", limit, handler);
    }

    SSDBClient zexists(String setKey, String itemKey, Handler<AsyncResult<Boolean>> handler);

    SSDBClient zkeys(String setKey, String itemKeyStart, int scoreStart, int scoreEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    SSDBClient zscan(String setKey, String itemKeyStart, int scoreStart, int scoreEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    SSDBClient zrscan(String setKey, String itemKeyStart, int scoreStart, int scoreEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    SSDBClient zrank(String setKey, String itemKey, Handler<AsyncResult<Integer>> handler);

    SSDBClient zrrank(String setKey, String itemKey, Handler<AsyncResult<Integer>> handler);

    SSDBClient zrange(String setKey, int offset, int limit, Handler<AsyncResult<List<PairStringInt>>> handler);

    SSDBClient zrrange(String setKey, int offset, int limit, Handler<AsyncResult<List<PairStringInt>>> handler);

    SSDBClient zclear(String setKey, Handler<AsyncResult<Long>> handler);

    SSDBClient zcount(String setKey, int scoreStart, int scoreEnd, Handler<AsyncResult<Long>> handler);

    SSDBClient zsum(String setKey, int scoreStart, int scoreEnd, Handler<AsyncResult<Long>> handler);

    SSDBClient zavg(String setKey, int scoreStart, int scoreEnd, Handler<AsyncResult<Long>> handler);

    SSDBClient zremRangeByRank(String setKey, int rankStart, int rankEnd, Handler<AsyncResult<Integer>> handler);

    SSDBClient zremRangeByScore(String setKey, int scoreStart, int scoreEnd, Handler<AsyncResult<Integer>> handler);

    SSDBClient zpopFront(String setKey, int limit, Handler<AsyncResult<List<PairStringInt>>> handler);

    SSDBClient zpopBack(String setKey, int limit, Handler<AsyncResult<List<PairStringInt>>> handler);

    SSDBClient multiZset(String setKey, Map<String, Integer> items, Handler<AsyncResult<Void>> handler);

    SSDBClient multiZget(String setKey, List<String> itemKeys, Handler<AsyncResult<List<PairStringInt>>> handler);

    SSDBClient multiZdel(String setKey, List<String> itemKeys, Handler<AsyncResult<Void>> handler);

    SSDBClient qpushFront(String listKey, String item, Handler<AsyncResult<Integer>> handler);

    SSDBClient qpushFront(String listKey, List<String> items, Handler<AsyncResult<Integer>> handler);

    SSDBClient qpushBack(String listKey, String item, Handler<AsyncResult<Long>> handler);

    SSDBClient qpushBack(String listKey, List<String> items, Handler<AsyncResult<Long>> handler);

    SSDBClient qpopFront(String listKey, int size, Handler<AsyncResult<List<String>>> handler);

    default SSDBClient qpopFront(String listKey, Handler<AsyncResult<List<String>>> handler)
    {
        return qpopFront(listKey, 1, handler);
    }

    SSDBClient qpopBack(String listKey, int size, Handler<AsyncResult<List<String>>> handler);

    default SSDBClient qpopBack(String listKey, Handler<AsyncResult<List<String>>> handler)
    {
        return qpopBack(listKey, 1, handler);
    }

    default SSDBClient qpush(String listKey, List<String> items, Handler<AsyncResult<Long>> handler)
    {
        return qpushBack(listKey, items, handler);
    }

    default SSDBClient qpush(String listKey, String item, Handler<AsyncResult<Long>> handler)
    {
        return qpushBack(listKey, item, handler);
    }

    default SSDBClient qpop(String listKey, int size, Handler<AsyncResult<List<String>>> handler)
    {
        return qpopFront(listKey, size, handler);
    }

    default SSDBClient qpop(String listKey, Handler<AsyncResult<List<String>>> handler)
    {
        return qpop(listKey, 1, handler);
    }

    SSDBClient qfront(String listKey, Handler<AsyncResult<String>> handler);

    SSDBClient qback(String listKey, Handler<AsyncResult<String>> handler);

    SSDBClient qsize(String listKey, Handler<AsyncResult<Long>> handler);

    SSDBClient qclear(String listKey, Handler<AsyncResult<Void>> handler);

    SSDBClient qget(String listKey, int index, Handler<AsyncResult<String>> handler);

    SSDBClient qset(String listKey, int index, String newValue, Handler<AsyncResult<Void>> handler);

    SSDBClient qrange(String listKey, int offset, int limit, Handler<AsyncResult<List<String>>> handler);

    SSDBClient qslice(String listKey, int begin, int end, Handler<AsyncResult<List<String>>> handler);

    SSDBClient qtrimFront(String listKey, int size, Handler<AsyncResult<Integer>> handler);

    SSDBClient qtrimBack(String listKey, int size, Handler<AsyncResult<Integer>> handler);

    SSDBClient qlist(String listKeyStart, String listKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    default SSDBClient qlist(int limit, Handler<AsyncResult<List<String>>> handler)
    {
        return qlist("", "", limit, handler);
    }

    SSDBClient qrlist(String listKeyStart, String listKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler);

    SSDBClient setAutoClose(boolean autoClose);

    SSDBClient beginBatch();

    void endBatch(Handler<AsyncResult<Void>> handler);
}
