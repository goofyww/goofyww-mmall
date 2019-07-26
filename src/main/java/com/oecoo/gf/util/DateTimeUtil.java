package com.oecoo.gf.util;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gf on 2018/4/30.
 */
@Slf4j
public class DateTimeUtil {

    public static final String FORMAT_ON_SECEND_24 = "yyyy-MM-dd HH:mm:ss";    //年-月-日 时:分:秒 （默认）（24小时制）

    public static final String FORMAT_ON_SECEND_12 = "yyyy-MM-dd hh:mm:ss";    //年-月-日 时:分:秒 （12小时制）

    public static final String formatTimeOnMinStyle = "yyyy-MM-dd HH:mm";       //年-月-日 时:分

    public static final String formatDateStyle = "yyyy-MM-dd";          //年-月-日

    private static Date date;

    private static SimpleDateFormat simpleDateFormat;

    public static final String EMPTY = "";

    /**
     * String ====> Date
     * @param str           字符
     * @param formatStyle   格式
     * @return
     */
    public static Date strToDate(String str, String formatStyle){
        simpleDateFormat = new SimpleDateFormat(formatStyle);
        date = null;
        try {
            date = simpleDateFormat.parse(str);
        } catch (ParseException e) {
           log.error("时间格式输入错误,请指定格式为"+formatStyle,e);
        }
        return date;
    }

    /**
     * String ====> Date 默认指定格式为 yyyy-MM-dd HH:mm:ss
     * @param str
     * @return
     */
    public static Date strToDate(String str){
        simpleDateFormat = new SimpleDateFormat(DateTimeUtil.FORMAT_ON_SECEND_24);
        date = null;
        try {
            date = simpleDateFormat.parse(str);
        } catch (ParseException e) {
            log.error("时间格式输入错误,请指定格式为"+DateTimeUtil.FORMAT_ON_SECEND_24,e);
        }
        return date;
    }

    /**
     * Date =====> String
     * @param updateTime     时间
     * @param formatStyle   格式
     * @return
     */
    public static String dateToStr(Date updateTime,String formatStyle){
        if(updateTime == null){
            return DateTimeUtil.EMPTY;
        }
        simpleDateFormat = new SimpleDateFormat(formatStyle);
        String resultTime = simpleDateFormat.format(date);
        return resultTime;
    }

    /**
     * date ======> String 默认指定格式为 yyyy-MM-dd HH:mm:ss
     * @param updateTime
     * @return
     */
    public static String dateToStr(Date updateTime) {
        if(updateTime == null){
            return DateTimeUtil.EMPTY;
        }
        simpleDateFormat = new SimpleDateFormat(DateTimeUtil.FORMAT_ON_SECEND_24);
        String resultTime = simpleDateFormat.format(updateTime);
        return resultTime;
    }

}
