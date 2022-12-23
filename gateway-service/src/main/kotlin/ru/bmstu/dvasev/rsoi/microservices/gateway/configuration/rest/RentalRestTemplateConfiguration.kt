package ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest.properties.RentalRestProperties

@Configuration
class RentalRestTemplateConfiguration(
    private val restTemplateConfiguration: CommonRestTemplateConfiguration
) {

    @Bean("rentalRestTemplate")
    fun rentalRestTemplate(properties: RentalRestProperties): RestTemplate {
        return restTemplateConfiguration
            .buildRestTemplate(properties)
    }
}
