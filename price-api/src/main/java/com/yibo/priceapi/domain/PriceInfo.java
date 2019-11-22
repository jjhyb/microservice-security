package com.yibo.priceapi.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: huangyibo
 * @Date: 2019/11/10 13:36
 * @Description:
 */

@Data
public class PriceInfo {

    private Integer id;

    private BigDecimal price;
}
