package ru.bmstu.dvasev.rsoi.microservices.payment.model

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class CreatePaymentRq(
    @get:Min(1)
    @get:NotNull
    val price: Int? = null
)
