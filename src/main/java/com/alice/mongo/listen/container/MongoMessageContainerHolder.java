package com.alice.mongo.listen.container;


import com.alice.mongo.listen.listen.MongoMessageListener;

import java.util.HashMap;
import java.util.Map;


/**
 * @program: MongoMessageContainerHolder
 * @description: 为了防止重复的监听
 * @author: alice
 **/
public class MongoMessageContainerHolder {

    private static Map<String, MongoMessageListener> routesMap = new HashMap<>();

    public static void putContainer(String database, String collection, MongoMessageListener handler) {

        String containerKey = new StringBuffer(database).append(":").append(collection).toString();

        if (routesMap.get(containerKey) != null) {
            throw new IllegalStateException("only one the same  database and collection ,please check your code right now.");
        } else {
            routesMap.put(containerKey, handler);
        }
    }

    public static MongoMessageListener getContainer(String database, String collection) {
        String containerKey = new StringBuffer(database).append(":").append(collection).toString();
        return routesMap.get(containerKey);
    }

    public static Map<String, MongoMessageListener> getAllHandler() {
        return routesMap;
    }


    public static void handler() {
    }

}
