package cn.rainyak.middleware.SOA.aop;

import cn.rainyak.middleware.SOA.annotation.DoWhiteList;
import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanUtils;
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

import javax.annotation.Resource;
import java.lang.reflect.Method;


// DoJoinPoint，是整个中间件的核心部分，它负责对所有添加自定义注解的方法进行拦截和逻辑处理。
@Aspect//声明这是一个切面类
@Component
@Order(3)
public class DoJoinPoint {
    private Logger logger = LoggerFactory.getLogger(DoJoinPoint.class.getName());//创建日志对象
    @Resource
    private String whiteListConfig;//注入白名单配置
    /*
    @Pointcut 注解，声明切点，这里的切点是 cn.rainyak.middleware.whitelist.annotation.DoWhiteList 注解。
     */
    @Pointcut("@annotation(cn.rainyak.middleware.SOA.annotation.DoWhiteList)")//声明切点
    public void aopPoint() {
    }
    @Around("aopPoint()")//声明环绕通知
    public Object doRouter(ProceedingJoinPoint jp) throws Throwable{
        Method method = getMethod(jp);//获取方法
        DoWhiteList whiteList = method.getAnnotation(DoWhiteList.class);//获取注解
        String keyValue = getFiledValue(whiteList.key(), jp.getArgs());//获取字段值
        logger.info("middleware whitelist handler method：{} value：{}", method.getName(), keyValue);//打印日志
        if (null == keyValue || "".equals(keyValue)) return jp.proceed();//如果字段值为空，直接返回
        String[] split = whiteListConfig.split(",");//获取白名单配置
        for (String str : split) {//白名单过滤
            if (keyValue.equals(str)) {
                return jp.proceed();
            }
        }
        return returnObject(whiteList, method);//拦截
    }
    /*
    getMethod 方法，是对拦截到的方法进行提取，这里的提取方式是通过反射获取方法。
     */
    private Method getMethod(ProceedingJoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();//获取签名
        MethodSignature methodSignature = (MethodSignature) sig;//获取方法签名
        return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());//获取方法
    }
    /*
    returnObject 方法，是对拦截后的返回信息进行处理，这里的处理方式是通过反射创建一个对象，然后将注解中的返回信息赋值给对象的属性，最后返回对象。
     */
    private Object returnObject(DoWhiteList whiteList, Method method) throws Exception{
        Class<?> returnType = method.getReturnType();//获取返回类型
        String returnJson = whiteList.returnJson();//获取返回信息
        if("".equals(returnJson)) {
            return returnType.newInstance();
        }
        return JSON.parseObject(returnJson, returnType);//返回对象
    }
    /*
    getFiledValue 方法，是对注解中的字段值进行提取，这里的提取方式是通过反射获取方法入参中的字段值。
     */
    private String getFiledValue(String key, Object[] args){
        String filedValue = "";
        for (Object arg : args) {
            try{
                if(null == filedValue || "".equals(filedValue)){//如果字段值为空
                    filedValue = BeanUtils.getProperty(arg, key);//获取字段值
                }else{
                    break;//如果字段值不为空，直接跳出循环
                }
            } catch (Exception e) {
                if (args.length == 1){
                    filedValue = args[0].toString();
                    return filedValue;//如果字段值为空，直接返回
                }
            }
        }
        return filedValue;
    }

}
