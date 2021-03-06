package com.oecoo.gf.dao;

import com.oecoo.gf.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    int checkProductByName(String productName);

    int checkProductById(Integer productId);

    List<Product> selectProductList();

    List<Product> selectByProductName(@Param("productId") Integer productId, @Param("productName") String productName);

    List<Product> selectByNameAndCategoryIds(@Param("keyword") String keyword, @Param("categoryIds") List<Integer> categoryIds);

    //这里一定要用Integer，因为int无法为null，考虑到很多商品已经删除的情况。
    Integer selectStockByProductId(Integer id);

}