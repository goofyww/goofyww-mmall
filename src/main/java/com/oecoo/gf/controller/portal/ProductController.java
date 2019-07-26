package com.oecoo.gf.controller.portal;

import com.github.pagehelper.PageInfo;
import com.oecoo.gf.common.ServerResponse;
import com.oecoo.gf.service.IProductService;
import com.oecoo.gf.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by gf on 2018/5/1.
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;
    //商品详情
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detailProduct(Integer productId){
        return  iProductService.getDetailProduct(productId);
    }
    //商品列表
    @RequestMapping(value = "list.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listProduct(@RequestParam(value = "keyword",required = false) String keyword,
                                                @RequestParam(value = "categoryId",required = false) Integer categoryId,
                                                @RequestParam(value = "orderBy",defaultValue = "") String orderBy,
                                                @RequestParam(value = "pageNum",defaultValue ="1") int pageNum,
                                                @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        return  iProductService.getProductKeyword(keyword,categoryId,pageNum,pageSize,orderBy);
    }

}
