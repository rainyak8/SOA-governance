package cn.rainyak.middleware.SOA.aop;

import cn.rainyak.middleware.SOA.annotation.DoHystrix;
import cn.rainyak.middleware.SOA.value.IValveService;
import cn.rainyak.middleware.SOA.value.impl.HystrixValveImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect//@Aspect是Spring框架中的注解，用于定义切面（Aspect）。
@Component//Spring框架中的注解，用于将一个类标识为Spring容器的组件。
@Order(4)
public class DoHystrixPoint {
    /*
    @Pointcut注解用于定义切点，即上面所说的那个位置。它有一个属性value，用于定义切点表达式。
    这个表达式决定了哪些方法会被AOP框架拦截。这个例子表示所有使用了DoHystrix注解的方法都会被拦截。
     */
    @Pointcut("@annotation(cn.rainyak.middleware.SOA.annotation.DoHystrix)")
    public void aopPoint() {
    }
    @Around("aopPoint() && @annotation(doGovern)")//在切点方法周围织入代码
    public Object doRouter(ProceedingJoinPoint jp, DoHystrix doGovern) throws Throwable {
        IValveService valveService = (IValveService) new HystrixValveImpl(doGovern.timeoutValue());
        return valveService.access(jp, getMethod(jp), doGovern, jp.getArgs());
        /*
        access方法的作用是执行被拦截的方法，并在执行过程中应用Hystrix的熔断和降级策略。
        如果被拦截的方法执行成功，那么access方法就返回该方法的返回值；
        如果被拦截的方法执行失败，那么access方法就返回DoHystrix注解中设置的降级结果。
         */
    }
    /*
    用来获取被AOP拦截的方法的Method对象。
    首先，Signature sig = jp.getSignature();这行代码获取了被拦截的方法的签名，这个签名包含了方法的名称和参数类型。
    然后，MethodSignature methodSignature = (MethodSignature) sig;这行代码将Signature对象转换为MethodSignature对象。
    MethodSignature是Signature的子类，它提供了一些额外的方法，如getMethod()，可以直接获取被拦截的方法的Method对象。
     */
    private Method getMethod(JoinPoint jp) throws NoSuchMethodException{
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(methodSignature.getName(),methodSignature.getParameterTypes());
    }
}
