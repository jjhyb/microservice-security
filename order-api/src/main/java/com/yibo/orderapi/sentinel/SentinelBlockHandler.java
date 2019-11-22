package com.yibo.orderapi.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.yibo.orderapi.domain.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: huangyibo
 * @Date: 2019/11/20 17:23
 * @Description:
 *
 * 服务发生流控或将级的时候会进入到此方法
 */

@Slf4j
public class SentinelBlockHandler {

    public static OrderInfo doOnBlock(@RequestBody OrderInfo order, @AuthenticationPrincipal String username, BlockException exception){
        log.info("blocked by ：blockException={}",exception.getClass().getSimpleName());

        return order;
    }
}
