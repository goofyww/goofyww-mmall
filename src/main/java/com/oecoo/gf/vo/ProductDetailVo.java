package com.oecoo.gf.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品详情VO
 * Created by gf on 2018/4/29.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailVo {

    private Integer  id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private String subImages;

    private String detail;

    private BigDecimal price;

    private Integer stock;

    private Integer status;

    private String createTime;

    private String updateTime;

    private String imageHost;

    private Integer parentCategoryId;

}
