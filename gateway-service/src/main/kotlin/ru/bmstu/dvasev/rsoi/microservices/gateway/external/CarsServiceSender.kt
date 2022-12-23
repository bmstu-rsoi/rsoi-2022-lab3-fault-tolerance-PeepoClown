package ru.bmstu.dvasev.rsoi.microservices.gateway.external

import mu.KotlinLogging
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.bmstu.dvasev.rsoi.microservices.cars.model.CarModel
import ru.bmstu.dvasev.rsoi.microservices.cars.model.GetCarByUidRq
import ru.bmstu.dvasev.rsoi.microservices.cars.model.GetCarsRq
import ru.bmstu.dvasev.rsoi.microservices.cars.model.GetCarsRs
import ru.bmstu.dvasev.rsoi.microservices.common.model.ApiResponse
import ru.bmstu.dvasev.rsoi.microservices.common.model.ErrorResponse
import ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest.properties.CarsRestProperties
import java.nio.charset.StandardCharsets.UTF_8

class WrappedGetCarsRs(httpCode: HttpStatus) : ApiResponse<GetCarsRs>(httpCode)
class WrappedGetCarByUidRs(httpCode: HttpStatus) : ApiResponse<CarModel>(httpCode)
class WrappedReserveCarRs(httpCode: HttpStatus) : ApiResponse<CarModel>(httpCode)

@Service
class CarsServiceSender(
    private val carsRestTemplate: RestTemplate,
    private val carsRestProperties: CarsRestProperties
) {

    private val log = KotlinLogging.logger {}

    fun getAvailableCars(getCarsRq: GetCarsRq): ApiResponse<GetCarsRs> {
        val request = HttpEntity(getCarsRq, buildHeaders())
        log.debug { "Sending get available cars request. $request" }

        return try {
            val response =
                carsRestTemplate.postForObject(carsRestProperties.getCarsPath, request, WrappedGetCarsRs::class.java)
            log.info { "Successfully received response from get available cars service. $response" }
            response!!
        } catch (ex: Exception) {
            log.warn(ex) { "Failed to send get available cars request. ${ex.message}" }
            ApiResponse(
                httpCode = INTERNAL_SERVER_ERROR,
                error = ErrorResponse("Failed to send get available cars request. ${ex.message}")
            )
        }
    }

    fun findCarByUid(carUid: String): ApiResponse<CarModel> {
        val findCarRq = GetCarByUidRq(carUid = carUid)
        val request = HttpEntity(findCarRq, buildHeaders())
        log.debug { "Sending find car by uid request" }

        return try {
            val response = carsRestTemplate.postForObject(
                carsRestProperties.findCarPath,
                request,
                WrappedGetCarByUidRs::class.java
            )
            log.info { "Successfully received response from get car by uid service. $response" }
            response!!
        } catch (ex: Exception) {
            log.warn(ex) { "Failed to send get car by uid request. ${ex.message}" }
            ApiResponse(
                httpCode = INTERNAL_SERVER_ERROR,
                error = ErrorResponse("Failed to send get car by uid request. ${ex.message}")
            )
        }
    }

    fun reserveCar(carUid: String): ApiResponse<CarModel> {
        val request = HttpEntity(null, buildHeaders())
        log.debug { "Sending reserve car request" }

        return try {
            val response = carsRestTemplate.patchForObject(
                carsRestProperties.reservePath + "/$carUid",
                request,
                WrappedReserveCarRs::class.java
            )
            log.info { "Successfully received response from reserve car service" }
            response!!
        } catch (ex: Exception) {
            log.warn(ex) { "Failed to reserve car with uid $carUid" }
            ApiResponse(
                httpCode = INTERNAL_SERVER_ERROR,
                error = ErrorResponse("Failed to reserve car with uid $carUid")
            )
        }
    }

    fun unreserveCar(carUid: String): ApiResponse<CarModel> {
        val request = HttpEntity(null, buildHeaders())
        log.debug { "Sending unreserve car request" }

        return try {
            val response = carsRestTemplate.patchForObject(
                carsRestProperties.unreservePath + "/$carUid",
                request,
                WrappedReserveCarRs::class.java
            )
            log.info { "Successfully received response from unreserve car service" }
            response!!
        } catch (ex: Exception) {
            log.warn(ex) { "Failed to unreserve car with uid $carUid" }
            ApiResponse(
                httpCode = INTERNAL_SERVER_ERROR,
                error = ErrorResponse("Failed to unreserve car with uid $carUid")
            )
        }
    }

    private fun buildHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.contentType = MediaType("application", "json", UTF_8)
        return headers
    }
}
