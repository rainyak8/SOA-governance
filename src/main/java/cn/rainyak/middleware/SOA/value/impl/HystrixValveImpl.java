package cn.rainyak.middleware.SOA.value.impl;

import cn.rainyak.middleware.SOA.annotation.DoHystrix;
import cn.rainyak.middleware.SOA.annotation.DoRateLimiter;
import cn.rainyak.middleware.SOA.common.Constants;
import cn.rainyak.middleware.SOA.value.IValveService;
import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.hystrix.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class HystrixValveImpl extends HystrixCommand<Object> implements IValveService {
    private ProceedingJoinPoint jp;
    private Method method;
    private DoHystrix doHystrix;
    private Object target;
    public HystrixValveImpl(DoHystrix doHystrix) {
        /*********************************************************************************************
         * 置HystrixCommand的属性
         * GroupKey：            该命令属于哪一个组，可以帮助我们更好的组织命令。
         * CommandKey：          该命令的名称
         * ThreadPoolKey：       该命令所属线程池的名称，同样配置的命令会共享同一线程池，若不配置，会默认使用GroupKey作为线程池名称。
         * CommandProperties：   该命令的一些设置，包括断路器的配置，隔离策略，降级设置，以及一些监控指标等。
         * ThreadPoolProperties：关于线程池的配置，包括线程池大小，排队队列的大小等
         *********************************************************************************************/
        /*
        这段代码是在配置HystrixCommand的属性。HystrixCommand是Netflix开源的Hystrix库中的一个类，用于实现服务的熔断和降级。
         */
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GovernGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("GovernKey"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GovernThreadPool"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(doHystrix.timeoutValue())
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties
                        .Setter().withCoreSize(10)));
    }
    /*
    access方法的作用是执行被拦截的方法，并在执行过程中应用Hystrix的熔断和降级策略。
    如果被拦截的方法执行成功，那么access方法就返回该方法的返回值；
    如果被拦截的方法执行失败，那么access方法就返回DoHystrix注解中设置的降级结果。
     */
    @Override
    public Object access(ProceedingJoinPoint jp, Method method, Annotation annotation, Object[] args) throws Throwable {
        if (annotation instanceof DoHystrix) {
            this.doHystrix = (DoHystrix) annotation;
            this.jp = jp;
            this.method = method;
            this.target = jp.getTarget();

            //设置超时时间
            HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GovernGroup"))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                            .withExecutionTimeoutInMilliseconds(doHystrix.timeoutValue()));
            return this.execute();
        } else {
            return jp.proceed();
        }
    }
    /*
    这个run方法的执行结果会被Hystrix用来判断被拦截的方法是否执行成功。
    如果run方法执行成功，那么Hystrix就认为被拦截的方法执行成功；
    如果run方法执行失败（即抛出了异常），那么Hystrix就认为被拦截的方法执行失败，此时Hystrix会启动熔断和降级策略。
     */
    @Override
    protected Object run() throws Exception{
        try {
            return jp.proceed();
        } catch (Throwable e) {
            return null;
        }
    }
    /*
    这个getFallback方法的作用是在被拦截的方法执行失败时，返回DoHystrix注解中设置的降级方法。
     */
    @Override
    protected Object getFallback(){
        try {
            // 获取备用方法的名称
            String fallbackMethodName = doHystrix.fallbackMethod();

            // 获取备用方法所在的类
            Class<?> fallbackClass = doHystrix.fallbackClass();

            // 获取备用方法
            Method fallbackMethod = fallbackClass.getMethod(fallbackMethodName);

            // 调用备用方法
            return fallbackMethod.invoke(target);
        } catch (Exception e) {
            return null;
        }
    }
}
