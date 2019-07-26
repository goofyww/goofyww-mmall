package com.oecoo.gf.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by gf on 2018/5/8.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingVo {

    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;

}
