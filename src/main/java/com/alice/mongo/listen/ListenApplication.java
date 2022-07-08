package com.alice.mongo.listen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author alice
 * @desc 测试启动累，后续回注调
 */
@SpringBootApplication
public class ListenApplication {

    //
    public static void main(String[] args) {
        SpringApplication.run(ListenApplication.class, args);
    }
}
