package com.oecoo.gf.controller;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.oecoo.gf.common.ServerResponse;
import com.oecoo.gf.pojo.Category;
import com.oecoo.gf.service.ICategoryService;
import com.oecoo.gf.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Created by gf on 2018/5/21.
 */
@Controller
public class IndexController {

    @Autowired
    private ICategoryService  iCategoryService;

    //跳转主页
    @RequestMapping(value = { "/", "/index.do" })
    public String index() {
        return "/index";
    }
    //普通用户注册
    @RequestMapping(value = {"/","/register.do"})
    public String rigester(){
        return "/user-register";
    }

    @RequestMapping(value = "/category/list.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<CategoryVo>> getCategoryList(){
        return iCategoryService.getCategoryList();
    }


}
