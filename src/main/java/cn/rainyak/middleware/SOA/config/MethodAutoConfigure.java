package cn.rainyak.middleware.SOA.config;

import cn.rainyak.middleware.SOA.aop.DoMethodExtPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MethodAutoConfigure {
    @Bean
    @ConditionalOnMissingBean
    public DoMethodExtPoint doMethodExtPoint() {
        return new DoMethodExtPoint();
    }
}
