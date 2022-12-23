package ru.bmstu.dvasev.rsoi.microservices.rental

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RentalServiceApplication

fun main(args: Array<String>) {
    runApplication<RentalServiceApplication>(*args)
}
