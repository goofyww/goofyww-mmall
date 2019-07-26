package com.oecoo.gf.task;

import com.oecoo.gf.common.Const;
import com.oecoo.gf.common.RedissonManager;
import com.oecoo.gf.service.IOrderService;
import com.oecoo.gf.util.PropertiesUtil;
import com.oecoo.gf.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * @author gf
 * @date 2019/5/4
 */
@Slf4j
@Component
public class CloseOrderTask {

    @Autowired
    private RedissonManager redissonManager;

    @Autowired
    private IOrderService iOrderService;

    private static final int HOUR = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));

    @PreDestroy // tomcat shutdown.sh手动关闭时执行此方法
    public void delLock() {
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_LOCK_NAME);
        log.info("Tomcat shut down 释放锁 {}", Const.REDIS_LOCK.CLOSE_ORDER_LOCK_NAME);
    }

    //@Scheduled(cron = "0 */1 * * * ?") //每1分钟（每个1分钟的整数倍）
    public void closeOrderTaskV1() {
        log.info("关闭订单定时任务启动");
        iOrderService.closeOrder(HOUR);
        log.info("关闭订单定时任务结束");
    }

    //@Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV2() {
        log.info("关闭订单定时任务启动");
        Long lockTimeLength = Long.parseLong(PropertiesUtil.getProperty("lock.time.length", "5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_LOCK_NAME, String.valueOf(System.currentTimeMillis() + lockTimeLength));
        if (setnxResult != null && setnxResult.intValue() == 1) {
            //获取锁成功，执行相应的关闭订单操作
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_LOCK_NAME);
        } else {
            log.info("没有获得分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_LOCK_NAME);
        }
        log.info("关闭订单定时任务结束");
    }

    //@Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV3() {
        log.info("关闭订单定时任务启动");
        Long lockTimeLength = Long.parseLong(PropertiesUtil.getProperty("lock.time.length", "5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_LOCK_NAME, String.valueOf(System.currentTimeMillis() + lockTimeLength));
        if (setnxResult != null && setnxResult.intValue() == 1) {
            //获取锁成功，执行相应的关闭订单操作
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_LOCK_NAME);
        } else {
            //未获取到锁，继续判断 时间戳，看是否可以重置并获取到锁
            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_LOCK_NAME);
            if (lockValueStr != null && System.currentTimeMillis() > Long.valueOf(lockValueStr)) {
                String getSetResult = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_LOCK_NAME, String.valueOf(System.currentTimeMillis() + lockTimeLength));
                //再次用当前时间戳getset
                //返回给定的key的旧值，->旧值判断，是否可以获取锁
                //当key没有旧值时，即key不存在时，返回nil，->获取锁
                //getSet : 返回旧值，设置新值
                if (getSetResult == null || (getSetResult != null && StringUtils.equals(lockValueStr, getSetResult))) {
                    //获取锁成功
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_LOCK_NAME);
                } else {
                    log.info("没有获得分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_LOCK_NAME);
                }
            } else {
                log.info("没有获得分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_LOCK_NAME);
            }
        }
        log.info("关闭订单定时任务结束");
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV4() {
        log.info("关闭订单定时任务启动");
        RLock lock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_LOCK_NAME);
        boolean getLock = false;
        try {
            if (getLock = lock.tryLock(0, 50, TimeUnit.SECONDS)) {
                log.info("Redisson获取到锁：{}，ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_LOCK_NAME, Thread.currentThread().getName());
                // iOrderService.closeOrder(HOUR);
            } else {
                log.info("Redisson没有获取到锁：{}，ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_LOCK_NAME, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("Redisson获取分布式锁异常：{}", e);
        } finally {
            if (!getLock) {
                return;
            }
            lock.unlock();
            log.info("Redisson分布式锁释放锁");
        }
    }

    private void closeOrder(String lockName) {
        RedisShardedPoolUtil.expire(lockName, 5);//有效期5秒，防止死锁
        log.info("获取{}，ThreadName:{}", lockName, Thread.currentThread().getName());
        iOrderService.closeOrder(HOUR);
        RedisShardedPoolUtil.del(lockName);
        log.info("释放{}，ThreadName:{}", lockName, Thread.currentThread().getName());
        log.info("===========================================================");
    }


}
