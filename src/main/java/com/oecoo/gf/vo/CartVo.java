package com.oecoo.gf.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车 VO
 * Created by gf on 2018/5/2.
 */
public class CartVo {

    private List<CartProductVo> cartProductVos;//购物车中的商品集合
    private BigDecimal cartTotalPrice;//购物车中商品总价
    private Boolean allChecked;//购物车是否已经全部勾选
    private String imageHost;

    public List<CartProductVo> getCartProductVos() {
        return cartProductVos;
    }

    public void setCartProductVos(List<CartProductVo> cartProductVos) {
        this.cartProductVos = cartProductVos;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
