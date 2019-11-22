package com.yibo.orderapi.feignclient;

import com.yibo.orderapi.domain.PriceInfo;
import com.yibo.orderapi.feignclient.fallbackfactory.PriceFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: huangyibo
 * @Date: 2019/11/17 22:48
 * @Description:
 */

//@FeignClient(value="priceServer",fallback = PriceFeignClientFallback.class,configuration = TokenFeignClientInterceptor.class)
@FeignClient(value="priceServer",fallbackFactory = PriceFeignClientFallbackFactory.class ,configuration = TokenFeignClientInterceptor.class)
public interface PriceFeignClient {

    @GetMapping(value = "/prices/{id}")
    PriceInfo getPrice(@PathVariable("id") Integer id);
}
