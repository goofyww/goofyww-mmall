package com.oecoo.gf.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * Created by gf on 2018/4/26.
 */
/**
 * 此对象作为服务响应对象 用来包装返回的类型以及状态信息
 * 在无法确定返回对象是何类型的前提上，因此定义为泛型<T>
 * @param <T>
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//保证序列化json的时候,如果是null的对象,key也会消失
public class ServerResponse<T> implements Serializable {

    private int status;     //状态码
    private String msg;     //文字描述
    private T data;         //对象数据

    private ServerResponse(int status){
        this.status = status;
    }
    private ServerResponse(int status,String msg){
        this.status = status;
        this.msg = msg;
    }
    private ServerResponse(int status,String msg,T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    private ServerResponse(int status,T data){
        this.status = status;
        this.data = data;
    }
    //判断这个响应是否是一个成功的响应 status==0 true 否则 false
    @JsonIgnore//@JsonIgnore 使这个方法的返回值不在Json序列化结果当中
    public boolean isSuccess(){
        return this.status==ResponseCode.SUCCESS.getCode();
    }
    public int getStatus(){
        return this.status;
    }
    public String getMsg(){
        return this.msg;
    }
    public T getData(){
        return this.data;
    }

    /**
     * 以下 ④个静态方法用来返回 成功状态 依照响应需求 调用相关方法即可
     *      返回 状态码
     * @param <T>
     * @return
     */

    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse(ResponseCode.SUCCESS.getCode());
    }

    /**
     *返回 状态码 说明信息
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse(ResponseCode.SUCCESS.getCode(),msg);
    }

    /**
     *  返回 状态码 对象
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createBySuccessData(T data){
        return new ServerResponse(ResponseCode.SUCCESS.getCode(),data);
    }

    /**
     * 返回 状态码 说明信息 对象
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    public static  <T> ServerResponse<T> createBySuccessMsgData(String msg,T data){
        return new ServerResponse(ResponseCode.SUCCESS.getCode(),msg,data);
    }


    /**
     * 以下 ②个静态方法用来返回 失败状态 依照响应需求 调用相关方法即可
     * 返回 状态码 和 描述（此描述为枚举内定义）
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }


    /**
     *  返回 状态码 和 说明信息
     * @param errorMsg
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createByErrorMessage(String errorMsg){
        return new ServerResponse(ResponseCode.ERROR.getCode(),errorMsg);
    }


    /**
     *  NOT_SUCCESS  SUCCESS
     *  result 自定义 状态 和描述信息
     *  if param error
     * @param errorCode
     * @param errorMessage
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new ServerResponse(errorCode,errorMessage);
    }


}
