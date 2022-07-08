package com.alice.mongo.listen.annotation;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author alice
 * @desc 监听注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@EnableAutoConfiguration
public @interface MongoListener {

    // 设置监听的名称，暂无作用
    String[]  value() default {};

    // 监听的mongo数据库
    String database() default "";

    // 每个collection映射的实体，可以不写
    Class<?> responseClass();

    // mongo的collection
    String collection() default "";

    // 考虑后期对监听进行分组
    String groupId() default "";

    // TODO 涉及到resumeToken的持久化 待定需求 是否开启断点续传
    boolean enableSync() default false;
}
