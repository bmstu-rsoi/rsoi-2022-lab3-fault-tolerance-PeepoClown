package ru.bmstu.dvasev.rsoi.microservices.gateway.action

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.bmstu.dvasev.rsoi.microservices.common.model.ErrorResponse
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
    private val rentalServiceSender: RentalServiceSender
) {

    fun cancelRent(username: String, rentalUid: String): ResponseEntity<*> {
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
        val payment = paymentsServiceSender.findPaymentByUid(rental.response!!.paymentUid).response!!

        carsServiceSender.unreserveCar(car.carUid)
        val rentalStatusChangeRq = RentalStatusChangeRq(
            rentalUid = rentalUid,
            status = RentStatus.CANCELED
        )
        rentalServiceSender.changeRentalStatus(rentalStatusChangeRq)
        paymentsServiceSender.cancelPayment(payment.paymentUid)
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
}
