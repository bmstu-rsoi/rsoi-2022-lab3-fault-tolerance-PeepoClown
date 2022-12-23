package ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import java.time.Duration
import java.time.Duration.ofSeconds
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Validated
@ConstructorBinding
@ConfigurationProperties("payment.rest")
data class PaymentRestProperties(
    @get:NotBlank
    override val baseUrl: String,
    @get:NotNull
    override val connectionTimeout: Duration = ofSeconds(3),
    @get:NotNull
    override val readTimeout: Duration = ofSeconds(15),
    @get:Min(1)
    @get:Max(50)
    @get:NotNull
    override val maxThreads: Int = 10,
    @get:NotNull
    val createPaymentPath: String = "",
    @get:NotNull
    val findPaymentPath: String = "/find",
    @get:NotNull
    val cancelPayment: String = "/cancel"
): CommonRestTemplateProperties
