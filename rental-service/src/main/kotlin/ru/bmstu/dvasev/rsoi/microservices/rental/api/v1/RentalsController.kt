package ru.bmstu.dvasev.rsoi.microservices.rental.api.v1

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.bmstu.dvasev.rsoi.microservices.common.model.ApiResponse
import ru.bmstu.dvasev.rsoi.microservices.common.model.ErrorResponse
import ru.bmstu.dvasev.rsoi.microservices.rental.model.CreateRentRq
import ru.bmstu.dvasev.rsoi.microservices.rental.model.GetUserRentRq
import ru.bmstu.dvasev.rsoi.microservices.rental.model.GetUserRentsRq
import ru.bmstu.dvasev.rsoi.microservices.rental.model.GetUserRentsRs
import ru.bmstu.dvasev.rsoi.microservices.rental.model.RentalModel
import ru.bmstu.dvasev.rsoi.microservices.rental.model.RentalStatusChangeRq
import ru.bmstu.dvasev.rsoi.microservices.rental.storage.RentalService
import javax.validation.Valid

@RestController
@RequestMapping(
    path = ["api/v1/rentals"],
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class RentalsController(
    private val rentalService: RentalService
) {

    private val log = KotlinLogging.logger {}

    @PostMapping
    fun rent(@Valid @RequestBody request: CreateRentRq) : ApiResponse<RentalModel> {
        log.debug { "Received new create rent request. $request" }
        val rent = rentalService.createRent(request)
        val response = ApiResponse(
            httpCode = OK,
            response = rent
        )
        log.debug { "Successfully create rent. Response: $response" }
        return response
    }

    @PostMapping("user/get")
    fun getUserRents(@Valid @RequestBody request: GetUserRentsRq): ApiResponse<GetUserRentsRs> {
        log.debug { "Received new get user rents request. $request" }
        val rents = rentalService.findRentsByUsername(request.username!!)
        val response = ApiResponse(
            httpCode = OK,
            response = GetUserRentsRs(items = rents)
        )
        log.debug { "Successfully found user rents. Response: $response" }
        return response
    }

    @PostMapping("user/find")
    fun findUserRent(@Valid @RequestBody request: GetUserRentRq): ApiResponse<RentalModel> {
        log.debug { "Received new get user rent request. $request" }
        val foundRent = rentalService.findRentsByUsernameAndRentalUid(request)
        if (foundRent.isEmpty) {
            log.warn { "Failed to find rent with username ${request.username} and uid ${request.rentalUid}" }
            return ApiResponse(
                httpCode = NOT_FOUND,
                error = ErrorResponse("Rent with uid ${request.rentalUid} not found for user ${request.username}")
            )
        }
        val response = ApiResponse(
            httpCode = OK,
            response = foundRent.get()
        )
        log.debug { "Successfully found rent by username ${request.username} and uid ${request.rentalUid}. Response: $response" }
        return response
    }

    @PatchMapping("status")
    fun changeRentalStatus(@Valid @RequestBody rentalStatusChangeRq: RentalStatusChangeRq): ApiResponse<RentalModel> {
        log.debug { "Received new change rent status request. $rentalStatusChangeRq" }
        val updatedRental = rentalService.changeRentStatus(rentalStatusChangeRq)
        if (updatedRental.isEmpty) {
            log.warn { "Failed to find rental with uid ${rentalStatusChangeRq.rentalUid}" }
            return ApiResponse(
                httpCode = NOT_FOUND,
                error = ErrorResponse("Rental with uid ${rentalStatusChangeRq.rentalUid} not found")
            )
        }
        val response = ApiResponse(
            httpCode = HttpStatus.NO_CONTENT,
            response = updatedRental.get()
        )
        log.debug { "Successfully cancel rental. Response: $response" }
        return response
    }
}
