package ru.bmstu.dvasev.rsoi.microservices.cars.model

data class CarModel(
    val carUid: String,
    val brand: String,
    val model: String,
    val registrationNumber: String,
    val power: Int? = null,
    val type: CarType,
    val price: Int,
    val available: Boolean
)
