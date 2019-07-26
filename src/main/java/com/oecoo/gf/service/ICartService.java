package com.oecoo.gf.service;

import com.oecoo.gf.common.ServerResponse;
import com.oecoo.gf.vo.CartVo;

/**
 * Created by gf on 2018/5/2.
 */
public interface ICartService{

    ServerResponse<CartVo> addCart(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> removeCartProduct(Integer userId,String productIds);

    ServerResponse<CartVo> updateCart(Integer userId,Integer productId,Integer count);

    ServerResponse<CartVo> list(Integer userId);

    ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer productId,Integer checked);

    ServerResponse<Integer> getCartProductCount(Integer userId);

}
