package io.github.yakami129.starter.netty.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author kaka
 * @Date 2022/4/20
 */
@SpringBootApplication
public class TestApplication {

    public static void main(final String[] args) {
        new SpringApplicationBuilder(TestApplication.class)
                .run(args);
    }

}