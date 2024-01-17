package cn.rainyak.middleware.SOA.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
@Retention(RetentionPolicy.RUNTIME) 标记注解在 JVM 运行时可见
@Target(ElementType.METHOD) 标记注解的作用域在方法层面 ElementType.METHOD
permitsPerSecond，方法限流许可量，超过这个量就会返回 returnJson 中的配置信息
returnJson，调用限流时返回的结果
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DoRateLimiter {
    double permitsPerSecond() default 0D;   // 限流许可量
    String returnJson() default "";         // 失败结果 JSON
}
