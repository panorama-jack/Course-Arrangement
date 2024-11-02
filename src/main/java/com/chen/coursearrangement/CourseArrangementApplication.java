package com.chen.coursearrangement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableWebMvc
@SpringBootApplication
public class CourseArrangementApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseArrangementApplication.class, args);
    }

}
