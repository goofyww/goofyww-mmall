package com.oecoo.gf.dao;

import com.oecoo.gf.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByUserIdAndProductId(@Param(value = "userId") Integer userId, @Param(value = "productId") Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdProductIds(@Param(value = "userId") Integer userId, @Param(value = "productIdList") List<String> productIds);

    int checkOrUncheckedProduct(@Param(value = "userId") Integer userId, @Param(value = "productId") Integer productId, @Param(value = "checked") Integer checked);

    int selectCartProductCountByUserId(Integer userId);

    List<Cart> selectCheckedCartByUserId(Integer userId);
}