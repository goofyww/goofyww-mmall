package com.oecoo.gf.common;

import com.oecoo.gf.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author gf
 * @date 2019/5/5
 */
@Slf4j
@Component
public class RedissonManager {

    private Config config = new Config();

    private Redisson redisson = null;

    private static String host1 = PropertiesUtil.getProperty("redis1.host");
    private static Integer port1 = Integer.parseInt(PropertiesUtil.getProperty("redis1.port"));

    private static String host2 = PropertiesUtil.getProperty("redis2.host");
    private static Integer port2 = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));

    public Redisson getRedisson() {
        return redisson;
    }

    @PostConstruct
    private void init() {
        try {
            config.useSingleServer().setAddress(new StringBuffer().append(host1).append(":").append(port1).toString());
            redisson = (Redisson) Redisson.create(config);
            log.info("Redisson init complete!");
        } catch (Exception e) {
            log.error("Redisson init error:{}", e);
        }
    }


}
