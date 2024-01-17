package cn.rainyak.middleware.SOA.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

//@ConfigurationProperties，用于创建指定前缀( prefix = "rainyak.whitelist" )的自定义配置信息
// 这样就在 yml 或者 properties 中读取到我们自己设定的配置信息。
@ConfigurationProperties("rainyak.whitelist")
public class WhiteListProperties {
    private String users;
    public String getUsers() {
        return users;
    }
    public void setUsers(String users) {
        this.users = users;
    }//getters and setters
}
