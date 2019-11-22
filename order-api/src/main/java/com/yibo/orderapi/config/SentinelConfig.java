package com.yibo.orderapi.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: huangyibo
 * @Date: 2019/11/16 14:20
 * @Description: ContextRefreshedEvent事件在Spring容器成功启动好并且将Bean都组装好以后会触发
 */

@Component
public class SentinelConfig implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 系统启动好，Bean都组装好以后会触发
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //服务熔断的规则
        FlowRule rule = new FlowRule();
        rule.setResource("createOrder");//设置流控规则的资源
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);//设置流控规则
        rule.setCount(10);//每秒只能有10个请求通过
        List<FlowRule> rules = new ArrayList<>();
        rules.add(rule);
        FlowRuleManager.loadRules(rules);

        //Sentinel以访问资源的平均响应时间RT作为降级策略
        DegradeRule degradeRule = new DegradeRule();
        degradeRule.setResource("createOrder");//设置降级规则的资源
        // 设置降级规则RT, 平均响应时间
        degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
        //响应时间超过10毫秒就需要计数，即有一个服务超时了
        degradeRule.setCount(10);
        //熔断器从打开到半打开状态的时间窗口(秒)
        degradeRule.setTimeWindow(10);
        List<DegradeRule> degradeRules = new ArrayList<>();
        degradeRules.add(degradeRule);
        DegradeRuleManager.loadRules(degradeRules);
    }
}
