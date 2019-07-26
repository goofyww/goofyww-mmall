package com.oecoo.gf.service;

import com.oecoo.gf.common.ServerResponse;
import com.oecoo.gf.pojo.Category;
import com.oecoo.gf.vo.CategoryVo;

import java.util.List;

/**
 * Created by gf on 2018/4/29.
 */
public interface ICategoryService {

    ServerResponse<String> addCategory(String categoryName, Integer parentId);

    ServerResponse<String> updateCategoryName(Integer categoryId,String categoryName);

    ServerResponse<List<Category>> getChildParallelCategory(Integer categoryId);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);

    ServerResponse<String> updateCategoryStatus(Integer categoryId,Integer status);

    ServerResponse removeCategoryById(Integer categoryId);

    ServerResponse<List<CategoryVo>> getCategoryList();

}
