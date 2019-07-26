package com.oecoo.gf.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.oecoo.gf.common.ServerResponse;
import com.oecoo.gf.pojo.Product;
import com.oecoo.gf.service.IFileService;
import com.oecoo.gf.service.IProductService;
import com.oecoo.gf.util.PropertiesUtil;
import com.oecoo.gf.vo.ProductDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by gf on 2018/4/29.
 */
@Controller
@RequestMapping("/manage/product/")
@Slf4j
public class ProductManageController {

    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    @RequestMapping(value = "save_or_update.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(Product product) {
        return iProductService.saveOrUpdateProduct(product);
    }

    @RequestMapping(value = "set_sale_status.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setProductStatus(Integer productId, Integer status) {
        return iProductService.setProductStatus(productId, status);
    }

    @RequestMapping(value = "detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailVo> productDetail(Integer productId) {
        return iProductService.manageProductDetail(productId);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> productGetList(//设置分页默认属性值
                                                   @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return iProductService.getProductList(pageNum, pageSize);
    }

    /**
     * 商品搜索
     *
     * @param productName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "search.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> productSearch(Integer productId,
                                                  String productName,
                                                  //设置分页默认属性值
                                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return iProductService.findProduct(productId, productName, pageNum, pageSize);
    }

    @RequestMapping(value = "upload.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(HttpServletRequest request,
                                 @RequestParam(value = "upload_file", required = false) MultipartFile file) {
        //获取到上下文路径下的upload文件夹的路径
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        Map<String, Object> map = Maps.newHashMap();
        map.put("fileName", targetFileName);
        map.put("url", url);
        return ServerResponse.createBySuccessData(map);
    }

    /**
     * 富文本上传
     *
     * @param request  获取上下文路径
     * @param response 设置ResponseHeader()指定格式
     * @param file     获得用户上传的文件
     * @return
     */
    @RequestMapping(value = "richtext_img_upload.do", method = RequestMethod.POST)
    @ResponseBody
    public Map richtextImgUpload(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam(value = "upload_file", required = false) MultipartFile file) {
        /**
         * 富文本中对于返回值有自己的要求,这里使用是simditor所以按照simditor的要求进行返回
         *  {
         *  "success": true/false,
         *  "msg": "error message", # optional
         *  "file_path": "[real file path]"
         *  }
         */
        Map resultMap = Maps.newHashMap();
        //获取到上下文路径下的upload文件夹的路径
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        if (StringUtils.isBlank(targetFileName)) {
            resultMap.put("success", false);
            resultMap.put("msg", "上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
        resultMap.put("success", true);
        resultMap.put("msg", "上传成功");
        resultMap.put("file_path", url);
        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
        return resultMap;
    }

}
