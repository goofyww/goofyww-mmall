package com.oecoo.gf.vo;

import java.math.BigDecimal;

/**
 * 购物车中 商品的一个抽象对象
 * Created by gf on 2018/5/2.
 */
public class CartProductVo {

    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;//购物车中此商品的的数量
    private String productName;
    private String productSubtitle;
    private String productMainImage;
    private BigDecimal priductPrice;//BigDecimal 用来解决商业运算浮点丢失问题
    private Integer productStatus;//商品状态 是否上下架
    private BigDecimal priductTotalPrice;//购物车中商品总价
    private Integer productStock;//商品的库存 数量
    private Integer productChecked;// 商品是否勾选

    private String limitQuantity;//限制数量的一个返回结果         购物车中商品数量 !> 商品库存数量

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSubtitle() {
        return productSubtitle;
    }

    public void setProductSubtitle(String productSubtitle) {
        this.productSubtitle = productSubtitle;
    }

    public String getProductMainImage() {
        return productMainImage;
    }

    public void setProductMainImage(String productMainImage) {
        this.productMainImage = productMainImage;
    }

    public BigDecimal getPriductPrice() {
        return priductPrice;
    }

    public void setPriductPrice(BigDecimal priductPrice) {
        this.priductPrice = priductPrice;
    }

    public Integer getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
    }

    public BigDecimal getPriductTotalPrice() {
        return priductTotalPrice;
    }

    public void setPriductTotalPrice(BigDecimal priductTotalPrice) {
        this.priductTotalPrice = priductTotalPrice;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public void setProductStock(Integer productStock) {
        this.productStock = productStock;
    }

    public Integer getProductChecked() {
        return productChecked;
    }

    public void setProductChecked(Integer productChecked) {
        this.productChecked = productChecked;
    }

    public String getLimitQuantity() {
        return limitQuantity;
    }

    public void setLimitQuantity(String limitQuantity) {
        this.limitQuantity = limitQuantity;
    }
}
