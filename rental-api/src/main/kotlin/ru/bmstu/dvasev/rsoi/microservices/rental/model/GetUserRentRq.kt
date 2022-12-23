package ru.bmstu.dvasev.rsoi.microservices.rental.model

import javax.validation.constraints.NotEmpty

data class GetUserRentRq(
    @get:NotEmpty
    val username: String? = null,
    @get:NotEmpty
    val rentalUid: String? = null
)
