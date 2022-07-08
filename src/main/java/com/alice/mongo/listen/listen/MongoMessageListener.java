package com.alice.mongo.listen.listen;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;
import org.springframework.data.mongodb.core.messaging.Message;
import org.springframework.data.mongodb.core.messaging.MessageListener;

/**
 * @param <T>
 * @author alice
 * @desc 监听changeStream回调
 */
public abstract class MongoMessageListener<T> implements MessageListener<ChangeStreamDocument<Document>, T> {

    /**
     * 回掉函数
     *
     * @param message
     */
    @Override
    public abstract void onMessage(Message<ChangeStreamDocument<Document>, T> message);

}