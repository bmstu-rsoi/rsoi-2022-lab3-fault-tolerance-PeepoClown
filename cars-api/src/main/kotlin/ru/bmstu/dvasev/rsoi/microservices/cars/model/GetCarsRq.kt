package ru.bmstu.dvasev.rsoi.microservices.cars.model

import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class GetCarsRq(
    @get:Min(0)
    val page: Int? = null,
    @get:Min(1)
    @get:Max(100)
    val size: Int? = null,
    val showAll: Boolean? = null
)
