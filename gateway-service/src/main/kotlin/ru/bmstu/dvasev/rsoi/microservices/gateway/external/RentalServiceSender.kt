package ru.bmstu.dvasev.rsoi.microservices.gateway.external

import mu.KotlinLogging
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.bmstu.dvasev.rsoi.microservices.common.model.ApiResponse
import ru.bmstu.dvasev.rsoi.microservices.common.model.ErrorResponse
import ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest.properties.RentalRestProperties
import ru.bmstu.dvasev.rsoi.microservices.rental.model.CreateRentRq
import ru.bmstu.dvasev.rsoi.microservices.rental.model.GetUserRentRq
import ru.bmstu.dvasev.rsoi.microservices.rental.model.GetUserRentsRq
import ru.bmstu.dvasev.rsoi.microservices.rental.model.GetUserRentsRs
import ru.bmstu.dvasev.rsoi.microservices.rental.model.RentalModel
import ru.bmstu.dvasev.rsoi.microservices.rental.model.RentalStatusChangeRq
import java.nio.charset.StandardCharsets

class WrappedCreateRentRs(httpCode: HttpStatus) : ApiResponse<RentalModel>(httpCode)
class WrappedGetUserRentRs(httpCode: HttpStatus) : ApiResponse<RentalModel>(httpCode)
class WrappedChangeStatusRentRs(httpCode: HttpStatus) : ApiResponse<RentalModel>(httpCode)
class WrappedGetUserRentsRs(httpCode: HttpStatus) : ApiResponse<GetUserRentsRs>(httpCode)

@Service
class RentalServiceSender(
    private val rentalRestTemplate: RestTemplate,
    private val rentalRestProperties: RentalRestProperties
) {

    private val log = KotlinLogging.logger {}

    fun createRental(createRentRq: CreateRentRq): ApiResponse<RentalModel> {
        val request = HttpEntity(createRentRq, buildHeaders())
        log.debug { "Sending create rent request. $request" }

        return try {
            val response = rentalRestTemplate.postForObject(
                rentalRestProperties.createRentalPath,
                request,
                WrappedCreateRentRs::class.java
            )
            log.info { "Successfully received response from create rent service. $response" }
            response!!
        } catch (ex: Exception) {
            log.warn(ex) { "Failed to send create rent request. ${ex.message}" }
            ApiResponse(
                httpCode = HttpStatus.INTERNAL_SERVER_ERROR,
                error = ErrorResponse("Failed to send create rent request. ${ex.message}")
            )
        }
    }

    fun getRentalByUserAndUid(getUserRentRq: GetUserRentRq): ApiResponse<RentalModel> {
        val request = HttpEntity(getUserRentRq, buildHeaders())
        log.debug { "Sending get user rent request. $request" }

        return try {
            val response = rentalRestTemplate.postForObject(
                rentalRestProperties.getUserRent,
                request,
                WrappedGetUserRentRs::class.java
            )
            log.info { "Successfully received response from get user rent service. $response" }
            response!!
        } catch (ex: Exception) {
            log.warn(ex) { "Failed to send get user rent request. ${ex.message}" }
            ApiResponse(
                httpCode = HttpStatus.INTERNAL_SERVER_ERROR,
                error = ErrorResponse("Failed to send get user rent request. ${ex.message}")
            )
        }
    }

    fun getRentalsByUser(getUserRentsRq: GetUserRentsRq): ApiResponse<GetUserRentsRs> {
        val request = HttpEntity(getUserRentsRq, buildHeaders())
        log.debug { "Sending get user rents request. $request" }

        return try {
            val response = rentalRestTemplate.postForObject(
                rentalRestProperties.getUserRents,
                request,
                WrappedGetUserRentsRs::class.java
            )
            log.info { "Successfully received response from get user rents service. $response" }
            response!!
        } catch (ex: Exception) {
            log.warn(ex) { "Failed to send get user rents request. ${ex.message}" }
            ApiResponse(
                httpCode = HttpStatus.INTERNAL_SERVER_ERROR,
                error = ErrorResponse("Failed to send get user rents request. ${ex.message}")
            )
        }
    }

    fun changeRentalStatus(rentalStatusChangeRq: RentalStatusChangeRq): ApiResponse<RentalModel> {
        val request = HttpEntity(rentalStatusChangeRq, buildHeaders())
        log.debug { "Sending change rent status request. $request" }

        return try {
            val response = rentalRestTemplate.patchForObject(
                rentalRestProperties.changeRentStatusPath,
                request,
                WrappedChangeStatusRentRs::class.java
            )
            log.info { "Successfully received response from change rent status service. $response" }
            response!!
        } catch (ex: Exception) {
            log.warn(ex) { "Failed to send change rent status request. ${ex.message}" }
            ApiResponse(
                httpCode = HttpStatus.INTERNAL_SERVER_ERROR,
                error = ErrorResponse("Failed to send change rent status request. ${ex.message}")
            )
        }
    }

    private fun buildHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.contentType = MediaType("application", "json", StandardCharsets.UTF_8)
        return headers
    }
}