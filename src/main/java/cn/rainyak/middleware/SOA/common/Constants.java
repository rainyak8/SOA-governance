package cn.rainyak.middleware.SOA.common;

import com.google.common.util.concurrent.RateLimiter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/*
cn.rainyak.middleware.ratelimiter.Constants 类中定义了一个静态的 Map，用于存储每个方法的限流器，key 为类名.方法名，value 为限流器。
 */
public class Constants {
    public static Map<String, RateLimiter> rateLimiterMap = Collections.synchronizedMap(new HashMap<String, RateLimiter>());
}
