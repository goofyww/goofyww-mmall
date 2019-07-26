package com.oecoo.gf.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品列表VO
 * Created by gf on 2018/4/30.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListVo {

    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private BigDecimal price;

    private Integer status;

    private String imageHost;

}
