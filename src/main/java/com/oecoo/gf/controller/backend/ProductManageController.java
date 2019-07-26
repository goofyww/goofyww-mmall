package com.oecoo.gf.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.oecoo.gf.common.Const;
import com.oecoo.gf.common.ResponseCode;
import com.oecoo.gf.common.ServerResponse;
import com.oecoo.gf.pojo.Product;
import com.oecoo.gf.pojo.User;
import com.oecoo.gf.service.IFileService;
import com.oecoo.gf.service.IProductService;
import com.oecoo.gf.service.IUserService;
import com.oecoo.gf.util.PropertiesUtil;
import com.oecoo.gf.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by gf on 2018/4/29.
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {

    private static Logger logger = LoggerFactory.getLogger(ProductManageController.class);

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    @RequestMapping(value = "save_or_update.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请先登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员再执行操作
            return iProductService.saveOrUpdateProduct(product);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
    @RequestMapping(value = "set_sale_status.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setProductStatus(HttpSession session,Integer productId,Integer status){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请先登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员再执行上下架操作
            return iProductService.setProductStatus(productId,status);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
    @RequestMapping(value = "detail.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailVo> productDetail(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请先登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员再执行操作
            return iProductService.manageProductDetail(productId);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    @RequestMapping(value = "list.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> productGetList(HttpSession session,
                                                   //设置分页默认属性值
                                                   @RequestParam(value = "pageNum",defaultValue ="1") int pageNum,
                                                   @RequestParam(value = "pageSize",defaultValue ="10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请先登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员再执行操作
            return iProductService.getProductList(pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    /**
     * 商品搜索
     * @param session
     * @param productName
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "search.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> productSearch(HttpSession session,
                                                  Integer productId,
                                                  String productName,
                                                   //设置分页默认属性值
                                                   @RequestParam(value = "pageNum",defaultValue ="1") int pageNum,
                                                   @RequestParam(value = "pageSize",defaultValue ="10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请先登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //是管理员再执行操作        pageHelper在Server层进行，因此这里一定将pageNum 、pageSize传递过去
            return iProductService.findProduct(productId,productName,pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    @RequestMapping(value = "upload.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(HttpSession session,
                                 HttpServletRequest request,
                                 @RequestParam(value = "upload_file",required = false) MultipartFile file){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请先登陆");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //获取到上下文路径下的upload文件夹的路径
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            Map<String,Object> map = Maps.newHashMap();
            map.put("fileName",targetFileName);
            map.put("url",url);
            return ServerResponse.createBySuccessData(map);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    /**
     * 富文本上传
     * @param session   判断是否为管理员
     * @param request   获取上下文路径
     * @param response  设置ResponseHeader()指定格式
     * @param file      获得用户上传的文件
     * @return
     */
    @RequestMapping(value = "richtext_img_upload.do",method = RequestMethod.POST)
    @ResponseBody
    public Map richtextImgUpload(HttpSession session,
                                 HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam(value = "upload_file",required = false) MultipartFile file){
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            resultMap.put("success",false);
            resultMap.put("msg","用户未登录,请先登陆");
            return resultMap;
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //获取到上下文路径下的upload文件夹的路径
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            /**
             * 富文本中对于返回值有自己的要求,这里使用是simditor所以按照simditor的要求进行返回
             *  {
             *  "success": true/false,
             *  "msg": "error message", # optional
             *  "file_path": "[real file path]"
             *  }
             */
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        }else{
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作，需要管理员权限");
            return resultMap;
        }
    }

}
