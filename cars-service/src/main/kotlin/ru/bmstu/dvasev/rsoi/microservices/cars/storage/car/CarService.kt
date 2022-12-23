package ru.bmstu.dvasev.rsoi.microservices.cars.storage.car

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Pageable.unpaged
import org.springframework.stereotype.Service
import ru.bmstu.dvasev.rsoi.microservices.cars.model.CarModel
import ru.bmstu.dvasev.rsoi.microservices.cars.model.GetCarsRq
import ru.bmstu.dvasev.rsoi.microservices.cars.storage.car.dao.CarRepository
import ru.bmstu.dvasev.rsoi.microservices.cars.storage.car.entity.Car
import ru.bmstu.dvasev.rsoi.microservices.cars.storage.car.model.PageableCarsModel
import java.util.Optional
import java.util.Objects.nonNull
import java.util.Optional.empty
import java.util.Optional.of
import java.util.UUID.fromString

@Service
class CarService(
    private val carRepository: CarRepository
) {

    fun getCarsPageable(getCarsRq: GetCarsRq): PageableCarsModel {
        var pageRequest: Pageable = unpaged()
        if (nonNull(getCarsRq.page) && nonNull(getCarsRq.size)) {
            pageRequest = PageRequest.of(getCarsRq.page!!, getCarsRq.size!!)
        }

        val cars = if (nonNull(getCarsRq.showAll)) {
            if (getCarsRq.showAll!!)
                carRepository.findAll(pageRequest).toList()
            else carRepository.findByAvailabilityTrue(pageRequest)
        } else carRepository.findByAvailabilityTrue(pageRequest)

        val carsList = cars.map(this::toCarModel)
        return PageableCarsModel(
            cars = carsList,
            page = if (pageRequest.isPaged) pageRequest.pageNumber + 1 else 1,
            pageSize = carsList.size
        )
    }

    fun findCarByUid(carUid: String): Optional<CarModel> {
        return carRepository
            .findByCarUid(fromString(carUid))
            .map { toCarModel(it) }
    }

    fun reserve(carUid: String): Optional<CarModel> {
        val foundCar = carRepository.findByCarUid(fromString(carUid))
        if (foundCar.isEmpty) {
            return empty();
        }
        val car = Car(
            id = foundCar.get().id,
            carUid = foundCar.get().carUid,
            brand = foundCar.get().brand,
            model = foundCar.get().model,
            registrationNumber = foundCar.get().registrationNumber,
            power = foundCar.get().power,
            price = foundCar.get().price,
            type = foundCar.get().type,
            availability = false
        )
        return of(carRepository.save(car))
            .map { toCarModel(it) }
    }

    fun unreserve(carUid: String): Optional<CarModel> {
        val foundCar = carRepository.findByCarUid(fromString(carUid))
        if (foundCar.isEmpty) {
            return empty();
        }
        val car = Car(
            id = foundCar.get().id,
            carUid = foundCar.get().carUid,
            brand = foundCar.get().brand,
            model = foundCar.get().model,
            registrationNumber = foundCar.get().registrationNumber,
            power = foundCar.get().power,
            price = foundCar.get().price,
            type = foundCar.get().type,
            availability = true
        )
        return of(carRepository.save(car))
            .map { toCarModel(it) }
    }

    private fun toCarModel(car: Car) =
        CarModel(
            carUid = car.carUid.toString(),
            brand = car.brand!!,
            model = car.model!!,
            registrationNumber = car.registrationNumber!!,
            power = car.power,
            type = car.type!!,
            price = car.price!!,
            available = car.availability!!
        )
}
