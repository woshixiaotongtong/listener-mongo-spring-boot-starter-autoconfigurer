# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.1/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.1/maven-plugin/reference/html/#build-image)


#项目简介

- mongo版本 3.6及以上支持changeStream，建议 4.3及以上支持更加
- jdk 1.8
- maven 3.8.3
- spring boot 2.3.5

次项目用于监听mongo的数据变更，基于mongo的changeStream功能做上层的数据监听

#项目的使用

实际上是想封装成starter，进行引入使用，目前只写好了autoconfigure</br>
可以自行创建starter引入autoconfigure即可

- <h3>使用注意事项及步骤</h3></br>
  ①根据MongoProperties进行配置</br>
  ②使用MongoListener注解配置监听的信息，继承MongoMessageListener，可查看MyListen实例代码</br>
  ③如果构建starter使用，在启动ð的注解需要添加扫描的包路径：@SpringBootApplication(scanBasePackages = {"com.alice.mongo.listen","自己项目路径"})
  </br></br></br></br>
- <h3>MongoListener属性说明</h3></br>
  ① value 设置监听的名称，暂无作用，可以作为监听的唯一标识，避免重复监听</br>
  ② 监听的mongo数据库 database  考虑是否需要此配置</br>
  ③ 每个collection映射的实体，可以不写 responseClass</br>
  ④ mongo的collection名称 collection</br>
  ⑤ 考虑后期对监听进行分组，同组可以加一些优化待定 groupId</br>
  ⑥ 涉及到resumeToken的持久化 待定需求 是否开启断点续传 enableSync


#存在问题
- 1.starter方式引入需要添加扫描包路径，需要完善EnableMongoWatcher注解在启动类添加次注解即可
- 2.token的保存实现断点续传，次版本暂时关闭token的保存，
  由于在创建changeStream是就需要放入token，所以目前想法使用aop解决，后续完善，另外token需要持久化，待定持久化到何地。
- 3.starter还没有完善
- 4.当没有mongoClient的连接是，目前的连接配置是否可以在spring-data-mongo的配置基础上添加


<h1>打赏</h1>
![](https://github.com/woshixiaotongtong/listener-mongo-spring-boot-starter-autoconfigurer/blob/main/images/%E6%94%B6%E6%AC%BE%E7%A0%81.jpeg)