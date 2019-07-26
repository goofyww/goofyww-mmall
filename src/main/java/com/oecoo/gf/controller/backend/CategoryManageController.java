package com.oecoo.gf.controller.backend;

import com.oecoo.gf.common.ServerResponse;
import com.oecoo.gf.pojo.Category;
import com.oecoo.gf.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by gf on 2018/4/28.
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 增加品类
     *
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping(value = "add_category.do")
    @ResponseBody
    public ServerResponse<String> addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        // 登录校验和鉴权交由拦截器完成
        return iCategoryService.addCategory(categoryName, parentId);
    }

    /**
     * 修改品类名字
     *
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "set_category_name.do")
    @ResponseBody
    public ServerResponse<String> setCategoryName(Integer categoryId, String categoryName) {
        // 登录校验和鉴权交由拦截器完成
        return iCategoryService.updateCategoryName(categoryId, categoryName);
    }

    /**
     * 删除品类
     *
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "remove_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse removeCategoryById(Integer categoryId) {
        // 登录校验和鉴权交由拦截器完成
        return iCategoryService.removeCategoryById(categoryId);
    }

    /**
     * 修改品类上下架状态
     *
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "set_category_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> setCategoryStatus(Integer categoryId, Integer status) {
        // 登录校验和鉴权交由拦截器完成
        return iCategoryService.updateCategoryStatus(categoryId, status);
    }

    /**
     * 获取同级节点
     *
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "get_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Category>> getChildParallelCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        // 登录校验和鉴权交由拦截器完成
        return iCategoryService.getChildParallelCategory(categoryId);
    }

    /**
     * 获取递归子节点ID
     *
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "get_deep_category.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        // 登录校验和鉴权交由拦截器完成
        //查询当前节点的id和递归子节点的id
        //            0->10000->100000
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }


}
