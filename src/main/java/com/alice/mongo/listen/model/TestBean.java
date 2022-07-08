package com.alice.mongo.listen.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author alice
 * 测试实体
 */
@Data
public class TestBean implements Serializable {

    private String name;
    private Integer age;
}