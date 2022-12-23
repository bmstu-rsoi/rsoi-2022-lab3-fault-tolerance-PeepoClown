package ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import java.time.Duration
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Validated
@ConstructorBinding
@ConfigurationProperties("rental.rest")
data class RentalRestProperties(
    @get:NotBlank
    override val baseUrl: String,
    @get:NotNull
    override val connectionTimeout: Duration = Duration.ofSeconds(3),
    @get:NotNull
    override val readTimeout: Duration = Duration.ofSeconds(15),
    @get:Min(1)
    @get:Max(50)
    @get:NotNull
    override val maxThreads: Int = 10,
    @get:NotNull
    val createRentalPath: String = "",
    @get:NotNull
    val getUserRent: String = "/user/find",
    @get:NotNull
    val getUserRents: String = "/user/get",
    @get:NotNull
    val changeRentStatusPath: String = "/status"
) : CommonRestTemplateProperties
