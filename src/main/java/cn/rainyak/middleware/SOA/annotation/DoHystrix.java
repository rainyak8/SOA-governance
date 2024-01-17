package cn.rainyak.middleware.SOA.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)//该注解可以应用于方法上
public @interface DoHystrix {
    String returnJson() default "";//失败结果 JSON
    int timeoutValue() default 0;//超时熔断
}
