package ru.bmstu.dvasev.rsoi.microservices.gateway.action

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.retry.RetryCallback
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Service
import ru.bmstu.dvasev.rsoi.microservices.common.model.ErrorResponse
import ru.bmstu.dvasev.rsoi.microservices.gateway.exception.PaymentServiceUnavailableException
import ru.bmstu.dvasev.rsoi.microservices.gateway.exception.RentalServiceUnavailableException
import ru.bmstu.dvasev.rsoi.microservices.gateway.external.CarsServiceSender
import ru.bmstu.dvasev.rsoi.microservices.gateway.external.PaymentsServiceSender
import ru.bmstu.dvasev.rsoi.microservices.gateway.external.RentalServiceSender
import ru.bmstu.dvasev.rsoi.microservices.rental.model.GetUserRentRq
import ru.bmstu.dvasev.rsoi.microservices.rental.model.RentStatus
import ru.bmstu.dvasev.rsoi.microservices.rental.model.RentalStatusChangeRq
import java.util.Objects.nonNull

@Service
class RentEndAction(
    private val carsServiceSender: CarsServiceSender,
    private val paymentsServiceSender: PaymentsServiceSender,
    private val rentalServiceSender: RentalServiceSender,
    @Qualifier("rentCancelRetryTemplate")
    private val rentCancelRetryTemplate: RetryTemplate,
    @Qualifier("paymentCancelRetryTemplate")
    private val paymentCancelRetryTemplate: RetryTemplate
) {

    fun cancelRent(username: String, rentalUid: String): ResponseEntity<*> {
        val getUserRentRq = GetUserRentRq(
            username = username,
            rentalUid = rentalUid
        )
        val rentalResponse = rentalServiceSender.getRentalByUserAndUid(getUserRentRq)

        if (nonNull(rentalResponse.error) && !rentalResponse.httpCode.is5xxServerError) {
            return ResponseEntity(
                ErrorResponse(rentalResponse.error!!.message),
                NOT_FOUND
            )
        } else if (nonNull(rentalResponse.error) && rentalResponse.httpCode.is5xxServerError) {
            return ResponseEntity(
                ErrorResponse(rentalResponse.error!!.message),
                INTERNAL_SERVER_ERROR
            )
        }
        val rental = rentalResponse.response!!

        val carResponse = carsServiceSender.findCarByUid(rental.carUid)
        if (nonNull(carResponse.error)) {
            return ResponseEntity(
                ErrorResponse(carResponse.error!!.message),
                INTERNAL_SERVER_ERROR
            )
        }
        val car = carResponse.response!!
        val unreservedCarResponse = carsServiceSender.unreserveCar(car.carUid)
        if (unreservedCarResponse.httpCode.is5xxServerError) {
            return ResponseEntity(
                ErrorResponse(unreservedCarResponse.error!!.message),
                INTERNAL_SERVER_ERROR
            )
        }

        rentCancelRetryTemplate.execute(RetryCallback<Unit, Exception> { cancelRentRetrials(rentalUid) })
        paymentCancelRetryTemplate.execute(RetryCallback<Unit, Exception> { cancelPayment(rental.paymentUid) })

        return ResponseEntity(null, NO_CONTENT)
    }

    fun finishRent(username: String, rentalUid: String): ResponseEntity<*> {
        val getUserRentRq = GetUserRentRq(
            username = username,
            rentalUid = rentalUid
        )
        val rental = rentalServiceSender.getRentalByUserAndUid(getUserRentRq)

        if (nonNull(rental.error)) {
            return ResponseEntity(
                ErrorResponse(rental.error!!.message),
                NOT_FOUND
            )
        }

        val car = carsServiceSender.findCarByUid(rental.response!!.carUid).response!!

        carsServiceSender.unreserveCar(car.carUid)
        val rentalStatusChangeRq = RentalStatusChangeRq(
            rentalUid = rentalUid,
            status = RentStatus.FINISHED
        )
        rentalServiceSender.changeRentalStatus(rentalStatusChangeRq)
        return ResponseEntity(null, NO_CONTENT)
    }

    fun cancelRentRetrials(rentalUid: String) {
        val rentalStatusChangeRq = RentalStatusChangeRq(
            rentalUid = rentalUid,
            status = RentStatus.CANCELED
        )
        val rentStatusResponse = rentalServiceSender.changeRentalStatus(rentalStatusChangeRq)
        if (rentStatusResponse.httpCode.is5xxServerError) {
            throw RentalServiceUnavailableException(rentStatusResponse.error?.message)
        }
    }

    fun cancelPayment(paymentUid: String) {
        val paymentCancelResponse = paymentsServiceSender.cancelPayment(paymentUid)
        if (paymentCancelResponse.httpCode.is5xxServerError) {
            throw PaymentServiceUnavailableException(paymentCancelResponse.error?.message)
        }
    }
}
