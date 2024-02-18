package cn.rainyak.middleware.SOA.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)//该注解可以应用于方法上
public @interface DoHystrix {
    int timeoutValue() default 0;//超时熔断
    String fallbackMethod() default "";//熔断后调用的方法
    Class<?> fallbackClass() default void.class;
}
