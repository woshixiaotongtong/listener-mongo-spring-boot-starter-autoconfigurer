package com.alice.mongo.listen.config;


import com.alice.mongo.listen.annotation.MongoListener;
import com.alice.mongo.listen.container.MongoMessageContainerHolder;
import com.alice.mongo.listen.listen.MongoMessageListener;
import com.alice.mongo.listen.model.ResumeToken;
import com.mongodb.client.model.changestream.FullDocument;
import com.mongodb.client.model.changestream.OperationType;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.messaging.ChangeStreamRequest;
import org.springframework.data.mongodb.core.messaging.MessageListenerContainer;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.ErrorHandler;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @program: ChangeStreamConfiguration
 * @description:
 * @author: alice
 **/
public class ChangeStreamConfiguration implements ApplicationContextAware, SmartInitializingSingleton, DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(ChangeStreamConfiguration.class);

    private ConfigurableApplicationContext applicationContext;

    public ChangeStreamConfiguration() {
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(MongoListener.class);

        if (Objects.nonNull(beans)) {
            beans.forEach(this::registerContainer);
        }
    }

    /**
     * @Description: 1. ?????????????????? Annotation
     * 2. ?????? Annotation ??? topic ???????????????
     * 3. ?????????????????????(????????????MessageContainer)
     */
    private void registerContainer(String beanName, Object bean) {
        Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);

        if (!MongoMessageListener.class.isAssignableFrom(bean.getClass())) {
            throw new IllegalStateException(clazz + " is not instance of " + MongoListener.class.getName());
        }

        MongoListener annotation = clazz.getAnnotation(MongoListener.class);

        String collection = annotation.collection();
        String mongoDbName = annotation.database();

        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        String resolvePlaceholdersDbName = environment.resolvePlaceholders(mongoDbName);
        String resolvePlaceholdersCollection = environment.resolvePlaceholders(collection);

        if (!Objects.isNull(resolvePlaceholdersDbName)) {
            mongoDbName = resolvePlaceholdersDbName;
        }

        if (!Objects.isNull(resolvePlaceholdersCollection)) {
            collection = resolvePlaceholdersCollection;
        }

        Class<?> aClass = annotation.responseClass();
        boolean enableResume = annotation.enableSync();

        // ??????holder??????????????????????????????collection
        MongoMessageContainerHolder.putContainer(mongoDbName, collection, (MongoMessageListener) bean);

        MessageListenerContainer messageListenerContainer =
                applicationContext.getBean(MessageListenerContainer.class);

        ErrorHandler errorHandler = (Throwable e) -> log.error("mongo watch has exception:{}", e.getMessage());
        String finalMongoDbName = mongoDbName;
        messageListenerContainer.register(genGroupRequest(finalMongoDbName, collection, (MongoMessageListener) bean, enableResume),
                aClass, errorHandler);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    /**
     * @Description: ???????????????????????????
     */
    @Override
    public void destroy() throws Exception {
        MongoMessageContainerHolder.getAllHandler().entrySet().removeIf(entry -> true);
    }

    /**
     * ?????? change stream ?????????
     */
    private ChangeStreamRequest genGroupRequest(String dnName, String collectionName,
                                                MongoMessageListener messageListener, boolean enableResume) {

        ChangeStreamRequest.ChangeStreamRequestBuilder builder = ChangeStreamRequest.builder(messageListener)
                .database(dnName)
                .collection(collectionName)
                .filter(newAggregation(match(where("operationType").in(
                        OperationType.INSERT.getValue(),
                        OperationType.UPDATE.getValue(),
                        OperationType.DELETE.getValue()))))
                .fullDocumentLookup(FullDocument.UPDATE_LOOKUP);

        // ???????????????????????????
        if (enableResume) {
            BsonDocument resumeToken = this.getLastToken(collectionName);
            if (resumeToken != null) {
                builder.resumeAfter(resumeToken);
            }
        }

        return builder.build();
    }

    /**
     * ?????????changeStream????????????token
     *
     * @param collectionName
     * @return
     */
    public BsonDocument getLastToken(String collectionName) {
        String applicationName = applicationContext.getApplicationName();
        String id = applicationContext.getId();
        log.info("?????????????????????:{},??????id:{}", applicationName, id);

        MongoTemplate mongoTemplate = applicationContext.getBean(MongoTemplate.class);

        Query query = Query.query(Criteria.where("appId").is(id)
                .and("collectionName").is(collectionName));
        ResumeToken resumeToken = mongoTemplate.findOne(query, ResumeToken.class);
        if (resumeToken == null || StringUtils.isEmpty(resumeToken.getToken())) {
            return null;
        }
        return new BsonDocument("_data", new BsonString(resumeToken.getToken()));
    }

    /**
     * ?????? ???????????????ResumeToken
     *
     * @param collectionName
     * @param token
     */
    public void setToken(String collectionName, String token) {
        long now = System.currentTimeMillis();
        MongoTemplate mongoTemplate = applicationContext.getBean(MongoTemplate.class);
        String id = applicationContext.getId();

        Query query = Query.query(Criteria.where("appId").is(id)
                .and("collectionName").is(collectionName));
        ResumeToken mongoResumeToken = mongoTemplate.findOne(query, ResumeToken.class);
        if (mongoResumeToken != null) {
            Update update = Update.update("updateTime", now).set("token", token);
            mongoTemplate.updateMulti(query, update, ResumeToken.class);
        } else {
            ResumeToken resumeToken = ResumeToken.builder()
                    .collection(collectionName)
                    .token(token)
                    .createTime(now)
                    .updateTime(now).build();
            mongoTemplate.save(resumeToken);
        }
    }
}
