package com.oecoo.gf.common;

/**
 * Created by gf on 2018/4/26.
 */
/**
 * 枚举类 作用:作为SeverResponse（服务响应对象的）响应code
 */
public enum  ResponseCode {

    /**
     * 枚举方法
     * --------------------------------------
     *  这里的两个参数作为枚举构造器传入的实参 \
     * --------------------------------------
     *       ↓    ↓
     */
    SUCCESS(0,"SUCCESS"),                                       //成功
    ERROR(1,"ERROR"),                                           //错误
    NEED_LOGIN(10,"NEED_LOGIN"),                                //需要登录
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");                     //参数错误
    /**                                 ↑
     *                       枚举方法的结尾要以 ; 结尾
     */

    private final int code;
    private final String desc;
    /**
     *枚举构造器 =====>作用
     * 1.使枚举方法(eg:SUCCESS(0,"SUCCESS"),)可用
     * 2.为常量code、desc 赋值
     * 3.传入的实参皆为枚举方法中的参数
     */
    ResponseCode(int code,String desc){
        this.code = code;
        this.desc = desc;
    }
    public int getCode(){
        return this.code;
    }
    public String getDesc(){
        return this.desc;
    }

    /**
     *  使用技巧:   enumName.finalMethodName.getMethod
     *     eg:     ResponseCode.SUCCESS.getCode()
     *   得到      0
     *
     */

}
