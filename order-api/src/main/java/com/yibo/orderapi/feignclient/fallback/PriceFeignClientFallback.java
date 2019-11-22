package com.yibo.orderapi.feignclient.fallback;

import com.yibo.orderapi.domain.PriceInfo;
import com.yibo.orderapi.feignclient.PriceFeignClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author: huangyibo
 * @Date: 2019/11/20 17:42
 * @Description:
 *
 * 发生限流降级时，自定义处理逻辑
 *
 * 一旦PriceFeignClient中远程调用的getPrice()方法被流控了或发生异常了，就会进入此方法
 * 这就相当于一个兜底的行为，保证了服务的可用
 */

@Component
public class PriceFeignClientFallback implements PriceFeignClient {

    @Override
    public PriceInfo getPrice(Integer id) {
        PriceInfo priceInfo = new PriceInfo();
        priceInfo.setId(id);
        priceInfo.setPrice(new BigDecimal(id));
        return priceInfo;
    }
}
