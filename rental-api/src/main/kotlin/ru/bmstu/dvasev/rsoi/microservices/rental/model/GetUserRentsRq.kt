package ru.bmstu.dvasev.rsoi.microservices.rental.model

import javax.validation.constraints.NotEmpty

data class GetUserRentsRq(
    @get:NotEmpty
    val username: String? = null
)
