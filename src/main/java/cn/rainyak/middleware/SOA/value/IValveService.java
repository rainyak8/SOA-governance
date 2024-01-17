package cn.rainyak.middleware.SOA.value;

import cn.rainyak.middleware.SOA.annotation.DoHystrix;
import cn.rainyak.middleware.SOA.annotation.DoMethodExt;
import cn.rainyak.middleware.SOA.annotation.DoRateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
/***
    实现接口
 ***/

public interface IValveService {
    Object access(ProceedingJoinPoint jp, Method method, Annotation annotation, Object[] args) throws Throwable;
}
