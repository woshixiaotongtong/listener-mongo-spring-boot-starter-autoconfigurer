package com.alice.mongo.listen.annotation;

import java.lang.annotation.*;

/**
 * @author alice
 * @desc 没有此注解，在使用的时候需要在springApplication注解添加需要扫面的包路径
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
// @Import({.class})
public @interface EnableMongoWatcher {

    boolean autoRegister() default true;
}
