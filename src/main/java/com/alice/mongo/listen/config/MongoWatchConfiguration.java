package com.alice.mongo.listen.config;

import com.alice.mongo.listen.properties.MongoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

/**
 * @program: MongoWatchConfiguration
 * @description:
 * @author: alice
 **/
@Configuration
@ConditionalOnProperty(prefix = "mongo.watch", name = "enable", havingValue = "true")
@EnableConfigurationProperties({MongoProperties.class})
@Import(ChangeStreamConfiguration.class)
public class MongoWatchConfiguration {

    @Autowired
    private MongoProperties mongoProperties;

    @Bean
    @ConditionalOnMissingBean(name = "watchMongoTemplate")
    public MongoTemplate watchMongoTemplate(@Qualifier("watchMongoDatabaseFactory") MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }


    private String getMongoHosts() {
        StringBuffer stringBuffer = new StringBuffer();
        String[] hosts = mongoProperties.getMongoHosts().split(",");
        for (String host : hosts) {
            stringBuffer.append(host).append(":").append(mongoProperties.getPort()).append(",");
        }
        return stringBuffer.toString();
    }

    @Bean
    @ConditionalOnMissingBean(name = "watchMongoDatabaseFactory")
    public MongoDatabaseFactory watchMongoDatabaseFactory() {
        MongoDatabaseFactory mongoDbFactory =
                new SimpleMongoClientDatabaseFactory(
                        "mongodb://" + mongoProperties.getUserName()
                                + ":" + mongoProperties.getPassword()
                                + "@" + getMongoHosts() + "/"
                                + mongoProperties.getDatabase());
        return mongoDbFactory;
    }

}
