package ru.bmstu.dvasev.rsoi.microservices.common.model

import com.fasterxml.jackson.annotation.JsonTypeName

data class ValidationErrorDescription(
    val field: String,
    val error: String
)

@JsonTypeName("VALIDATION_ERROR_RESPONSE")
data class ValidationErrorResponse(
    override val message: String,
    val errors: List<ValidationErrorDescription>
) : Error
