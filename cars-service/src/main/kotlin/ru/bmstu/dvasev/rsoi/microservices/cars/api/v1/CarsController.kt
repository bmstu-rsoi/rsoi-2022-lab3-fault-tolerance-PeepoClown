package ru.bmstu.dvasev.rsoi.microservices.cars.api.v1

import mu.KotlinLogging
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.bmstu.dvasev.rsoi.microservices.cars.model.CarModel
import ru.bmstu.dvasev.rsoi.microservices.cars.model.GetCarByUidRq
import ru.bmstu.dvasev.rsoi.microservices.cars.model.GetCarsRq
import ru.bmstu.dvasev.rsoi.microservices.cars.model.GetCarsRs
import ru.bmstu.dvasev.rsoi.microservices.cars.storage.car.CarService
import ru.bmstu.dvasev.rsoi.microservices.common.model.ApiResponse
import ru.bmstu.dvasev.rsoi.microservices.common.model.ErrorResponse
import javax.validation.Valid

@RestController
@RequestMapping(
    path = ["api/v1/cars"],
    consumes = [APPLICATION_JSON_VALUE],
    produces = [APPLICATION_JSON_VALUE]
)
class CarsController(
    private val carService: CarService
) {

    private val log = KotlinLogging.logger {}

    @PostMapping("get/pageable")
    fun getCarsPagination(@Valid @RequestBody request: GetCarsRq): ApiResponse<GetCarsRs> {
        log.debug { "Received new get cars pageable request. $request" }
        val carsPageable = carService.getCarsPageable(request)
        val response = ApiResponse(
            httpCode = OK,
            response = GetCarsRs(
                page = carsPageable.page,
                pageSize = carsPageable.pageSize,
                totalElements = carsPageable.cars.size,
                items = carsPageable.cars
            )
        )
        log.debug { "Return a get cars pageable response. $response" }
        return response
    }

    @PostMapping("find")
    fun findCar(@Valid @RequestBody request: GetCarByUidRq): ApiResponse<CarModel> {
        log.debug { "Received new find car by uid request. $request" }
        val car = carService.findCarByUid(request.carUid)
        if (car.isEmpty) {
            log.warn { "Failed to find car by uid ${request.carUid}" }
            return ApiResponse(
                httpCode = NOT_FOUND,
                error = ErrorResponse("Failed to find car by uid ${request.carUid}")
            )
        }
        val response = ApiResponse(
            httpCode = OK,
            response = car.get()
        )
        log.debug { "Successfully found car by uid ${request.carUid}. $response" }
        return response
    }

    @PatchMapping("reserve/{carUid}")
    fun reserveCar(@PathVariable("carUid") carUid: String): ApiResponse<CarModel> {
        log.debug { "Received new reserve card with uid $carUid request" }
        val car = carService.reserve(carUid)
        if (car.isEmpty) {
            log.warn { "Failed to reserve car with uid $carUid" }
            return ApiResponse(
                httpCode = NOT_FOUND,
                error = ErrorResponse("Failed to reserve car with uid $carUid")
            )
        }
        val response = ApiResponse(
            httpCode = OK,
            response = car.get()
        )
        log.debug { "Successfully reserve car with uid $carUid. Response: $response" }
        return response
    }

    @PatchMapping("unreserve/{carUid}")
    fun unreserveCar(@PathVariable("carUid") carUid: String): ApiResponse<CarModel> {
        log.debug { "Received new unreserve card with uid $carUid request" }
        val car = carService.unreserve(carUid)
        if (car.isEmpty) {
            log.warn { "Failed to unreserve car with uid $carUid" }
            return ApiResponse(
                httpCode = NOT_FOUND,
                error = ErrorResponse("Failed to unreserve car with uid $carUid")
            )
        }
        val response = ApiResponse(
            httpCode = OK,
            response = car.get()
        )
        log.debug { "Successfully unreserve car with uid $carUid. Response: $response" }
        return response
    }
}
