package cn.rainyak.middleware.SOA.aop;

import cn.rainyak.middleware.SOA.annotation.DoRateLimiter;
import cn.rainyak.middleware.SOA.value.IValveService;
import cn.rainyak.middleware.SOA.value.impl.RateLimiterValve;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/*
@Aspect 定义切面、@Component 定义组件以求被实例化、@Pointcut 定义切点，这些与我们之前的内容一样。
@Around("aopPoint() && @cn.rainyak.middleware.ratelimiter.annotation(doRateLimiter)") ，这块的处理可以直接通过方法入参的方式可以更加方便的拿到注解，处理起来也更优雅。
接下来就是方法内容的调用逻辑，valveService.access(jp, getMethod(jp), doGovern, jp.getArgs()); 这步调用的就是我们包装好的限流保护服务。
这里的调用可以和熔断保护联想起来，也就是这些服务可以统一包装在一起。
 */
@Aspect
@Component//定义切面、定义组件以求被实例化、定义切点
@Order(1)
public class DoRateLimiterPoint {
    /*
    @Pointcut("@annotation(cn.rainyak.middleware.ratelimiter.annotation.DoRateLimiter)") 定义切点，这些与我们之前的内容一样。
     */
    @Pointcut("@annotation(cn.rainyak.middleware.SOA.annotation.DoRateLimiter)")
    public void aopPoint() {
    }
    /*
    @Around("aopPoint() && @annotation(doRateLimiter)") ，这块的处理可以直接通过方法入参的方式可以更加方便的拿到注解，处理起来也更优雅。
     */
    @Around("aopPoint() && @annotation(doRateLimiter)")
    public Object doRouter(ProceedingJoinPoint jp, DoRateLimiter doRateLimiter) throws Throwable {
        IValveService valveService = new RateLimiterValve();
        return valveService.access(jp, getMetod(jp), doRateLimiter, jp.getArgs());
    }
    /*
    这里的调用可以和熔断保护联想起来，也就是这些服务可以统一包装在一起。
     */
    private Method getMetod(ProceedingJoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }
}
