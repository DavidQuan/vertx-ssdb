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
    protected SSDBClientImpl(Vertx vertx, SSDBOptions options)
    {
        super(vertx, options);
    }

    @Override
    public SSDBClient ping(Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "ping");
        return this;
    }

    public void close(Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "quit");
    }

    @Override
    public SSDBClient dbsize(Handler<AsyncResult<Long>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::longValue), "dbsize");
        return this;
    }

    @Override
    public SSDBClient info(String cmd, Handler<AsyncResult<Map<String, String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, queue -> {
            // pop ssdb-server
            queue.poll();
            return mapValue(queue);
        }), "info", cmd);
        return this;
    }

    @Override
    public SSDBClient auth(String authKey, Handler<AsyncResult<Boolean>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::booleanValue), "auth", authKey);
        return this;
    }

    @Override
    public SSDBClient listAllowIp(Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "list_allow_ip");
        return this;
    }

    @Override
    public SSDBClient addAllowIp(String prefixIp, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "add_allow_ip", prefixIp);
        return this;
    }

    @Override
    public SSDBClient delAllowIp(String prefixIp, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "del_allow_ip", prefixIp);
        return this;
    }

    @Override
    public SSDBClient listDenyIp(Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "list_deny_ip");
        return this;
    }

    @Override
    public SSDBClient addDenyIp(String prefixIp, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "add_deny_ip", prefixIp);
        return this;
    }

    @Override
    public SSDBClient delDenyIp(String prefixIp, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "del_deny_ip", prefixIp);
        return this;
    }

    @Override
    public SSDBClient set(String key, String value, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "set", key, value);
        return this;
    }

    @Override
    public SSDBClient setx(String key, String value, int secondsTTL, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "setx", key, value, secondsTTL);
        return this;
    }

    @Override
    public SSDBClient setnx(String key, String value, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "setnx", key, value);
        return this;
    }

    @Override
    public SSDBClient expire(String key, int secondsTTL, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "expire", key, secondsTTL);
        return this;
    }

    @Override
    public SSDBClient ttl(String key, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "ttl", key);
        return this;
    }

    @Override
    public SSDBClient get(String key, Handler<AsyncResult<String>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::stringValue), "get", key);
        return this;
    }

    @Override
    public SSDBClient getset(String key, String value, Handler<AsyncResult<String>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::stringValue), "getset", key, value);
        return this;
    }

    @Override
    public SSDBClient del(String key, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "del", key);
        return this;
    }

    @Override
    public SSDBClient incr(String key, int incrValue, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "incr", key, incrValue);
        return this;
    }

    @Override
    public SSDBClient exists(String key, Handler<AsyncResult<Boolean>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::booleanValue), "exists", key);
        return this;
    }

    @Override
    public SSDBClient getbit(String key, int offset, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "getbit", key, offset);
        return this;
    }

    @Override
    public SSDBClient setbit(String key, int offset, int bitValue, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "setbit", key, offset, bitValue);
        return this;
    }

    @Override
    public SSDBClient bitcount(String key, int start, int end, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "bitcount", key, start, end);
        return this;
    }

    @Override
    public SSDBClient countbit(String key, int start, int end, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "countbit", key, start, end);
        return this;
    }

    @Override
    public SSDBClient substr(String key, int start, int size, Handler<AsyncResult<String>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::stringValue), "substr", key, start, size);
        return this;
    }

    @Override
    public SSDBClient strlen(String key, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "strlen", key);
        return this;
    }

    @Override
    public SSDBClient keys(String keyStart, String keyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "keys", keyStart, keyEnd, limit);
        return this;
    }

    @Override
    public SSDBClient rkeys(String keyStart, String keyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "rkeys", keyStart, keyEnd, limit);
        return this;
    }

    @Override
    public SSDBClient scan(String keyStart, String keyEnd, int limit, Handler<AsyncResult<List<PairStringString>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listPairValue), "scan", keyStart, keyEnd, limit);
        return this;
    }

    @Override
    public SSDBClient rscan(String keyStart, String keyEnd, int limit, Handler<AsyncResult<List<PairStringString>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listPairValue), "rscan", keyStart, keyEnd, limit);
        return this;
    }

    @Override
    public SSDBClient multiSet(Map<String, String> keyValues, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "multi_set", toArray(keyValues));
        return this;
    }

    @Override
    public SSDBClient multiGet(List<String> keys, Handler<AsyncResult<Map<String, String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::mapValue), "multi_get", keys.toArray());
        return this;
    }

    @Override
    public SSDBClient multiDel(List<String> keys, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "multi_del", keys.toArray());
        return this;
    }

    @Override
    public SSDBClient hset(String hashKey, String fieldKey, String fieldValue, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "hset", hashKey, fieldKey, fieldValue);
        return this;
    }

    @Override
    public SSDBClient hget(String hashKey, String fieldKey, Handler<AsyncResult<String>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::stringValue), "hget", hashKey, fieldKey);
        return this;
    }

    @Override
    public SSDBClient hdel(String hashKey, String fieldKey, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "hdel", hashKey, fieldKey);
        return this;
    }

    @Override
    public SSDBClient hincr(String hashKey, String fieldKey, int incrValue, Handler<AsyncResult<Long>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::longValue), "hincr", hashKey, fieldKey, incrValue);
        return this;
    }

    @Override
    public SSDBClient hexists(String hashKey, String fieldKey, Handler<AsyncResult<Boolean>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::booleanValue), "hexists", hashKey, fieldKey);
        return this;
    }

    @Override
    public SSDBClient hsize(String hashKey, Handler<AsyncResult<Long>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::longValue), "hsize", hashKey);
        return this;
    }

    @Override
    public SSDBClient hlist(String hashKeyStart, String hashKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "hlist", hashKeyStart, hashKeyEnd, limit);
        return this;
    }

    @Override
    public SSDBClient hrlist(String hashKeyStart, String hashKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "hrlist", hashKeyStart, hashKeyEnd, limit);
        return this;
    }

    @Override
    public SSDBClient hkeys(String hashKey, String fieldKeyStart, String fieldKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "hrlist", hashKey, fieldKeyStart, fieldKeyEnd, limit);
        return this;
    }

    @Override
    public SSDBClient hgetall(String hashKey, Handler<AsyncResult<Map<String, String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::mapValue), "hgetall", hashKey);
        return this;
    }

    @Override
    public SSDBClient hscan(String hashKey, String fieldKeyStart, String fieldKeyEnd, int limit, Handler<AsyncResult<Map<String, String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::mapValue), "hscan", hashKey, fieldKeyStart, fieldKeyEnd, limit);
        return this;
    }

    @Override
    public SSDBClient hrscan(String hashKey, String fieldKeyStart, String fieldKeyEnd, int limit, Handler<AsyncResult<List<PairStringString>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listPairValue), "hscan", hashKey, fieldKeyStart, fieldKeyEnd, limit);
        return this;
    }

    @Override
    public SSDBClient hclear(String hashKey, Handler<AsyncResult<Long>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::longValue), "hclear", hashKey);
        return this;
    }

    @Override
    public SSDBClient multiHset(String hashKey, Map<String, String> fields, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "multi_hset", hashKey, toArray(fields));
        return this;
    }

    @Override
    public SSDBClient multiHget(String hashKey, List<String> fieldKeys, Handler<AsyncResult<Map<String, String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::mapValue), "multi_hget", hashKey, fieldKeys.toArray());
        return this;
    }

    @Override
    public SSDBClient multiHdel(String hashKey, List<String> fieldKeys, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "multi_hdel", hashKey, fieldKeys.toArray());
        return this;
    }

    @Override
    public SSDBClient zset(String setKey, String itemKey, int score, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "zset", setKey, itemKey, score);
        return this;
    }

    @Override
    public SSDBClient zget(String setKey, String itemKey, Handler<AsyncResult<String>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::stringValue), "zget", setKey, itemKey);
        return this;
    }

    @Override
    public SSDBClient zdel(String setKey, String itemKey, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "zdel", setKey, itemKey);
        return this;
    }

    @Override
    public SSDBClient zincr(String setKey, String itemKey, int incrValue, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "zincr", setKey, itemKey, incrValue);
        return this;
    }

    @Override
    public SSDBClient zsize(String setKey, Handler<AsyncResult<Long>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::longValue), "zsize", setKey);
        return this;
    }

    @Override
    public SSDBClient zlist(String setKeyStart, String setKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "zlist", setKeyStart, setKeyEnd, limit);
        return this;
    }

    @Override
    public SSDBClient zrlist(String setKeyStart, String setKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "zrlist", setKeyStart, setKeyEnd, limit);
        return this;
    }

    @Override
    public SSDBClient zexists(String setKey, String itemKey, Handler<AsyncResult<Boolean>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::booleanValue), "zexists", setKey, itemKey);
        return this;
    }

    @Override
    public SSDBClient zkeys(String setKey, String itemKeyStart, int scoreStart, int scoreEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "zkeys", setKey, itemKeyStart, scoreStart, scoreEnd, limit);
        return this;
    }

    @Override
    public SSDBClient zscan(String setKey, String itemKeyStart, int scoreStart, int scoreEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "zscan", setKey, itemKeyStart, scoreStart, scoreEnd, limit);
        return this;
    }

    @Override
    public SSDBClient zrscan(String setKey, String itemKeyStart, int scoreStart, int scoreEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "zrscan", setKey, itemKeyStart, scoreStart, scoreEnd, limit);
        return this;
    }

    @Override
    public SSDBClient zrank(String setKey, String itemKey, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue_1), "zrank", setKey, itemKey);
        return this;
    }

    @Override
    public SSDBClient zrrank(String setKey, String itemKey, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue_1), "zrrank", setKey, itemKey);
        return this;
    }

    @Override
    public SSDBClient zrange(String setKey, int offset, int limit, Handler<AsyncResult<List<PairStringInt>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listPairStringIntValue), "zrange", setKey, offset, limit);
        return this;
    }

    @Override
    public SSDBClient zrrange(String setKey, int offset, int limit, Handler<AsyncResult<List<PairStringInt>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listPairStringIntValue), "zrrange", setKey, offset, limit);
        return this;
    }

    @Override
    public SSDBClient zclear(String setKey, Handler<AsyncResult<Long>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::longValue), "zclear", setKey);
        return this;
    }

    @Override
    public SSDBClient zcount(String setKey, int scoreStart, int scoreEnd, Handler<AsyncResult<Long>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::longValue), "zcount", setKey, scoreStart, scoreEnd);
        return this;
    }

    @Override
    public SSDBClient zsum(String setKey, int scoreStart, int scoreEnd, Handler<AsyncResult<Long>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::longValue), "zsum", setKey, scoreStart, scoreEnd);
        return this;
    }

    @Override
    public SSDBClient zavg(String setKey, int scoreStart, int scoreEnd, Handler<AsyncResult<Long>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::longValue), "zavg", setKey, scoreStart, scoreEnd);
        return this;
    }

    @Override
    public SSDBClient zremRangeByRank(String setKey, int rankStart, int rankEnd, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "zremrangebyrank", setKey, rankStart, rankEnd);
        return this;
    }

    @Override
    public SSDBClient zremRangeByScore(String setKey, int scoreStart, int scoreEnd, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "zremrangebyscore", setKey, scoreStart, scoreEnd);
        return this;
    }

    @Override
    public SSDBClient zpopFront(String setKey, int limit, Handler<AsyncResult<List<PairStringInt>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listPairStringIntValue), "zpop_front", setKey, limit);
        return this;
    }

    @Override
    public SSDBClient zpopBack(String setKey, int limit, Handler<AsyncResult<List<PairStringInt>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listPairStringIntValue), "zpop_back", setKey, limit);
        return this;
    }

    @Override
    public SSDBClient multiZset(String setKey, Map<String, Integer> items, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "multi_zset", setKey, toArray(items));
        return this;
    }

    @Override
    public SSDBClient multiZget(String setKey, List<String> itemKeys, Handler<AsyncResult<List<PairStringInt>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listPairStringIntValue), "multi_zget", setKey, itemKeys.toArray());
        return this;
    }

    @Override
    public SSDBClient multiZdel(String setKey, List<String> itemKeys, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "multi_zdel", setKey, itemKeys.toArray());
        return this;
    }

    @Override
    public SSDBClient qpushFront(String listKey, String item, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "qpush_front", listKey, item);
        return this;
    }

    @Override
    public SSDBClient qpushFront(String listKey, List<String> items, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "qpush_front", listKey, items.toArray());
        return this;
    }

    @Override
    public SSDBClient qpushBack(String listKey, String item, Handler<AsyncResult<Long>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::longValue), "qpush_back", listKey, item);
        return this;
    }

    @Override
    public SSDBClient qpushBack(String listKey, List<String> items, Handler<AsyncResult<Long>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::longValue), "qpush_back", listKey, items.toArray());
        return this;
    }

    @Override
    public SSDBClient qpopFront(String listKey, int size, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "qpop_front", listKey, size);
        return this;
    }

    @Override
    public SSDBClient qpopBack(String listKey, int size, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "qpop_back", listKey, size);
        return this;
    }

    @Override
    public SSDBClient qfront(String listKey, Handler<AsyncResult<String>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::stringValue), "qfront", listKey);
        return this;
    }

    @Override
    public SSDBClient qback(String listKey, Handler<AsyncResult<String>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::stringValue), "qback", listKey);
        return this;
    }

    @Override
    public SSDBClient qsize(String listKey, Handler<AsyncResult<Long>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::longValue), "qsize", listKey);
        return this;
    }

    @Override
    public SSDBClient qclear(String listKey, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "qclear", listKey);
        return this;
    }

    @Override
    public SSDBClient qget(String listKey, int index, Handler<AsyncResult<String>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::stringValue), "qget", listKey, index);
        return this;
    }

    @Override
    public SSDBClient qset(String listKey, int index, String newValue, Handler<AsyncResult<Void>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::voidValue), "qset", listKey, index, newValue);
        return this;
    }

    @Override
    public SSDBClient qrange(String listKey, int offset, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "qrange", listKey, offset, limit);
        return this;
    }

    @Override
    public SSDBClient qslice(String listKey, int begin, int end, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "qslice", listKey, begin, end);
        return this;
    }

    @Override
    public SSDBClient qtrimFront(String listKey, int size, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "qtrim_front", listKey, size);
        return this;
    }

    @Override
    public SSDBClient qtrimBack(String listKey, int size, Handler<AsyncResult<Integer>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::intValue), "qtrim_back", listKey, size);
        return this;
    }

    @Override
    public SSDBClient qlist(String listKeyStart, String listKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "qlist", listKeyStart, listKeyEnd, limit);
        return this;
    }

    @Override
    public SSDBClient qrlist(String listKeyStart, String listKeyEnd, int limit, Handler<AsyncResult<List<String>>> handler)
    {
        sendCommand(F.ofSucceeded(handler, this::listValue), "qrlist", listKeyStart, listKeyEnd, limit);
        return this;
    }

    @Override
    public SSDBClient beginBatch()
    {
        initBatch();
        return this;
    }

    @Override
    public void endBatch(Handler<AsyncResult<Void>> handler)
    {
        sendCommands(handler);
    }
}
