package ru.bmstu.dvasev.rsoi.microservices.cars.storage.car.dao

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.bmstu.dvasev.rsoi.microservices.cars.storage.car.entity.Car
import java.util.*

@Repository
interface CarRepository : JpaRepository<Car, Long> {

    fun findByAvailabilityTrue(pageable: Pageable): List<Car>

    fun findByCarUid(carUid: UUID): Optional<Car>
}
