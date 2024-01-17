package cn.rainyak.middleware.SOA.aop;

import cn.rainyak.middleware.SOA.annotation.DoMethodExt;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/*
根据自定义注解作为切点拦截到的方法以后，开始做切面处理。主要包含以下几点：

获取自定义注解中的信息，主要是拿到扩展的方法名称和返回内容。
getClass(jp).getMethod(methodName, method.getParameterTypes()) 获取自定义方法，并在后面判断了此扩展方法的返回类型，这里我们设置为布尔类型校验。
接下来就是自定义方法的调用，执行的就是用户在自定义方法中实现的内容。
最后根据自定义方法的返回结果，如果是 true 则放行，否则返回 returnJson 中的配置结果。
 */
@Aspect//切面
@Component//组件
@Order(2)
public class DoMethodExtPoint {
    private Logger logger = LoggerFactory.getLogger(DoMethodExtPoint.class);//日志
    @Pointcut("@annotation(cn.rainyak.middleware.SOA.annotation.DoMethodExt)")//切点
    public void aopPoint() {
    }
    @Around("aopPoint()")//环绕
    public Object doRouter(ProceedingJoinPoint jp) throws Throwable {//获取自定义注解中的信息，主要是拿到扩展的方法名称和返回内容。
        // 获取内容
        Method method = getMethod(jp);
        DoMethodExt doMethodExt = method.getAnnotation(DoMethodExt.class);
        // 获取拦截方法
        String methodName = doMethodExt.method();
        // 功能处理
        Method methodExt = getClass(jp).getMethod(methodName, method.getParameterTypes());
        Class<?> returnType = methodExt.getReturnType();
        // 判断方法返回类型
        if (!returnType.getName().equals("boolean")) {
            throw new RuntimeException("annotation @DoMethodExt set method：" + methodName + " returnType is not boolean");
        }
        // 拦截判断正常，继续
        boolean invoke = (boolean) methodExt.invoke(jp.getThis(), jp.getArgs());
        // 返回结果
        return invoke ? jp.proceed() : JSON.parseObject(doMethodExt.returnJson(), method.getReturnType());
    }
    /*
       获取自定义方法，并在后面判断了此扩展方法的返回类型
     */
    private Method getMethod(JoinPoint jp) throws NoSuchMethodException{
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }

    private Class<? extends Object> getClass(JoinPoint jp) throws NoSuchMethodException {
        return jp.getTarget().getClass();
    }
}
