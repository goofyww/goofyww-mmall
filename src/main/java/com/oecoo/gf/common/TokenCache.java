package com.oecoo.gf.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by gf on 2018/4/27.
 * 设置缓冲
 */
@Slf4j
public class TokenCache {

    public static final String TOKEN_PREFIX = "token_";

    //采用google缓冲 URL算法                    本地缓冲                               初始值：1000          最大值：10000
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000)
            //缓冲时间         30分钟                创建
            .expireAfterAccess(30, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
                //默认的数据加载实现,当调用get取值的时候,如果key没有对应的值,就调用这个方法进行加载.
                public String load(String s) throws Exception {
                    //为防止nullpointerexception 设置为字符串形式
                    return "null";
                }
            });
    //set TokenCache
    public static void setKey(String key,String value){
        localCache.put(key,value);
    }
    //by key get CacheValue
    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        }catch (Exception e){
            log.error("localCache Get Error",e);
        }
        return null;
    }
}
