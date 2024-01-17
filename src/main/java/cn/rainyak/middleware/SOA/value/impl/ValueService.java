package cn.rainyak.middleware.SOA.value.impl;

import cn.rainyak.middleware.SOA.annotation.DoHystrix;
import cn.rainyak.middleware.SOA.annotation.DoRateLimiter;
import cn.rainyak.middleware.SOA.value.IValveService;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ValueService implements IValveService {
    private HystrixValveImpl hystrixValve;
    private RateLimiterValve rateLimiterValve;

    public ValueService() {
        this.hystrixValve = new HystrixValveImpl(1000); // 你可以根据需要调整这个值
        this.rateLimiterValve = new RateLimiterValve();
    }

    @Override
    public Object access(ProceedingJoinPoint jp, Method method, Annotation annotation, Object[] args) throws Throwable {
        if (annotation instanceof DoHystrix) {
            return hystrixValve.access(jp, method, (DoHystrix) annotation, args);
        } else if (annotation instanceof DoRateLimiter) {
            return rateLimiterValve.access(jp, method, (DoRateLimiter) annotation, args);
        } else {
            return jp.proceed();
        }
    }
}
