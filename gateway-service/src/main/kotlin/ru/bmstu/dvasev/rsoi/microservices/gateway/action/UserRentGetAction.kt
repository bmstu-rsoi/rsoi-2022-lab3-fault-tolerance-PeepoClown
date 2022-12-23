package ru.bmstu.dvasev.rsoi.microservices.gateway.action

import mu.KotlinLogging
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.bmstu.dvasev.rsoi.microservices.gateway.external.CarsServiceSender
import ru.bmstu.dvasev.rsoi.microservices.gateway.external.PaymentsServiceSender
import ru.bmstu.dvasev.rsoi.microservices.gateway.external.RentalServiceSender
import ru.bmstu.dvasev.rsoi.microservices.gateway.model.CarResponseModel
import ru.bmstu.dvasev.rsoi.microservices.gateway.model.PaymentResponseModel
import ru.bmstu.dvasev.rsoi.microservices.gateway.model.RentalResponseModel
import ru.bmstu.dvasev.rsoi.microservices.rental.model.GetUserRentRq
import ru.bmstu.dvasev.rsoi.microservices.rental.model.GetUserRentsRq
import ru.bmstu.dvasev.rsoi.microservices.rental.model.RentalModel
import java.time.format.DateTimeFormatter.ISO_DATE
import java.util.Objects.nonNull

@Service
class UserRentGetAction(
    private val rentalServiceSender: RentalServiceSender,
    private val carsServiceSender: CarsServiceSender,
    private val paymentsServiceSender: PaymentsServiceSender
) {
    private val log = KotlinLogging.logger {}

    fun getUserRent(username: String, rentalUid: String): ResponseEntity<*> {
        val getUserRentRq = GetUserRentRq(
            username = username,
            rentalUid = rentalUid
        )
        val rentalResponse = rentalServiceSender.getRentalByUserAndUid(getUserRentRq)
        if (nonNull(rentalResponse.error)) {
            val errorMessage = rentalResponse.error
            log.error { "Failed to get user rent. ${errorMessage.toString()}" }
            return ResponseEntity(
                errorMessage,
                rentalResponse.httpCode
            )
        }
        val response = ResponseEntity(
            toRentalResponseModel(rentalResponse.response!!),
            OK
        )
        log.info { "Successful get user rent request. Response: $response" }
        return response
    }

    fun getUserRents(username: String): ResponseEntity<*> {
        val getUserRentsRq = GetUserRentsRq(
            username = username
        )
        val rentalResponse = rentalServiceSender.getRentalsByUser(getUserRentsRq)
        if (nonNull(rentalResponse.error)) {
            val errorMessage = rentalResponse.error
            log.error { "Failed to get user rents. ${errorMessage.toString()}" }
            return ResponseEntity(
                errorMessage,
                rentalResponse.httpCode
            )
        }
        val rentals = rentalResponse.response!!.items
            .map { toRentalResponseModel(it) }
        val response = ResponseEntity(
            rentals,
            OK
        )
        log.info { "Successful get user rents request. Response: $response" }
        return response
    }

    private fun toRentalResponseModel(rental: RentalModel): RentalResponseModel {
        val car = carsServiceSender.findCarByUid(rental.carUid).response!!
        val payment = paymentsServiceSender.findPaymentByUid(rental.paymentUid).response!!
        return RentalResponseModel(
            rentalUid = rental.rentalUid,
            status = rental.status,
            dateFrom = rental.dateFrom.format(ISO_DATE),
            dateTo = rental.dateTo.format(ISO_DATE),
            car = CarResponseModel(
                carUid = car.carUid,
                brand = car.brand,
                model = car.model,
                registrationNumber = car.registrationNumber
            ),
            payment = PaymentResponseModel(
                paymentUid = payment.paymentUid,
                status = payment.status,
                price = payment.price
            )
        )
    }
}
