package cn.cst.myspringboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.cst.myspringboot.dao")
public class MySpringbootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MySpringbootApplication.class, args);
    }

}
