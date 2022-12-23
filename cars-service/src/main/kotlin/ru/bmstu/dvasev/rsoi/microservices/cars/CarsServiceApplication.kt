package ru.bmstu.dvasev.rsoi.microservices.cars

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CarsServiceApplication

fun main(args: Array<String>) {
    runApplication<CarsServiceApplication>(*args)
}
