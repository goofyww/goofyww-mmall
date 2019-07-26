package com.oecoo.gf.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.oecoo.gf.common.ResponseCode;
import com.oecoo.gf.common.ServerResponse;
import com.oecoo.gf.dao.ShippingMapper;
import com.oecoo.gf.pojo.Shipping;
import com.oecoo.gf.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by gf on 2018/5/3.
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    /**
     *  新增地址
     * @param userId
     * @param shipping
     * @return
     */
    public ServerResponse addShipping(Integer userId,Shipping shipping){
        if(shipping == null || userId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        shipping.setUserId(userId);//防止越权
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount > 0){
            Map result = Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            //使用map 来得到用户在插入时生成的 id 主键 ，返回给前台
            return ServerResponse.createBySuccessMsgData("新建地址成功",result);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    /**
     * 更新
     * @param userId
     * @param shipping
     * @return
     */
    public ServerResponse updateShipping(Integer userId,Shipping shipping){
        shipping.setUserId(userId);
        int resultCount = shippingMapper.updateByShipping(shipping);
        if(resultCount > 0){
            return ServerResponse.createBySuccessMessage("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    /**
     * 删除
     * @param userId
     * @param shippingId
     * @return
     */
    public ServerResponse deleteShipping(Integer userId,Integer shippingId){
        int isSuccess = shippingMapper.deleteByUserIdShippingId(userId,shippingId);
        if(isSuccess > 0 ){
            return ServerResponse.createBySuccessMessage("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    public ServerResponse<Shipping> select(Integer userId, Integer shippingId){
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId,shippingId);
        if(shipping == null){
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponse.createBySuccessMsgData("更新地址成功",shipping);
    }


    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccessData(pageInfo);
    }


}
