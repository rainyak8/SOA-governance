package cn.rainyak.middleware.SOA.config;

import cn.rainyak.middleware.SOA.aop.DoHystrixPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HystrixAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    public DoHystrixPoint doHystrixPoint() {
        return new DoHystrixPoint();
    }
}
