package cn.rainyak.middleware.SOA.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
@Retention(RetentionPolicy.RUNTIME) 标记注解在 JVM 运行时可见
@Target(ElementType.METHOD) 标记注解的作用域在方法层面 ElementType.METHOD
method，用于配置用户的新增扩展方法，这里目前支持的单个方法，也可以设计成多个方法或者其他类中的方法。
returnJson，调用扩展方法拦截时返回的结果配置。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)//标记注解的作用域在方法层面
public @interface DoMethodExt {
        String method() default "";//方法名
        String returnJson() default "";//失败结果 JSON
}
