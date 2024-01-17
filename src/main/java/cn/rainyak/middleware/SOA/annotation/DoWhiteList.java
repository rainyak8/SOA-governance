package cn.rainyak.middleware.SOA.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// DoWhiteList，是一个自定义注解。它作用就是在需要使用到的白名单服务的接口上，添加此注解并配置必要的信息。
// 接口入参提取字段属性名称、拦截后的返回信息
/*
@Retention 是注解的注解，也称作元注解。
这个注解里面有一个入参信息 RetentionPolicy.RUNTIME 在它的注释中有这样一段描述：
Annotations are to be recorded in the class file by the compiler and retained by the VM at run time, so they may be read reflectively.
其实说的就是加了这个注解，它的信息会被带到JVM运行时，当你在调用方法时可以通过反射拿到注解信息。
除此之外，RetentionPolicy 还有两个属性 SOURCE、CLASS，其实这三个枚举正式对应了Java代码的加载和运行顺序
Java源码文件 -> .class文件 -> 内存字节码。
并且后者范围大于前者，所以一般情况下只需要使用 RetentionPolicy.RUNTIME 即可。
@Target 也是元注解起到标记作用，它的注解名称就是它的含义，目标，也就是我们这个自定义注解 DoWhiteList 要放在类、接口还是方法上。
自定义注解 @DoWhiteList 中有两个属性 key、returnJson。key 的作用是配置当前接口入参需要提取的属性
returnJson 的作用是在我们拦截到用户请求后需要给一个返回信息。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoWhiteList {
    String key() default "";// 接口入参提取字段属性名称
    String returnJson() default "";// 拦截后的返回信息
}
