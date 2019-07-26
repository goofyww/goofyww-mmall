package com.oecoo.gf.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车 VO
 * Created by gf on 2018/5/2.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartVo {

    private List<CartProductVo> cartProductVos; //购物车中的商品集合

    private BigDecimal cartTotalPrice;          //购物车中商品总价

    private Boolean allChecked;                 //购物车是否已经全部勾选

    private String imageHost;

}
