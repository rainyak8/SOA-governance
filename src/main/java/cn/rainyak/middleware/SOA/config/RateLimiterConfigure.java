package cn.rainyak.middleware.SOA.config;

import cn.rainyak.middleware.SOA.aop.DoHystrixPoint;
import cn.rainyak.middleware.SOA.aop.DoRateLimiterPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfigure {
    @Bean
    @ConditionalOnMissingBean
    public DoRateLimiterPoint doRateLimiterPoint() {
        return new DoRateLimiterPoint();
    }
}
