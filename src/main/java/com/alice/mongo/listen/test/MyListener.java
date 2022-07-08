package com.alice.mongo.listen.test;

import com.alice.mongo.listen.annotation.MongoListener;
import com.alice.mongo.listen.listen.MongoMessageListener;
import com.alice.mongo.listen.model.TestBean;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @program: MyListener
 * @description: 测试监听
 * @author: alice
 **/
@Slf4j
@Component
@MongoListener(database = "${mongo.watch.database}", collection = "${mongo.watch.collection}", responseClass = TestBean.class)
public class MyListener extends MongoMessageListener<TestBean> {
    @Override
    public void onMessage(Message<ChangeStreamDocument<Document>, TestBean> message) {
        TestBean body = message.getBody();
        log.info("变更的数据：{}", body);
        String token = message.getRaw().getResumeToken().get("_data").asString().getValue();
        log.info("变更的数据的token：{}", token);

        // TODO  数据token的存储
    }
}
