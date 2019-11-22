package com.yibo.priceapi.controller;

import com.yibo.priceapi.domain.PriceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author: huangyibo
 * @Date: 2019/11/10 13:35
 * @Description:
 */

@RestController
@RequestMapping("/prices")
@Slf4j
public class PriceController {

    @GetMapping("/{id}")
    public PriceInfo getPrice(@PathVariable("id") Integer id,@AuthenticationPrincipal String username){
        log.info("商品id：productId={}，用户username={}",id,username);
        PriceInfo priceInfo = new PriceInfo();
        priceInfo.setId(id);
        priceInfo.setPrice(new BigDecimal(100));
        return priceInfo;
    }
}
