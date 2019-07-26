package com.oecoo.gf.service;

import com.github.pagehelper.PageInfo;
import com.oecoo.gf.common.ServerResponse;
import com.oecoo.gf.pojo.Product;
import com.oecoo.gf.vo.ProductDetailVo;

/**
 * Created by gf on 2018/4/29.
 */
public interface IProductService {

    ServerResponse<String> saveOrUpdateProduct(Product product);

    ServerResponse<String> setProductStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> findProduct(Integer productId, String productName, Integer pageNum, Integer pageSize);

    ServerResponse<ProductDetailVo> getDetailProduct(Integer productId);

    ServerResponse<PageInfo> getProductKeyword(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);


}
