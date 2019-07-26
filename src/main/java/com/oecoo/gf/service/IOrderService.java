package com.oecoo.gf.service;

import com.github.pagehelper.PageInfo;
import com.oecoo.gf.common.ServerResponse;
import com.oecoo.gf.vo.OrderVo;

import java.util.Map;

/**
 * Created by gf on 2018/5/5.
 */
public interface IOrderService {

    //portal
    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse aliCallback(Map<String, String> params);

    ServerResponse<Boolean> queryOrderPayStatus(Integer userId, Long orderNo);

    ServerResponse createOrder(Integer userId, Integer shippingId);

    ServerResponse cancel(Integer userId, Long orderNo);

    ServerResponse getOrderCartProduct(Integer userId);

    ServerResponse<PageInfo> getOrderlist(Integer userId, int pageNum, int pageSize);

    ServerResponse getOrderDetail(Integer userId, Long orderNo);

    //backend
    ServerResponse<PageInfo> manageList(int pageNum, int pageSize);

    ServerResponse<OrderVo> manageDetail(Long orderNo);

    ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);

    ServerResponse<String> manageSendGoods(Long orderNo);

    //hour个小时以内未付款的订单，进行关闭
    void closeOrder(int hour);


}
