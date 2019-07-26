package com.oecoo.gf.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 购物车中 商品的一个抽象对象
 * Created by gf on 2018/5/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartProductVo {

    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer quantity;                       //购物车中此商品的的数量

    private String productName;

    private String productSubtitle;

    private String productMainImage;

    private BigDecimal priductPrice;                //BigDecimal 用来解决商业运算浮点丢失问题

    private Integer productStatus;                  //商品状态 是否上下架

    private BigDecimal priductTotalPrice;           //购物车中商品总价

    private Integer productStock;                   //商品的库存 数量

    private Integer productChecked;                 // 商品是否勾选

    private String limitQuantity;                   //限制数量的一个返回结果         购物车中商品数量 !> 商品库存数量

}
