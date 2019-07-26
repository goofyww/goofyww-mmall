package com.oecoo.gf.dao;

import com.oecoo.gf.pojo.Category;

import java.util.List;

public interface CategoryMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    int checkByCategoryName(String categoryName);

    List<Category> selectCategoryChildByCategoryId(Integer categoryId);

    Category selectByCategoryId(Integer categoryId);

    int checkByCategoryId(Integer categoryId);

    int updateCategoryNameByPrimaryKey(Category record);

    int updateCategoryStatusByPrimaryKey(Category category);

    List<Category> selectPaterCategory();

}