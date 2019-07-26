package com.oecoo.gf.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Properties工具类
 */
@Slf4j
public class PropertiesUtil {

    private static Properties props;

    private static final String FILE_NAME = "/mmall.properties";

    static {
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getResourceAsStream(FILE_NAME), "UTF-8"));
        } catch (IOException e) {
            log.error("配置文件读取异常", e);
        }
    }

    public static Map<String, String> getPropertyList(String keyPrefix) {
        Map<String, String> map = Maps.newHashMap();
        props.forEach((t, c) -> map.put(t.toString(), c.toString()));
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            String key = entry.getKey();
            if (!key.startsWith(keyPrefix)) {
                it.remove();
            }
        }
        return map;
    }

    public static String getProperty(String key) {
        String value = props.getProperty(key.trim());//防止空格 .trim()
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key, String defaultValue) {
        String value = props.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            value = defaultValue;
        }
        return value.trim();
    }


}
