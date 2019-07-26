package com.oecoo.gf.dao;

import com.oecoo.gf.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByUserIdAndOrderNo(@Param(value = "userId") Integer userId, @Param(value = "orderNo") Long orderNo);

    Order selectByOrderNo(Long orderNo);

    List<Order> selectByUserId(Integer userId);

    List<Order> selectAllOrder();

    //二期新增定时关单
    List<Order> selectOrderStatusByCreateTime(@Param("status") Integer status, @Param("date") String date);

    int closeOrderByOrderId(Integer id);

}