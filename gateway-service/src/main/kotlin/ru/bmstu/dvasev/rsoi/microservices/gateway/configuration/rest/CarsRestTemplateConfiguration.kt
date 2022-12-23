package ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest.properties.CarsRestProperties

@Configuration
class CarsRestTemplateConfiguration(
    private val restTemplateConfiguration: CommonRestTemplateConfiguration
) {

    @Bean("carsRestTemplate")
    fun carsRestTemplate(properties: CarsRestProperties): RestTemplate {
        return restTemplateConfiguration
            .buildRestTemplate(properties)
    }
}
