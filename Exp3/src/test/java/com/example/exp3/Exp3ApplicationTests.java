package com.example.exp3;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.context.SpringBootTest;


@MapperScan("com.example.exp3.mapper")
@SpringBootTest
class Exp3ApplicationTests {

    @Test
    void contextLoads() {
    }

}
