package ru.bmstu.dvasev.rsoi.microservices.payment.model

data class PaymentModel(
    val paymentUid: String,
    val status: PaymentStatus,
    val price: Int
)
