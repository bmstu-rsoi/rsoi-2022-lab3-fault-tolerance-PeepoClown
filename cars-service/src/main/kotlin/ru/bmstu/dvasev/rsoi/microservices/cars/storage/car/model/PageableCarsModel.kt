package ru.bmstu.dvasev.rsoi.microservices.cars.storage.car.model

import ru.bmstu.dvasev.rsoi.microservices.cars.model.CarModel

data class PageableCarsModel(
    val cars: List<CarModel>,
    val page: Int,
    val pageSize: Int
)
