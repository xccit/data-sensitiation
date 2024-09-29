---
title: 浅析数据脱敏
date: 2024-09-29 0:25
tags: Java开发技巧

---

# 浅析数据脱敏

## 数据脱敏的概念

**数据脱敏是一种保护敏感信息的技术，通过对原始数据进行变换或修改来隐藏敏感信息，从而在非生产环境中安全使用脱敏后的数据**。以下是对数据脱敏概念的具体介绍：

1. **基本概念**：数据脱敏，也称为数据去隐私化或数据变形，是对敏感数据进行有策略的修改或替换的过程，旨在防止未经授权的访问或泄露。这种技术通过特定的规则和策略，将敏感信息转换为看似相似但不含真正敏感细节的数据副本，以供开发、测试、分析或培训等非生产环境使用。
2. **主要类型**：静态数据脱敏通常在数据被提取并复制到非生产环境之前完成一次性脱敏处理，适用于数据外发场景，如提供给第三方或用于测试数据库。动态数据脱敏在数据查询过程中实时进行，当用户访问敏感数据时，系统自动对其进行脱敏处理，适用于直接连接生产数据库的场景。
3. **实现方式**：包括使用脚本手动编写代码进行脱敏，以及利用专业的数据脱敏产品进行自动化脱敏，后者能够提高脱敏效率并节省操作时间。
4. **技术手段**：常见的脱敏技术包括数据替换、屏蔽、加密、混淆和伪装等，这些方法可以根据实际业务场景选择，以满足数据一致性和可用性的需求。
5. **应用场景**：数据脱敏广泛应用于金融、医疗、政府等领域，特别是在涉及个人身份信息、联系方式、银行卡信息等敏感数据的处理中。
6. **重要性**：数据脱敏技术的重要性在于它能够在不降低数据价值的前提下，有效保护个人隐私和商业机密不被泄露。随着大数据和云计算的发展，数据脱敏成为了确保数据安全合规的关键措施之一。

总的来说，数据脱敏是一种关键的信息安全技术，它通过多种手段和方法，有效地保护了敏感数据的安全，同时也支持了数据的有效利用。在实施数据脱敏时，需要综合考虑数据一致性、可用性和安全性的要求，选择合适的技术和方法来实现最佳的数据保护效果。

## 数据脱敏的实现方式

### 1.前端脱敏

* 后端返回的数据已经进行了脱敏处理
* 后端返回的数据没有脱敏处理,前端使用JavaScript等脚本进行脱敏

### 2.后端脱敏

* 数据存储时直接进行加密处理,例如AES、RSA等对称/非对称加密方式
* 数据返回给前端时使用混淆/替换

## 前后端脱敏各自的弊端

### 1.前端脱敏的弊端

#### 1.1 安全性不够

总的来说,数据脱敏就是对一些敏感数据进行保护,不想被用户看到,但是前端接收到的数据为原文的话,即使JS等脚本进行处理,原来的响应结果也不会变,只是页面显示的东西变了而已,没有实际意义。

### 2.后端脱敏的弊端

#### 2.1无法模糊检索

如何理解检索呢?以查询用户信息为例,假如代入一个条件:`匹配手机号前三位为177,后四位为0000`的用户,假如是精确匹配,比如我就要`手机号为17799890000`这个用户的信息,那SQL可以是这样的:

假设自定义一个解密函数为`deenc(phone varchar(11))`,那么SQL为:

```sql
SELECT username,nick_name,birthday,deenc(phone) as phone FROM t_user WHERE phone = 'edcXdejhopacahah89avavsaib';
```

其中,`edcXdejhopacahah89avavsaib`为前端传入的`17799890000`这条数据在后端被加密算法加密之后的数据,那么精确匹配的话,可以很快查询到这条数据,流程图可以是这样的:

