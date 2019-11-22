package com.yibo.orderapi.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.yibo.orderapi.domain.OrderInfo;
import com.yibo.orderapi.domain.PriceInfo;
import com.yibo.orderapi.feignclient.PriceFeignClient;
import com.yibo.orderapi.sentinel.SentinelBlockHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * @author: huangyibo
 * @Date: 2019/11/10 13:30
 * @Description:
 */

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    /**
     * OAuth2RestTemplate 会默认从当前请求的上下文中拿到token令牌，将token放在请求头中，然后在把请求发出去
     */
    /*@Autowired
    private OAuth2RestTemplate restTemplate;*/

    @Autowired
    private PriceFeignClient priceFeignClient;


    @PostMapping
//    @PreAuthorize("#oauth2.hasScope('write')")//这是通过Scope来控制应用有什么样的权限的，不能控制人
    @PreAuthorize("hasRole('ROLE_ADMIN')")//控制用户拥有什么权限
    @SentinelResource(value = "createOrder",blockHandler = "doOnBlock",blockHandlerClass = SentinelBlockHandler.class)
    public OrderInfo create(@RequestBody OrderInfo order, @AuthenticationPrincipal String username){
        log.info("用户名为：username={}",username);
        PriceInfo priceInfo = priceFeignClient.getPrice(order.getProductId());
        log.info("商品价格为,priceInfo={}",priceInfo);
        return order;
    }

    @GetMapping("/{id}")
    @SentinelResource("getOrder")
    public OrderInfo getOrder(@PathVariable("id")Integer id,@AuthenticationPrincipal String username){
        log.info("order id={}",id);
        log.info("用户名为：username={}",username);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(id);
        orderInfo.setProductId(id * 5);
        return orderInfo;
    }
}
