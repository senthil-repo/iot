package com.uk.iot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Senthil on 03/06/2020.
 */
@SpringBootApplication
@EnableSwagger2
public class Service {
    static final Logger logger = LogManager.getLogger(Service.class);

    public static void main(String[] args) {
        logger.info(" ******* Service is loading *******");
        SpringApplication.run(Service.class);
    }

    @Bean
    public Docket swaggerConfig() {
        return new Docket(DocumentationType.SWAGGER_12).select().
                paths(PathSelectors.ant("/iot/event/v1")).
                apis(RequestHandlerSelectors.basePackage("com.uk.iot")).
                build().
                apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
       List<VendorExtension> vendorExtensionList = new ArrayList<>();
        return new ApiInfo(
                "IOT Tracking Device report Api",
                "Api for reporting IOT tracking device ",
                "1.0",
                "Free to use",
                new Contact("sample", "sample", "sample"),
                "Free to use",
                "/",
                vendorExtensionList);

         }
    }
