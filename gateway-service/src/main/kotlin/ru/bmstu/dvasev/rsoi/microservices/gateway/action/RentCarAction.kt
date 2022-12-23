package ru.bmstu.dvasev.rsoi.microservices.gateway.action

import mu.KotlinLogging
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.bmstu.dvasev.rsoi.microservices.cars.model.CarModel
import ru.bmstu.dvasev.rsoi.microservices.common.model.ApiResponse
import ru.bmstu.dvasev.rsoi.microservices.common.model.ErrorResponse
import ru.bmstu.dvasev.rsoi.microservices.gateway.external.CarsServiceSender
import ru.bmstu.dvasev.rsoi.microservices.gateway.external.PaymentsServiceSender
import ru.bmstu.dvasev.rsoi.microservices.gateway.external.RentalServiceSender
import ru.bmstu.dvasev.rsoi.microservices.gateway.model.CreateRentalRequestWithUser
import ru.bmstu.dvasev.rsoi.microservices.gateway.model.CreateRentalResponse
import ru.bmstu.dvasev.rsoi.microservices.gateway.model.PaymentInfo
import ru.bmstu.dvasev.rsoi.microservices.payment.model.PaymentModel
import ru.bmstu.dvasev.rsoi.microservices.rental.model.CreateRentRq
import java.time.Period.between
import java.time.format.DateTimeFormatter.ISO_DATE
import java.util.Objects.nonNull

@Service
class RentCarAction(
    private val carsServiceSender: CarsServiceSender,
    private val paymentsServiceSender: PaymentsServiceSender,
    private val rentalServiceSender: RentalServiceSender
) : Action<CreateRentalRequestWithUser, ResponseEntity<*>> {

    private val log = KotlinLogging.logger {}

    override fun process(request: CreateRentalRequestWithUser): ResponseEntity<*> {
        val carResponse = carsServiceSender.findCarByUid(carUid = request.carUid)
        if (nonNull(carResponse.error)) {
            val errorMessage = carResponse.error
            log.error { "Failed to get car with uid ${request.carUid}" }
            return ResponseEntity(
                errorMessage,
                carResponse.httpCode
            )
        }
        return rentCar(request, carResponse.response!!)
    }

    private fun rentCar(request: CreateRentalRequestWithUser, car: CarModel): ResponseEntity<*> {
        if (!car.available) {
            return ResponseEntity(
                ErrorResponse("Car with uid ${car.carUid} is not available"),
                CONFLICT
            )
        }
        reserveCar(car)?.let {
            return it
        }

        val paymentResponse = createPayment(request, car)
        if (nonNull(paymentResponse.error)) {
            val errorMessage = paymentResponse.error
            log.error { "Failed to create payment for car with uid ${car.carUid}" }
            return ResponseEntity(
                errorMessage,
                paymentResponse.httpCode
            )
        }

        return createRentAndSendResponse(request, car, paymentResponse.response!!)
    }

    private fun reserveCar(car: CarModel): ResponseEntity<*>? {
        val reservedCar = carsServiceSender.reserveCar(car.carUid)
        if (nonNull(reservedCar.error)) {
            val errorMessage = reservedCar.error
            log.error { "Failed to reserve car with uid ${car.carUid}" }
            return ResponseEntity(
                errorMessage,
                INTERNAL_SERVER_ERROR
            )
        }
        return null
    }

    private fun createPayment(request: CreateRentalRequestWithUser, car: CarModel): ApiResponse<PaymentModel> {
        val dateInterval = between(request.dateFrom, request.dateTo).days
        val totalPrice = car.price * dateInterval
        return paymentsServiceSender.createPayment(totalPrice)
    }

    private fun createRentAndSendResponse(
        request: CreateRentalRequestWithUser,
        car: CarModel,
        payment: PaymentModel
    ): ResponseEntity<*> {
        val createRentRq = CreateRentRq(
            username = request.username,
            carUid = car.carUid,
            paymentUid = payment.paymentUid,
            dateFrom = request.dateFrom,
            dateTo = request.dateTo
        )
        val rentalResponse = rentalServiceSender.createRental(createRentRq)
        if (nonNull(rentalResponse.error)) {
            val errorMessage = rentalResponse.error
            log.error { "Failed to create rent for payment with uid ${payment.paymentUid}" }
            return ResponseEntity(
                errorMessage,
                INTERNAL_SERVER_ERROR
            )
        }

        val response = CreateRentalResponse(
            rentalUid = rentalResponse.response!!.rentalUid,
            status = rentalResponse.response!!.status,
            carUid = rentalResponse.response!!.carUid,
            dateFrom = rentalResponse.response!!.dateFrom.format(ISO_DATE),
            dateTo = rentalResponse.response!!.dateTo.format(ISO_DATE),
            payment = PaymentInfo(
                paymentUid = payment.paymentUid,
                status = payment.status,
                price = payment.price
            )
        )
        log.info { "Successfully rent car with uid ${request.carUid}. $response" }
        return ResponseEntity(
            response,
            OK
        )
    }
}
