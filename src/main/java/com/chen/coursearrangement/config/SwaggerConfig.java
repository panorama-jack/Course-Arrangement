package com.chen.coursearrangement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SwaggerConfig {


    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     *
     * @return
     */
    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("标准接口")
                .apiInfo(apiInfo("Spring Boot中使用Swagger2构建RESTful APIs", "1.0"))  //设置API文档的基本信息，包括标题和版本等。
                .useDefaultResponseMessages(true)
                .forCodeGeneration(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.chen.coursearrangement.controller"))//指定扫描的包路径，只有该路径下的Controller类的接口才会被展示在API文档中。
                .paths(PathSelectors.any()) //匹配所有路径，不做限制。
                .build();  //构建Docket实例。
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://ip:port/swagger-ui.html
     *
     * @return
     */
    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfoBuilder()
                .title(title)
                .description("永远相信美好的事情即将发生！")
                .contact(new Contact("Jack Chen", "", "explosion_1@163.com"))
                .version(version)
                .build();
    }
}