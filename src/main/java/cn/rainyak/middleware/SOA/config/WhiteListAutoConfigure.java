package cn.rainyak.middleware.SOA.config;
import cn.rainyak.middleware.SOA.aop.DoJoinPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//白名单配置获取 WhiteListAutoConfigure，配置下是对 SpringBoot yml 文件的使用，这样就可以把配置到 yml 文件的中白名单信息读取到中间件中。
@Configuration//声明这是一个配置类
@ConditionalOnClass(WhiteListProperties.class)//当 WhiteListProperties 类存在时，才会加载这个配置类
@EnableConfigurationProperties(WhiteListProperties.class)//开启配置属性功能
public class WhiteListAutoConfigure {
    @Bean("whiteListConfig")
    @ConditionalOnMissingBean
    public String whiteListConfig(WhiteListProperties properties) {
        return properties.getUsers();
    }
    @Bean
    @ConditionalOnMissingBean
    public DoJoinPoint doJoinPoint() {
        return new DoJoinPoint();
    }
}
