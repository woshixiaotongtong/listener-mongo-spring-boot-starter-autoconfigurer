package com.alice.mongo.listen.container;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.messaging.DefaultMessageListenerContainer;
import org.springframework.data.mongodb.core.messaging.MessageListenerContainer;

/**
 * @program: MessageListenerContainerInitializing
 * @description: 初始化mongo自带的listen容器，注入spring
 * @author: alice
 **/
@Configuration
public class MessageListenerContainerInitializing implements ApplicationContextAware {

    private ConfigurableApplicationContext applicationContext;

    private SimpleAsyncTaskExecutor simpleAsyncTaskExecutor;

    private MongoTemplate mongoTemplate;

    public MessageListenerContainerInitializing(MongoTemplate mongoTemplate) {
        //定义线程池
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(10);
        simpleAsyncTaskExecutor.setThreadNamePrefix("mongo-consumer-");
        this.simpleAsyncTaskExecutor = simpleAsyncTaskExecutor;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Bean
    public MessageListenerContainer getMessageListenerContainer() {
        MessageListenerContainer messageListenerContainer = new DefaultMessageListenerContainer(mongoTemplate, simpleAsyncTaskExecutor) {
            @Override
            public boolean isAutoStartup() {
                return true;
            }
        };
        return messageListenerContainer;
    }
}
