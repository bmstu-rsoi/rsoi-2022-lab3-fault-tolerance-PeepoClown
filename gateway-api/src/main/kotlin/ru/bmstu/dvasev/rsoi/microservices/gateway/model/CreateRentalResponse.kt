package ru.bmstu.dvasev.rsoi.microservices.gateway.model

import ru.bmstu.dvasev.rsoi.microservices.payment.model.PaymentStatus
import ru.bmstu.dvasev.rsoi.microservices.rental.model.RentStatus

data class PaymentInfo(
    val paymentUid: String,
    val status: PaymentStatus,
    val price: Int
)

data class CreateRentalResponse(
    val rentalUid: String,
    val status: RentStatus,
    val carUid: String,
    val dateFrom: String,
    val dateTo: String,
    val payment: PaymentInfo
)
