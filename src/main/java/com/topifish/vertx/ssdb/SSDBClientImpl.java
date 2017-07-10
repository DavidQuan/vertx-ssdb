package com.topifish.vertx.ssdb;

import com.topifish.vertx.ssdb.models.PairStringInt;
import com.topifish.vertx.ssdb.models.PairStringString;
import com.topifish.vertx.ssdb.models.SSDBOptions;
import com.topifish.vertx.ssdb.util.F;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.List;
import java.util.Map;

/**
 * @author FanYongquan
 * @copyright (c) topifish game studio, create on 2017/7/7.
 */
public class SSDBClientImpl extends CompositeClient
{
    SSDBClientImpl(Vertx vertx, SSDBOptions options)
    {
        super(vertx, options);
    }

    @Override
    public void dbsize(Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "dbsize");
    }

    @Override
    public void info(String cmd, Handler<AsyncResult<Map<String, String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, queue -> {
            // pop ssdb-server
            queue.poll();
            return mapValue(queue);
        }), "info", cmd);
    }

    @Override
    public void auth(String authKey, Handler<AsyncResult<Boolean>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::booleanValue), "auth", authKey);
    }

    @Override
    public void listAllowIp(Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "list_allow_ip");
    }

    @Override
    public void addAllowIp(String prefixIp, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "add_allow_ip", prefixIp);
    }

    @Override
    public void delAllowIp(String prefixIp, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "del_allow_ip", prefixIp);
    }

    @Override
    public void listDenyIp(Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "list_deny_ip");
    }

    @Override
    public void addDenyIp(String prefixIp, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "add_deny_ip", prefixIp);
    }

    @Override
    public void delDenyIp(String prefixIp, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "del_deny_ip", prefixIp);
    }

    @Override
    public void set(String key, String value, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "set", key, value);
    }

    @Override
    public void setx(String key, String value, int secondsTTL, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "setx", key, value, secondsTTL);
    }

    @Override
    public void setnx(String key, String value, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "setnx", key, value);
    }

    @Override
    public void expire(String key, int secondsTTL, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "expire", key, secondsTTL);
    }

    @Override
    public void ttl(String key, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "ttl", key);
    }

    @Override
    public void get(String key, Handler<AsyncResult<String>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::stringValue), "get", key);
    }

    @Override
    public void getset(String key, String value, Handler<AsyncResult<String>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::stringValue), "getset", key, value);
    }

    @Override
    public void del(String key, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "del", key);
    }

    @Override
    public void incr(String key, int incrValue, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "incr", key, incrValue);
    }

    @Override
    public void exists(String key, Handler<AsyncResult<Boolean>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::booleanValue), "exists", key);
    }

    @Override
    public void getbit(String key, int offset, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "getbit", key, offset);
    }

    @Override
    public void setbit(String key, int offset, int bitValue, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "setbit", key, offset, bitValue);
    }

    @Override
    public void bitcount(String key, int start, int end, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "bitcount", key, start, end);
    }

    @Override
    public void countbit(String key, int start, int end, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "countbit", key, start, end);
    }

    @Override
    public void substr(String key, int start, int size, Handler<AsyncResult<String>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::stringValue), "substr", key, start, size);
    }

    @Override
    public void strlen(String key, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "strlen", key);
    }

    @Override
    public void keys(String keyStart, String keyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "keys", keyStart, keyEnd, limit);
    }

    @Override
    public void rkeys(String keyStart, String keyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "rkeys", keyStart, keyEnd, limit);
    }

    @Override
    public void scan(String keyStart, String keyEnd, int limit, Handler<AsyncResult<List<PairStringString>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listPairValue), "scan", keyStart, keyEnd, limit);
    }

    @Override
    public void rscan(String keyStart, String keyEnd, int limit, Handler<AsyncResult<List<PairStringString>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listPairValue), "rscan", keyStart, keyEnd, limit);
    }

    @Override
    public void multiSet(Map<String, String> keyValues, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "multi_set", toArray(keyValues));
    }

    @Override
    public void multiGet(List<String> keys, Handler<AsyncResult<Map<String, String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::mapValue), "multi_get", keys.toArray());
    }

    @Override
    public void multiDel(List<String> keys, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "multi_del", keys.toArray());
    }

    @Override
    public void hset(String hashKey, String fieldKey, String fieldValue, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "hset", hashKey, fieldKey, fieldValue);
    }

    @Override
    public void hget(String hashKey, String fieldKey, Handler<AsyncResult<String>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::stringValue), "hget", hashKey, fieldKey);
    }

    @Override
    public void hdel(String hashKey, String fieldKey, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "hdel", hashKey, fieldKey);
    }

    @Override
    public void hincr(String hashKey, String fieldKey, int incrValue, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "hincr", hashKey, fieldKey, incrValue);
    }

    @Override
    public void hexists(String hashKey, String fieldKey, Handler<AsyncResult<Boolean>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::booleanValue), "hexists", hashKey, fieldKey);
    }

    @Override
    public void hsize(String hashKey, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "hsize", hashKey);
    }

    @Override
    public void hlist(String hashKeyStart, String hashKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "hlist", hashKeyStart, hashKeyEnd, limit);
    }

    @Override
    public void hrlist(String hashKeyStart, String hashKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "hrlist", hashKeyStart, hashKeyEnd, limit);
    }

    @Override
    public void hkeys(String hashKey, String fieldKeyStart, String fieldKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "hrlist", hashKey, fieldKeyStart, fieldKeyEnd, limit);
    }

    @Override
    public void hgetall(String hashKey, Handler<AsyncResult<Map<String, String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::mapValue), "hgetall", hashKey);
    }

    @Override
    public void hscan(String hashKey, String fieldKeyStart, String fieldKeyEnd, int limit, Handler<AsyncResult<Map<String, String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::mapValue), "hscan", hashKey, fieldKeyStart, fieldKeyEnd, limit);
    }

    @Override
    public void hrscan(String hashKey, String fieldKeyStart, String fieldKeyEnd, int limit, Handler<AsyncResult<List<PairStringString>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listPairValue), "hscan", hashKey, fieldKeyStart, fieldKeyEnd, limit);
    }

    @Override
    public void hclear(String hashKey, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "hclear", hashKey);
    }

    @Override
    public void multiHset(String hashKey, Map<String, String> fields, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "multi_hset", hashKey, toArray(fields));
    }

    @Override
    public void multiHget(String hashKey, List<String> fieldKeys, Handler<AsyncResult<Map<String, String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::mapValue), "multi_hget", hashKey, fieldKeys.toArray());
    }

    @Override
    public void multiHdel(String hashKey, List<String> fieldKeys, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "multi_hdel", hashKey, fieldKeys.toArray());
    }

    @Override
    public void zset(String setKey, String itemKey, int score, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "zset", setKey, itemKey, score);
    }

    @Override
    public void zget(String setKey, String itemKey, Handler<AsyncResult<String>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::stringValue), "zget", setKey, itemKey);
    }

    @Override
    public void zdel(String setKey, String itemKey, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "zdel", setKey, itemKey);
    }

    @Override
    public void zincr(String setKey, String itemKey, int incrValue, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "zincr", setKey, itemKey, incrValue);
    }

    @Override
    public void zsize(String setKey, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "zsize", setKey);
    }

    @Override
    public void zlist(String setKeyStart, String setKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "zlist", setKeyStart, setKeyEnd, limit);
    }

    @Override
    public void zrlist(String setKeyStart, String setKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "zrlist", setKeyStart, setKeyEnd, limit);
    }

    @Override
    public void zexists(String setKey, String itemKey, Handler<AsyncResult<Boolean>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::booleanValue), "zexists", setKey, itemKey);
    }

    @Override
    public void zkeys(String setKey, String itemKeyStart, int scoreStart, int scoreEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "zkeys", setKey, itemKeyStart, scoreStart, scoreEnd, limit);
    }

    @Override
    public void zscan(String setKey, String itemKeyStart, int scoreStart, int scoreEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "zscan", setKey, itemKeyStart, scoreStart, scoreEnd, limit);
    }

    @Override
    public void zrscan(String setKey, String itemKeyStart, int scoreStart, int scoreEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "zrscan", setKey, itemKeyStart, scoreStart, scoreEnd, limit);
    }

    @Override
    public void zrank(String setKey, String itemKey, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue_1), "zrank", setKey, itemKey);
    }

    @Override
    public void zrrank(String setKey, String itemKey, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue_1), "zrrank", setKey, itemKey);
    }

    @Override
    public void zrange(String setKey, int offset, int limit, Handler<AsyncResult<List<PairStringInt>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listPairStringIntValue), "zrange", setKey, offset, limit);
    }

    @Override
    public void zrrange(String setKey, int offset, int limit, Handler<AsyncResult<List<PairStringInt>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listPairStringIntValue), "zrrange", setKey, offset, limit);
    }

    @Override
    public void zclear(String setKey, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "zclear", setKey);
    }

    @Override
    public void zcount(String setKey, int scoreStart, int scoreEnd, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "zcount", setKey, scoreStart, scoreEnd);
    }

    @Override
    public void zsum(String setKey, int scoreStart, int scoreEnd, Handler<AsyncResult<Long>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::longValue), "zsum", setKey, scoreStart, scoreEnd);
    }

    @Override
    public void zavg(String setKey, int scoreStart, int scoreEnd, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "zavg", setKey, scoreStart, scoreEnd);
    }

    @Override
    public void zremRangeByRank(String setKey, int rankStart, int rankEnd, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "zremrangebyrank", setKey, rankStart, rankEnd);
    }

    @Override
    public void zremRangeByScore(String setKey, int scoreStart, int scoreEnd, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "zremrangebyscore", setKey, scoreStart, scoreEnd);
    }

    @Override
    public void zpopFront(String setKey, int limit, Handler<AsyncResult<List<PairStringInt>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listPairStringIntValue), "zpop_front", setKey, limit);
    }

    @Override
    public void zpopBack(String setKey, int limit, Handler<AsyncResult<List<PairStringInt>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listPairStringIntValue), "zpop_back", setKey, limit);
    }

    @Override
    public void multiZset(String setKey, Map<String, Integer> items, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "multi_zset", setKey, toArray(items));
    }

    @Override
    public void multiZget(String setKey, List<String> itemKeys, Handler<AsyncResult<List<PairStringInt>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listPairStringIntValue), "multi_zget", setKey, itemKeys.toArray());
    }

    @Override
    public void multiZdel(String setKey, List<String> itemKeys, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "multi_zdel", setKey, itemKeys.toArray());
    }

    @Override
    public void qpushFront(String listKey, String item, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "qpush_front", listKey, item);
    }

    @Override
    public void qpushFront(String listKey, List<String> items, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "qpush_front", listKey, items.toArray());
    }

    @Override
    public void qpushBack(String listKey, String item, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "qpush_back", listKey, item);
    }

    @Override
    public void qpushBack(String listKey, List<String> items, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "qpush_back", listKey, items.toArray());
    }

    @Override
    public void qpopFront(String listKey, int size, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "qpop_front", listKey, size);
    }

    @Override
    public void qpopBack(String listKey, int size, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "qpop_back", listKey, size);
    }

    @Override
    public void qfront(String listKey, Handler<AsyncResult<String>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::stringValue), "qfront", listKey);
    }

    @Override
    public void qback(String listKey, Handler<AsyncResult<String>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::stringValue), "qback", listKey);
    }

    @Override
    public void qsize(String listKey, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "qsize", listKey);
    }

    @Override
    public void qclear(String listKey, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "qclear", listKey);
    }

    @Override
    public void qget(String listKey, int index, Handler<AsyncResult<String>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::stringValue), "qget", listKey, index);
    }

    @Override
    public void qset(String listKey, int index, String newValue, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "qset", listKey, index, newValue);
    }

    @Override
    public void qrange(String listKey, int offset, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "qrange", listKey, offset, limit);
    }

    @Override
    public void qslice(String listKey, int begin, int end, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "qslice", listKey, begin, end);
    }

    @Override
    public void qtrimFront(String listKey, int size, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "qtrim_front", listKey, size);
    }

    @Override
    public void qtrimBack(String listKey, int size, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "qtrim_back", listKey, size);
    }

    @Override
    public void qlist(String listKeyStart, String listKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "qlist", listKeyStart, listKeyEnd, limit);
    }

    @Override
    public void qrlist(String listKeyStart, String listKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "qrlist", listKeyStart, listKeyEnd, limit);
    }
}
