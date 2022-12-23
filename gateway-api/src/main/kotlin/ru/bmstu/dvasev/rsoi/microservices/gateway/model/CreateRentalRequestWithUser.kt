package ru.bmstu.dvasev.rsoi.microservices.gateway.model

import java.time.LocalDate

data class CreateRentalRequestWithUser(
    val carUid: String,
    val dateFrom: LocalDate,
    val dateTo: LocalDate,
    val username: String
)
