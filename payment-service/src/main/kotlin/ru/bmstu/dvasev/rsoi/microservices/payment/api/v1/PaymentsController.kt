package ru.bmstu.dvasev.rsoi.microservices.payment.api.v1

import mu.KotlinLogging
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.bmstu.dvasev.rsoi.microservices.common.model.ApiResponse
import ru.bmstu.dvasev.rsoi.microservices.common.model.ErrorResponse
import ru.bmstu.dvasev.rsoi.microservices.payment.model.CancelPaymentRq
import ru.bmstu.dvasev.rsoi.microservices.payment.model.CreatePaymentRq
import ru.bmstu.dvasev.rsoi.microservices.payment.model.GetPaymentByUidRq
import ru.bmstu.dvasev.rsoi.microservices.payment.model.PaymentModel
import ru.bmstu.dvasev.rsoi.microservices.payment.storage.payment.PaymentService
import javax.validation.Valid

@RestController
@RequestMapping(
    path = ["api/v1/payments"],
    consumes = [APPLICATION_JSON_VALUE],
    produces = [APPLICATION_JSON_VALUE]
)
class PaymentsController(
    private val paymentService: PaymentService
) {

    private val log = KotlinLogging.logger {}

    @PostMapping
    fun pay(@Valid @RequestBody request: CreatePaymentRq): ApiResponse<PaymentModel> {
        log.debug { "Received new create payment request. $request" }
        val payment = paymentService.createPayment(request.price!!)
        val response = ApiResponse(
            httpCode = OK,
            response = payment
        )
        log.debug { "Successfully create payment. Response: $response" }
        return response
    }

    @PostMapping("find")
    fun findPayment(@Valid @RequestBody request: GetPaymentByUidRq): ApiResponse<PaymentModel> {
        log.debug { "Received new find payment by uid request. $request" }
        val payment = paymentService.findPaymentByUid(request.paymentUid)
        if (payment.isEmpty) {
            log.warn { "Failed to find payment by uid ${request.paymentUid}" }
            return ApiResponse(
                httpCode = NOT_FOUND,
                error = ErrorResponse("Failed to find payment by uid ${request.paymentUid}")
            )
        }
        val response = ApiResponse(
            httpCode = OK,
            response = payment.get()
        )
        log.debug { "Successfully found payment by uid ${request.paymentUid}. $response" }
        return response
    }

    @PatchMapping("cancel")
    fun cancel(@Valid @RequestBody request: CancelPaymentRq): ApiResponse<PaymentModel> {
        log.debug { "Received new cancel payment request. $request" }
        val canceledPayment = paymentService.cancelPayment(request.paymentUid!!)
        if (canceledPayment.isEmpty) {
            log.warn { "Failed to find payment with uid ${request.paymentUid}" }
            return ApiResponse(
                httpCode = NOT_FOUND,
                error = ErrorResponse("Payment with uid ${request.paymentUid} not found")
            )
        }
        val response = ApiResponse(
            httpCode = NO_CONTENT,
            response = canceledPayment.get()
        )
        log.debug { "Successfully cancel payment. Response: $response" }
        return response
    }
}