![image-20240928235655491](https://fastly.jsdelivr.net/gh/xccit/blog_images/images_resource/image-20240928235655491.png)

但是,精确匹配只需要后端的业务层对前端传过来的数据进行加密,交给dao去数据库精确匹配即可,只需要查询这一条数据,并且将手机号解密后返回给前端,如果是模糊查询呢?比如`匹配手机号前三位为177,后四位为0000`的用户,那么正常的SQL应该是:

```sql
SELECT username,nick_name,birthday,deenc(phone) as phone FROM t_user WHERE deenc(phone) like '177%0000';
```

首先,需要将所有数据进行解密之后再匹配`177%0000`,假设数据库中有10000w条数据,那么我们会解密到什么时候呢?这是一道送命题。我们必须在`数据安全`与`用户体验`中做出选择。所以在数据脱敏这个问题上,基本上不会有公司做得很好,经过撞库等操作用户敏感信息被泄漏的事情并不少,这里就不点名了,`d(懂)d(的)d(都)d(懂)`

## 优雅实现数据脱敏

> 我们以SpringBoot为例,自行封装工具类,使用策略设计模式来写一个高拓展性的数据脱敏工具

### 1.搭建SpringBoot工程

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.1</version>
    </parent>
    <groupId>io.xccit</groupId>
    <artifactId>data-sensitiation</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <description>
        Java数据脱敏
    </description>
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.30</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>3.0.3</version>
        </dependency>
        <!--knife4j 文档增强-->
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
            <version>4.4.0</version>
        </dependency>
    </dependencies>

</project>
```

```yaml
spring:
  application:
    name: data-desensitization
    version: 1.0.0
    description: 数据脱敏
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/data-sensitization?useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: root
#mybatis配置
mybatis:
  type-aliases-package: io.xccit.domain
  mapper-locations: classpath:mapper/**/*.xml
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
# springdoc-openapi项目配置
springdoc:
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha
  api-docs:
    path: /v3/api-docs
  group-configs:
    - group: 'default'
      paths-to-match: '/**'
      packages-to-scan: io.xccit.controller
# knife4j的增强配置，不需要增强可以不配
knife4j:
  enable: true
  setting:
    language: zh_cn
    footer-custom-content: Apache License 2.0 | Copyright  2024-[xccit](https://xccit.github.io)
    swagger-model-name:
```

### 2.准备数据模型

![image-20240929112339500](https://fastly.jsdelivr.net/gh/xccit/blog_images/images_resource/image-20240929112339500.png)

### 3.封装统一返回结果

```java
package io.xccit.r;

import lombok.Builder;
import lombok.Data;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>全局返回对象</p>
 */
@Data
@Builder
public class AjaxResult<T> {
    private Integer code;
    private String message;
    private T data;

    public AjaxResult() {}
    public AjaxResult(ResultEnum resultEnum, T data){
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.data = data;
    }
    public AjaxResult(Integer code, String message,T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 请求成功
     * @param data 数据
     * @return AjaxResult
     * @param <T> 参数类型
     */
    public static <T> AjaxResult success(T data){
        if (data != null){
            return AjaxResult.builder().code(200).message("SUCCESS").data(data).build();
        }
        return AjaxResult.builder().code(200).message("SUCCESS").build();
    }

    /**
     * 请求成功
     * @param resultEnum 返回类型枚举
     * @param data 数据
     * @return AjaxResult
     * @param <T> 参数类型
     */
    public static <T> AjaxResult success(ResultEnum resultEnum,T data){
        if (data != null){
            return AjaxResult.builder().code(resultEnum.getCode()).message(resultEnum.getMessage()).data(data).build();
        }
        return AjaxResult.builder().code(resultEnum.getCode()).message(resultEnum.getMessage()).build();
    }

    /**
     * 请求失败
     * @return AjaxResult
     * @param <T> 参数类型
     */
    public static <T> AjaxResult fail(){
        return new AjaxResult(ResultEnum.SERVER_ERROR,null);
    }

    /**
     * 请求失败
     * @param resultEnum 返回类型枚举
     * @return AjaxResult
     * @param <T> 参数类型
     */
    public static <T> AjaxResult fail(ResultEnum resultEnum){
        return new AjaxResult(resultEnum,null);
    }
}
```

```java
package io.xccit.r;

import lombok.Getter;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>返回类型枚举</p>
 */
@Getter
public enum ResultEnum {
    SUCCESS(200,"SUCCESS"),
    SERVER_ERROR(500,"SERVER ERROR"),
    NOT_FOUND(404,"NOT FOUND"),
    BAD_REQUEST(400,"BAD REQUEST"),
    UNAUTHORIZED(401,"UNAUTHORIZED"),
    FORBIDDEN(403,"FORBIDDEN"),
    NOT_SUPPORT(405,"NOT SUPPORT"),
    NOT_ACCEPTABLE(406,"NOT ACCEPTABLE"),
    UNSUPPORTED_MEDIA_TYPE(415,"UNSUPPORTED MEDIA TYPE");
    private final Integer code;
    private final String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
```

### 4.编写控制器,业务,数据访问

> 完整代码访问github: [xccit/data-sensitiation: 数据脱敏demo (github.com)](https://github.com/xccit/data-sensitiation)

### 5.核心逻辑代码

```java
package io.xccit.desensitization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>脱敏注解</p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Desensitization {

    SensitiveType type();
}
```

```java
package io.xccit.desensitization;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>脱敏类型</p>
 */
public enum SensitiveType {
    BANK_CARD,
    PHONE,
    ID_CARD;
}
```

```java
package io.xccit.desensitization.strategy;

import io.xccit.desensitization.SensitiveType;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>脱敏策略</p>
 */
public interface IDesensitizationStrategy {

    /**
     * 判断是否支持脱敏
     * @param type 待脱敏的类型
     * @return 是否支持
     */
    boolean support(SensitiveType type);

    /**
     * 脱敏
     * @param value 待脱敏的值
     * @return 脱敏后的值
     */
    String desensitize(String value);
}
```

```java
package io.xccit.desensitization.strategy.impl;

import io.xccit.desensitization.SensitiveType;
import io.xccit.desensitization.strategy.IDesensitizationStrategy;
import org.springframework.stereotype.Component;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>银行卡脱敏</p>
 */
@Component
public class BankCardDesensitization implements IDesensitizationStrategy {
    /**
     * 判断是否支持脱敏
     *
     * @param type 待脱敏的类型
     * @return 是否支持
     */
    @Override
    public boolean support(SensitiveType type) {
        return type.equals(SensitiveType.BANK_CARD);
    }

    /**
     * 脱敏
     *
     * @param value 中国所有银行支持的银行卡号
     * @return 脱敏后的值
     */
    @Override
    public String desensitize(String value) {
        if (value != null && value.length() == 19){
            return "**** **** **** " + value.substring(12,19);
        }
        return "不支持的类型";
    }
}
```

`......`

> 完整代码访问github即可

## 测试数据脱敏

### 1.脱敏前

![image-20240929113727280](https://fastly.jsdelivr.net/gh/xccit/blog_images/images_resource/image-20240929113727280.png)

```java
/**
     * 根据id查询用户,脱敏后返回
     *
     * @param id 用户id
     * @return 用户信息
     */
    @Override
    public UserVO getUser(Long id) {
        User user = userMapper.getUser(id);
        UserVO vo = new UserVO();
        /*try {
            return (UserVO) desensitizationUtil.desensitize(user, vo);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }*/
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
```

### 2.脱敏后

![image-20240929113901034](https://fastly.jsdelivr.net/gh/xccit/blog_images/images_resource/image-20240929113901034.png)

```java
/**
     * 根据id查询用户,脱敏后返回
     *
     * @param id 用户id
     * @return 用户信息
     */
    @Override
    public UserVO getUser(Long id) {
        User user = userMapper.getUser(id);
        UserVO vo = new UserVO();
        try {
            return (UserVO) desensitizationUtil.desensitize(user, vo);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
/*        BeanUtils.copyProperties(user, vo);
        return vo;*/
    }
```

## 总结

总之,代码有多种不同的设计方法来实现需求,并且需求永远是写不完的,总会有一个场景让你不得不改变部分需求。本案例的代码可以加入Spring Security进行权限校验,并且用户密码也没有写加密方法,可以自行实现一下,如需更多脱敏类型,只需在类型的`枚举`中加入类型字段,然后再编写实现类实现`IDesensitizationStrategy`接口,编写自己的业务逻辑即可,后续在`vo或do`中字段标注该注解即可。
