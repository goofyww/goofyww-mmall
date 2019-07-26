package com.oecoo.gf.service.impl;

import com.google.common.collect.Lists;
import com.oecoo.gf.common.Const;
import com.oecoo.gf.common.ResponseCode;
import com.oecoo.gf.common.ServerResponse;
import com.oecoo.gf.dao.CartMapper;
import com.oecoo.gf.dao.ProductMapper;
import com.oecoo.gf.pojo.Cart;
import com.oecoo.gf.pojo.Product;
import com.oecoo.gf.service.ICartService;
import com.oecoo.gf.util.BigDecimalUtil;
import com.oecoo.gf.util.PropertiesUtil;
import com.oecoo.gf.vo.CartProductVo;
import com.oecoo.gf.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by gf on 2018/5/2.
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * 新增购物车商品
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    public ServerResponse<CartVo> addCart(Integer userId, Integer productId, Integer count){
        if(productId == null || count == null ){
            //参数错误
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        //检查商品是否存在
        int resultCount = productMapper.checkProductById(productId);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("----该商品不存在----");
        }

        //检查购物车中是否有添加该商品
        Cart cart = cartMapper.selectByUserIdAndProductId(userId,productId);
        if(cart == null ){
            //用户的购物车无此商品
            Cart cartItem = new Cart();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);//
            cartItem.setChecked(Const.Cart.UN_CHECKED);//新加入购物车的商品默认是未勾选状态
            cartItem.setQuantity(count);//添加数量
            int result = cartMapper.insert(cartItem);
            if(result == 0){
                return ServerResponse.createByErrorMessage("添加购物车失败");
            }
        }else{
            //用户购物车有此商品
            count = cart.getQuantity()+1;
            cart.setQuantity(count);
            int result = cartMapper.updateByPrimaryKeySelective(cart);
            if(result == 0){
                return ServerResponse.createByErrorMessage("添加购物车失败");
            }
        }
        return this.list(userId);
    }

    /**
     * 更新购物车
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    public ServerResponse<CartVo> updateCart(Integer userId,Integer productId,Integer count){
        if(productId == null || count == null ){
            //参数错误
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        //检查商品是否存在
        int resultCount = productMapper.checkProductById(productId);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("----该商品不存在----");
        }
        //检查购物车中 1.该用户2.所属商品 是否存在
        Cart cart = cartMapper.selectByUserIdAndProductId(userId,productId);
        if(cart == null){
            return ServerResponse.createByErrorMessage("----该商品不存在用户购物车中----");
        }else{
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKey(cart);
        }
        return this.list(userId);
    }
    /**
     * 删除购物车中的 商品
     * @param userId
     * @param productIds       商品id 集合
     * @return
     */
    public ServerResponse<CartVo> removeCartProduct(Integer userId,String productIds){
        String[] productId = productIds.split(",");
        List<String> productList = Lists.newArrayList();
        for(int i= 0;i<productId.length;i++){
            productList.add(productId[i]);
        }
        if(CollectionUtils.isEmpty(productList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdProductIds(userId,productList);
        return this.list(userId);
    }

    /**
     * 遍历
     * @param userId
     * @return
     */
    public ServerResponse<CartVo> list(Integer userId){
        CartVo cartVo = this.getCartVolimit(userId);
        return ServerResponse.createBySuccessData(cartVo);
    }

    /**
     * 更新 :单选 全选 反选
     * @param userId
     * @param productId
     * @param checked
     * @return
     */
    public ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer productId,Integer checked){
        cartMapper.checkOrUncheckedProduct(userId,productId,checked);
        return this.list(userId);
    }

    /**
     * 获取购物车商品数量
     * @param userId
     * @return
     */
    public ServerResponse<Integer> getCartProductCount(Integer userId){
        if(userId == null) {
            return  ServerResponse.createBySuccessData(0);
        }
        int count = cartMapper.selectCartProductCountByUserId(userId);
        return ServerResponse.createBySuccessData(count);
    }

    /**
     * 查找 指定用户id 的购物车 （其中包含所选的商品） ====> 转为 cartVo
     * @param userId
     * @return
     */
    private CartVo getCartVolimit(Integer userId){

        CartVo cartVo = new CartVo();//用来接值
        List<Cart> carts = cartMapper.selectCartByUserId(userId);   //查找userId相对应的Cart集合
        List<CartProductVo> cartProductVoList = Lists.newArrayList(); //用来接值 ，最后放入CartVo中

        BigDecimal cartTotalPrice = new BigDecimal("0");// 构造器为字符型 才可以处理商业运算 浮点丢失问题
        if(CollectionUtils.isNotEmpty(carts)){ //返回集合不为空时
            for(Cart cartItem : carts){//遍历这个集合
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setUserId(cartItem.getUserId());
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());//通过商品id获取商品详情
                if(product != null){

                    //商品不为空 ，开始组合cartProductVo
                    cartProductVo.setProductName(product.getName());  //商品名
                    cartProductVo.setPriductPrice(product.getPrice()); //商品价格
                    cartProductVo.setProductStatus(product.getStatus()); //商品上下架状态
                    cartProductVo.setProductSubtitle(product.getSubtitle()); //商品标题
                    cartProductVo.setProductMainImage(product.getMainImage()); //商品主图
                    cartProductVo.setProductStock(product.getStock());//商品库存

                    int buyLimitCount = 0;

                    if(product.getStock() >= cartItem.getQuantity()){
                        //库存充足  --->使用当前库存
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else {
                        //库存大于商品剩余数量   --->使用商品数量
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //--------这个对象只是为了更新数据库中有效库存--------------
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        //开始更新库存
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }

                    cartProductVo.setQuantity(buyLimitCount);//set 处理后的库存
                    //计算总价    商品单价*购物车数量
                    cartProductVo.setPriductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVo.getQuantity().doubleValue()));
                    cartProductVo.setProductChecked(cartItem.getChecked());// 购物车中商品是否被勾选
                }
                if (cartItem.getChecked() == Const.Cart.CHECKED){
                    //如果已勾选，增加到整个的购物车总价中              购物车总价    +    当前商品价钱
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getPriductPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
        cartVo.setCartProductVos(cartProductVoList);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVo;
    }

    /**
     * 获得全部该 此用户下的所有的购物车商品是否全部都已经勾选
     * @param userId
     * @return
     */
    private boolean getAllCheckedStatus(Integer userId){
        if(userId == null){
            return false;
        }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
        // 找到了返回 1   1==0:false
        // 没找到返回 0   0==0:true
    }

}
