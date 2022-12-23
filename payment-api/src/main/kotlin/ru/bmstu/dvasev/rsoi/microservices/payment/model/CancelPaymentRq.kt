package ru.bmstu.dvasev.rsoi.microservices.payment.model

import javax.validation.constraints.NotEmpty

data class CancelPaymentRq(
    @get:NotEmpty
    val paymentUid: String? = null
)
