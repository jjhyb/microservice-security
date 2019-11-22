package com.yibo.orderapi.domain;

import lombok.Data;

/**
 * @author: huangyibo
 * @Date: 2019/11/10 13:31
 * @Description:
 */

@Data
public class OrderInfo {

    /**
     * 订单id
     */
    private Integer id;

    /**
     * 商品id
     */
    private Integer productId;


}
