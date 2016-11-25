package com.meituan.service.mobile.tips.queryResult;

import com.google.common.collect.Lists;
import com.meituan.jmonitor.JMonitor;
import com.meituan.service.mobile.tips.consts.JMonitorKey;
import com.meituan.service.mobile.tips.consts.TipsConsts;
import com.meituan.service.mobile.tips.utils.RedisUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.List;

/**
 * 实时结果数redis
 *
 * @author chendayao
 * @version 1.0
 * @created 2015-03-13
 */
public class QueryResultRedisClient {

    private static final Logger logger = LoggerFactory.getLogger(TipsConsts.CLIENTLOG);

    // redis线程池
    private static JedisPool jedisPoolPoi;

    public QueryResultRedisClient() {
        jedisPoolPoi = RedisUtils.getJedisPool(TipsConsts.REDISCONF);
    }

    public List<String> getQueryResult(String [] queryList) {
        Jedis jedisPoi = null;
        List<String> results = null;
        try {
            if (queryList.length > 0) {
                jedisPoi = jedisPoolPoi.getResource();
                results = jedisPoi.mget(queryList);
            } else {
                results = Lists.newArrayList();
            }
        } catch (JedisConnectionException je) {
            JMonitor.add(JMonitorKey.QueryResultError);
            if (jedisPoi != null) {
                jedisPoolPoi.returnBrokenResource(jedisPoi);
                jedisPoi = null;
            }
            logger.error(je.getMessage(), je);
            logger.error("queryResult failed to handle: " + StringUtils.join(queryList, ","));
        } catch (Exception e) {
            JMonitor.add(JMonitorKey.QueryResultError);
            logger.error(e.getMessage(), e);
            logger.error("queryResult failed to handle: " + StringUtils.join(queryList, ","));
        } finally {
            JMonitor.add(JMonitorKey.QueryResultCount);
            if (jedisPoi != null) {
                jedisPoolPoi.returnResource(jedisPoi);
            }
        }

        return results;
    }
}
