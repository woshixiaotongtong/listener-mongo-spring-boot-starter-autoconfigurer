package com.alice.mongo.listen.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @program: ResumeToken
 * @description: 可用于持久货token数据
 * @author: alice
 **/
@Document(collection = "t_resume_token")
@Builder
@ToString
@Data
public class ResumeToken {
    @Field("token")
    private String token;
    @Field("database")
    private String database;
    @Field("collection")
    private String collection;
    @Field("createTime")
    private Long createTime;
    @Field("updateTime")
    private Long updateTime;
}
