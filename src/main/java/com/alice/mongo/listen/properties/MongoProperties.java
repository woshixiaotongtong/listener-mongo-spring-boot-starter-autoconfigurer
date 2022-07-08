package com.alice.mongo.listen.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;


/**
 * @program: MongoProperties
 * @description: mongo配置
 * @author: alice
 **/
@Configuration
@ConfigurationProperties(prefix = MongoProperties.MONGO_PREFIX)
public class MongoProperties implements Serializable {
    public static final String MONGO_PREFIX = "mongo.watch";

    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 端口
     */
    private String port;
    /**
     * 集群ip，用逗号分割 例如： 192.*.*.*,192.*.*.*
     */
    private String mongoHosts;
    /**
     * 连接的数据库名
     */
    private String database;
    /**
     * 线程池最大数
     */
    private int maxPoolSize;
    /**
     * 线程池最小数
     */
    private int minPoolSize;
    private int waitQueueMultiple;

    private Boolean enabled = true;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getMongoHosts() {
        return mongoHosts;
    }

    public void setMongoHosts(String mongoHosts) {
        this.mongoHosts = mongoHosts;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(int minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public int getWaitQueueMultiple() {
        return waitQueueMultiple;
    }

    public void setWaitQueueMultiple(int waitQueueMultiple) {
        this.waitQueueMultiple = waitQueueMultiple;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
