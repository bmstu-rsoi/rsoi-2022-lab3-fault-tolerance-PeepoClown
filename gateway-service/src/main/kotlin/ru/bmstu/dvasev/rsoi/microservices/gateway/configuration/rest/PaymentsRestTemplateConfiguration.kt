package ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest.properties.PaymentRestProperties

@Configuration
class PaymentsRestTemplateConfiguration(
    private val restTemplateConfiguration: CommonRestTemplateConfiguration
) {

    @Bean("paymentsRestTemplate")
    fun paymentsRestTemplate(properties: PaymentRestProperties): RestTemplate {
        return restTemplateConfiguration
            .buildRestTemplate(properties)
    }
}
