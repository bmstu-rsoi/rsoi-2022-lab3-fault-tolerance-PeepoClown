package ru.bmstu.dvasev.rsoi.microservices.payment.api.v1.handler

import mu.KotlinLogging
import org.apache.commons.lang3.StringUtils.capitalize
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import ru.bmstu.dvasev.rsoi.microservices.common.model.ApiResponse
import ru.bmstu.dvasev.rsoi.microservices.common.model.ErrorResponse
import ru.bmstu.dvasev.rsoi.microservices.common.model.ValidationErrorDescription
import ru.bmstu.dvasev.rsoi.microservices.common.model.ValidationErrorResponse

@RestControllerAdvice
class PaymentsExceptionHandler {

    private val log = KotlinLogging.logger {}

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException, request: WebRequest): ApiResponse<Any> {
        log.warn(ex) { "Validation error: ${ex.message}" }
        val error = ValidationErrorResponse(
            message = "${capitalize(ex.bindingResult.objectName)} is invalid",
            errors = ex.bindingResult.fieldErrors.map { err ->
                ValidationErrorDescription(
                    err.field,
                    err.defaultMessage.orEmpty()
                )
            }
        )
        return ApiResponse(
            httpCode = BAD_REQUEST,
            error = error
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleInternalError(ex: Exception, request: WebRequest): ApiResponse<Any> {
        log.error(ex) { "Internal server error: ${ex.message}" }
        val error = ErrorResponse(
            message = "Internal error. ${ex.message}"
        )
        return ApiResponse(
            httpCode = INTERNAL_SERVER_ERROR,
            error = error
        )
    }
}
