package ru.bmstu.dvasev.rsoi.microservices.payment.model

import javax.validation.constraints.NotNull

data class GetPaymentByUidRq(
    @get:NotNull
    val paymentUid: String,
)
