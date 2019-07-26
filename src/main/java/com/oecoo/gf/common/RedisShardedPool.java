package com.oecoo.gf.common;

import com.oecoo.gf.util.PropertiesUtil;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis连接池
 */
public class RedisShardedPool {

    //sharded jedis连接池
    private static ShardedJedisPool pool;

    //最大连接池
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));

    //在jedispool中最大的idle状态(空闲的)的jedis实例的个数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));

    //在jedispool中最小的idle状态(空闲的)的jedis实例的个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "20"));

    //在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true,则得到的这个jedis实例肯定是可以用的。
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));

    //在return一个jedis实例的时候，是否要进行验证操作，如果赋值true，则放回jedispool的jedis实例肯定是可以用的。
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "false"));

    private static String host1 = PropertiesUtil.getProperty("redis1.host");
    private static Integer port1 = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    private static String host2 = PropertiesUtil.getProperty("redis2.host");
    private static Integer port2 = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    static {
        initPool();
    }

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true); // 连接耗尽时，是否阻塞，false会抛出异常，true阻塞直到超时。默认为 true


        JedisShardInfo info1 = new JedisShardInfo(host1, port1, 1000 * 2, 2);
        JedisShardInfo info2 = new JedisShardInfo(host2, port2, 1000 * 2, 2);

        List<JedisShardInfo> infoList = new ArrayList<>(2);
        infoList.add(info1);
        infoList.add(info2);

        //Hashing.MURMUR_HASH 一致性算法分片，Sharded中有默认分配虚拟节点策略
        pool = new ShardedJedisPool(config, infoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    public static ShardedJedis getJedis() {
        return pool.getResource();
    }

    //----------------------------- 把 Jedis 放 回 连 接 池 ---------------------------------------//

    /**
     * 放回损坏连接到连接池
     *
     * @param jedis
     */
    public static void returnBrokenResource(ShardedJedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    /**
     * 放回连接到连接池
     *
     * @param jedis
     */
    public static void returnResource(ShardedJedis jedis) {
        pool.returnResource(jedis);
    }

    public static void main(String[] args) {

        ShardedJedis jedis = pool.getResource();
        for (int i = 1; i <= 10; i++) {
            jedis.set("key" + i, "value" + i);
        }
        returnResource(jedis);

        System.out.println("program is end");
    }


}
