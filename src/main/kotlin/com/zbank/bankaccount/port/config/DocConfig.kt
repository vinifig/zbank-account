package com.zbank.bankaccount.port.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.BasicAuth
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@Configuration
@EnableSwagger2
class DocConfig {
    @Bean
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .securitySchemes(listOf(basicAuthScheme()))
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()

    private fun apiInfo() = ApiInfoBuilder()
        .title("ZBank API")
        .description("ZBank account and transactions documentation, " +
            "secure routes must be Basic authenticated with accountId:cpf")
        .version("1.0")
        .build()

    private fun basicAuthScheme(): SecurityScheme {
        return BasicAuth("basicAuth")
    }
}
