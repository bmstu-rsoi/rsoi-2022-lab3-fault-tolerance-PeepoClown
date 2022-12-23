package ru.bmstu.dvasev.rsoi.microservices.gateway.action

import mu.KotlinLogging
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import ru.bmstu.dvasev.rsoi.microservices.cars.model.GetCarsRq
import ru.bmstu.dvasev.rsoi.microservices.gateway.external.CarsServiceSender
import java.util.Objects.nonNull

@Service
class CarsGetAction(
    private val carsServiceSender: CarsServiceSender
) : Action<GetCarsRq, ResponseEntity<*>> {

    private val log = KotlinLogging.logger {}

    override fun process(request: GetCarsRq): ResponseEntity<*> {
        val carsResponse = carsServiceSender.getAvailableCars(request)
        if (nonNull(carsResponse.error)) {
            val errorMessage = carsResponse.error
            log.error { "Failed to get available cars. ${errorMessage.toString()}" }
            return ResponseEntity(
                errorMessage,
                carsResponse.httpCode
            )
        }
        val response = ResponseEntity(
            carsResponse.response!!,
            OK
        )
        log.info { "Successful get available cars request. Response: $response" }
        return response
    }
}
