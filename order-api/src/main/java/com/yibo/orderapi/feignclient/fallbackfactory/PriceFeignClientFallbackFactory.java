package com.yibo.orderapi.feignclient.fallbackfactory;

import com.yibo.orderapi.domain.PriceInfo;
import com.yibo.orderapi.feignclient.PriceFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author: huangyibo
 * @Date: 2019/11/20 17:53
 * @Description:
 *
 * 发生限流降级时，自定义处理逻辑
 * 一旦PriceFeignClient中远程调用的getPrice()方法被流控了或发生异常了，就会进入此方法
 * 这就相当于一个兜底的行为，保证了服务的可用
 * 相比于FeignClient中的fallback属性而言，fallbackFactory属性在fallback的基础上可以拿到异常信息
 */

@Component
@Slf4j
public class PriceFeignClientFallbackFactory implements FallbackFactory<PriceFeignClient> {

    @Override
    public PriceFeignClient create(Throwable throwable) {
        return new PriceFeignClient() {
            @Override
            public PriceInfo getPrice(Integer id) {
                log.error("远程调用被限流或降级了，throwable={}",throwable);
                PriceInfo priceInfo = new PriceInfo();
                priceInfo.setId(id);
                priceInfo.setPrice(new BigDecimal(id));
                return priceInfo;
            }
        };
    }
}
