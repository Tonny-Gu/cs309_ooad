package com.sustech.dboj.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket( DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //.title("swagger-bootstrap-ui-demo RESTful APIs")
                        .description("# SUSTech DBOJ RESTful APIs")
                        .termsOfServiceUrl("http://www.xx.com/")
                        .contact("11811805@sustech.edu.cn")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("Release Version")
                .select()
                //这里指定Controller扫描包路径
                .apis( RequestHandlerSelectors.basePackage("com.sustech.dboj.backend.controller"))
                .paths( PathSelectors.any())
                .build();
    }
}