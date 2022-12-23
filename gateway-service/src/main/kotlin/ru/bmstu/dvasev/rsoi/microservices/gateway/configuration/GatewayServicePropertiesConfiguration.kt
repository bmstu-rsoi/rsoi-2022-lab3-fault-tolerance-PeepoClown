package ru.bmstu.dvasev.rsoi.microservices.gateway.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest.properties.CarsRestProperties
import ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest.properties.PaymentRestProperties
import ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest.properties.RentalRestProperties

@Configuration
@EnableConfigurationProperties(
    CarsRestProperties::class,
    PaymentRestProperties::class,
    RentalRestProperties::class
)
class GatewayServicePropertiesConfiguration
