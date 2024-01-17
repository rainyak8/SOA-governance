package cn.rainyak.middleware.SOA.value.impl;

import cn.rainyak.middleware.SOA.common.Constants;
import cn.rainyak.middleware.SOA.annotation.DoRateLimiter;
import cn.rainyak.middleware.SOA.value.IValveService;
import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
/*
这是一个名为RateLimiterValve的Java类，它实现了IValveService接口。
这个类的主要功能是限制方法的访问速率。
在access方法中，首先检查DoRateLimiter注解的permitsPerSecond属性是否为0 如果是，则直接执行目标方法。
如果permitsPerSecond不为0，那么会根据类名和方法名生成一个键，然后在Constants.rateLimiterMap中查找是否已经存在一个RateLimiter实例。
如果不存在，则创建一个新的RateLimiter实例并存入Constants.rateLimiterMap。
然后尝试从RateLimiter获取许可，如果获取成功，则执行目标方法。如果获取失败，则返回DoRateLimiter注解的returnJson属性对应的对象。
 */
public class RateLimiterValve implements IValveService {

    public Object access(ProceedingJoinPoint jp, Method method, Annotation annotation, Object[] args) throws Throwable {
        if (annotation instanceof DoRateLimiter) {
            DoRateLimiter doRateLimiter = (DoRateLimiter) annotation;
            if (doRateLimiter.permitsPerSecond() == 0) {
                return jp.proceed();
            } else {
                String clazzName = jp.getTarget().getClass().getName();
                String methodName = method.getName();
                String key = clazzName + "." + methodName;
                if (Constants.rateLimiterMap.get(key) == null) {
                    Constants.rateLimiterMap.put(key, RateLimiter.create(doRateLimiter.permitsPerSecond()));
                }

                RateLimiter rateLimiter = (RateLimiter) Constants.rateLimiterMap.get(key);
                if (rateLimiter.tryAcquire()) {
                    return jp.proceed();
                }

                return JSON.parseObject(doRateLimiter.returnJson(), method.getReturnType());
            }
        } else {
            return jp.proceed();
        }
    }
}
