package ru.bmstu.dvasev.rsoi.microservices.rental.model

import javax.validation.constraints.NotEmpty

data class RentalStatusChangeRq(
    @get:NotEmpty
    val rentalUid: String? = null,
    val status: RentStatus? = null
)
