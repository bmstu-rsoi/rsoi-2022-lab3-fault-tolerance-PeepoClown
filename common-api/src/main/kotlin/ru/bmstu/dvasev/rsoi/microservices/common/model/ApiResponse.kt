package ru.bmstu.dvasev.rsoi.microservices.common.model

import org.springframework.http.HttpStatus

open class ApiResponse<T>(
    val httpCode: HttpStatus,
    val response: T? = null,
    val error: Error? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ApiResponse<*>) return false

        if (httpCode != other.httpCode) return false
        if (response != other.response) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        var result = httpCode.hashCode()
        result = 31 * result + (response?.hashCode() ?: 0)
        result = 31 * result + (error?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "ApiResponse(httpCode=$httpCode, response=$response, error=$error)"
    }
}
