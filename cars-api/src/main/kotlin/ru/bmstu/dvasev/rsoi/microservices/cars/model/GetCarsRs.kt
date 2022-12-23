package ru.bmstu.dvasev.rsoi.microservices.cars.model

data class GetCarsRs(
    val page: Int? = null,
    val pageSize: Int? = null,
    val totalElements: Int,
    val items: List<CarModel>
)
