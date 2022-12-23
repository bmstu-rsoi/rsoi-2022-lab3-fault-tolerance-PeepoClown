package ru.bmstu.dvasev.rsoi.microservices.cars.model

import javax.validation.constraints.NotEmpty

data class GetCarByUidRq(
    @get:NotEmpty
    val carUid: String
)
