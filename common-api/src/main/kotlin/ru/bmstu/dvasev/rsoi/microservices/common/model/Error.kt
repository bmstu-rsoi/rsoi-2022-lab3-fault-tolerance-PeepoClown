package ru.bmstu.dvasev.rsoi.microservices.common.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME

@JsonTypeInfo(use = NAME, property = "type")
@JsonSubTypes(
    Type(value = ErrorResponse::class),
    Type(value = ValidationErrorResponse::class)
)
interface Error {
    val message: String
}
