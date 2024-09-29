package io.xccit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>启动类</p>
 */
@SpringBootApplication
@MapperScan(basePackages = {"io.xccit.mapper"})
public class DataSensitizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataSensitizationApplication.class, args);
        System.out.println("数据脱敏启动成功~~");
    }
}
