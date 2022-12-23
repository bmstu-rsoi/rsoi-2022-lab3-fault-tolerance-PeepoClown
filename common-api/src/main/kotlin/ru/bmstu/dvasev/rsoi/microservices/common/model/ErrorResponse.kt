package ru.bmstu.dvasev.rsoi.microservices.common.model

import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("ERROR_RESPONSE")
data class ErrorResponse(
    override val message: String
) : Error
