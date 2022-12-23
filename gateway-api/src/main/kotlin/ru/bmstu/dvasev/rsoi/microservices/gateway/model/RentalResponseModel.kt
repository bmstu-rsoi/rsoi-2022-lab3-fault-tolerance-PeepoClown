package ru.bmstu.dvasev.rsoi.microservices.gateway.model

import ru.bmstu.dvasev.rsoi.microservices.payment.model.PaymentStatus
import ru.bmstu.dvasev.rsoi.microservices.rental.model.RentStatus

data class CarResponseModel(
    val carUid: String,
    val brand: String,
    val model: String,
    val registrationNumber: String
)

data class PaymentResponseModel(
    val paymentUid: String,
    val status: PaymentStatus,
    val price: Int
)

data class RentalResponseModel(
    val rentalUid: String,
    val status: RentStatus,
    val dateFrom: String,
    val dateTo: String,
    val car: CarResponseModel,
    val payment: PaymentResponseModel
)
