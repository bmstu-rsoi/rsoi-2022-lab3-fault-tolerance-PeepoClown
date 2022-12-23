package ru.bmstu.dvasev.rsoi.microservices.rental.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.time.LocalDate
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CreateRentRq(
    @get:NotEmpty
    val username: String? = null,
    @get:NotEmpty
    val carUid: String? = null,
    @get:NotEmpty
    val paymentUid: String? = null,
    @get:NotNull
    @JsonSerialize(using = LocalDateSerializer::class)
    @JsonDeserialize(using = LocalDateDeserializer::class)
    val dateFrom: LocalDate? = null,
    @get:NotNull
    @JsonSerialize(using = LocalDateSerializer::class)
    @JsonDeserialize(using = LocalDateDeserializer::class)
    val dateTo: LocalDate? = null,
)
