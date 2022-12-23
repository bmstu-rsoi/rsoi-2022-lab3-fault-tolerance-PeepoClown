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
import ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest.properties.PaymentRestProperties
import ru.bmstu.dvasev.rsoi.microservices.payment.model.CancelPaymentRq
import ru.bmstu.dvasev.rsoi.microservices.payment.model.CreatePaymentRq
import ru.bmstu.dvasev.rsoi.microservices.payment.model.GetPaymentByUidRq
import ru.bmstu.dvasev.rsoi.microservices.payment.model.PaymentModel
import java.nio.charset.StandardCharsets

class WrappedCreatePaymentRs(httpCode: HttpStatus) : ApiResponse<PaymentModel>(httpCode)
class WrappedFindPaymentByUid(httpCode: HttpStatus) : ApiResponse<PaymentModel>(httpCode)
class WrappedCancelPaymentByUid(httpCode: HttpStatus) : ApiResponse<PaymentModel>(httpCode)

@Service
class PaymentsServiceSender(
    private val paymentsRestTemplate: RestTemplate,
    private val paymentRestProperties: PaymentRestProperties
) {

    private val log = KotlinLogging.logger {}

    fun createPayment(price: Int): ApiResponse<PaymentModel> {
        val createPaymentRq = CreatePaymentRq(price = price)
        val request = HttpEntity(createPaymentRq, buildHeaders())
        log.debug { "Sending create payment request. $request" }

        return try {
            val response = paymentsRestTemplate.postForObject(
                paymentRestProperties.createPaymentPath,
                request,
                WrappedCreatePaymentRs::class.java
            )
            log.info { "Successfully received response from create payment service. $response" }
            response!!
        } catch (ex: Exception) {
            log.warn(ex) { "Failed to send create payment request. ${ex.message}" }
            ApiResponse(
                httpCode = HttpStatus.INTERNAL_SERVER_ERROR,
                error = ErrorResponse("Failed to send create payment request. ${ex.message}")
            )
        }
    }

    fun findPaymentByUid(paymentUid: String): ApiResponse<PaymentModel> {
        val findPaymentByUidRq = GetPaymentByUidRq(
            paymentUid = paymentUid
        )
        val request = HttpEntity(findPaymentByUidRq, buildHeaders())
        log.debug { "Sending find payment by uid request" }

        return try {
            val response = paymentsRestTemplate.postForObject(
                paymentRestProperties.findPaymentPath,
                request,
                WrappedFindPaymentByUid::class.java
            )
            log.info { "Successfully received response from get payment by uid service. $response" }
            response!!
        } catch (ex: Exception) {
            log.warn(ex) { "Failed to send get payment by uid request. ${ex.message}" }
            ApiResponse(
                httpCode = HttpStatus.INTERNAL_SERVER_ERROR,
                error = ErrorResponse("Failed to send get payment by uid request. ${ex.message}")
            )
        }
    }

    fun cancelPayment(paymentUid: String): ApiResponse<PaymentModel> {
        val cancelPaymentRq = CancelPaymentRq(paymentUid)
        val request = HttpEntity(cancelPaymentRq, buildHeaders())
        log.debug { "Sending cancel payment by uid request" }

        return try {
            val response = paymentsRestTemplate.patchForObject(
                paymentRestProperties.cancelPayment,
                request,
                WrappedCancelPaymentByUid::class.java
            )
            log.info { "Successfully received response from cancel payment by uid service. $response" }
            response!!
        } catch (ex: Exception) {
            log.warn(ex) { "Failed to send get cancel by uid request. ${ex.message}" }
            ApiResponse(
                httpCode = HttpStatus.INTERNAL_SERVER_ERROR,
                error = ErrorResponse("Failed to send get cancel by uid request. ${ex.message}")
            )
        }
    }

    private fun buildHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.contentType = MediaType("application", "json", StandardCharsets.UTF_8)
        return headers
    }
}