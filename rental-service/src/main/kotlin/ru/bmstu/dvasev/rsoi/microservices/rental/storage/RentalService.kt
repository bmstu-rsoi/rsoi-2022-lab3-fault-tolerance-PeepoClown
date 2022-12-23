package ru.bmstu.dvasev.rsoi.microservices.rental.storage

import org.springframework.stereotype.Service
import ru.bmstu.dvasev.rsoi.microservices.rental.model.CreateRentRq
import ru.bmstu.dvasev.rsoi.microservices.rental.model.GetUserRentRq
import ru.bmstu.dvasev.rsoi.microservices.rental.model.RentStatus
import ru.bmstu.dvasev.rsoi.microservices.rental.model.RentStatus.IN_PROGRESS
import ru.bmstu.dvasev.rsoi.microservices.rental.model.RentalModel
import ru.bmstu.dvasev.rsoi.microservices.rental.model.RentalStatusChangeRq
import ru.bmstu.dvasev.rsoi.microservices.rental.storage.dao.RentalRepository
import ru.bmstu.dvasev.rsoi.microservices.rental.storage.entity.Rental
import java.util.*
import java.util.UUID.fromString
import java.util.UUID.randomUUID

@Service
class RentalService(
    private val rentalRepository: RentalRepository
) {

    fun createRent(createRentRq: CreateRentRq): RentalModel {
        val rent = Rental(
            rentalUid = randomUUID(),
            username = createRentRq.username,
            paymentUid = fromString(createRentRq.paymentUid),
            carUid = fromString(createRentRq.carUid),
            dateFrom = createRentRq.dateFrom,
            dateTo = createRentRq.dateTo,
            status = IN_PROGRESS
        )
        val savedRent = rentalRepository.save(rent)
        return toRentalModel(savedRent)
    }

    fun findRentsByUsername(username: String): List<RentalModel> {
        return rentalRepository
            .findAllByUsername(username)
            .map { toRentalModel(it) }
    }

    fun findRentsByUsernameAndRentalUid(getUserRentRq: GetUserRentRq): Optional<RentalModel> {
        return rentalRepository
            .findByUsernameAndRentalUid(getUserRentRq.username!!, fromString(getUserRentRq.rentalUid!!))
            .map { toRentalModel(it) }
    }

    fun changeRentStatus(rentalStatusChangeRq: RentalStatusChangeRq): Optional<RentalModel> {
        return rentalRepository
            .findByRentalUid(rentalUid = fromString(rentalStatusChangeRq.rentalUid!!))
            .map { updateRentalStatusByUid(it, rentalStatusChangeRq.status!!) }
            .map { toRentalModel(it) }
    }

    private fun updateRentalStatusByUid(rental: Rental, status: RentStatus): Rental {
        val updatedRental = Rental(
            id = rental.id,
            rentalUid = rental.rentalUid,
            username = rental.username,
            paymentUid = rental.paymentUid,
            carUid = rental.carUid,
            dateFrom = rental.dateFrom,
            dateTo = rental.dateTo,
            status = status
        )
        return rentalRepository.save(updatedRental)
    }

    private fun toRentalModel(rental: Rental) =
        RentalModel(
            rentalUid = rental.rentalUid.toString(),
            username = rental.username!!,
            paymentUid = rental.paymentUid.toString(),
            carUid = rental.carUid.toString(),
            dateFrom = rental.dateFrom!!,
            dateTo = rental.dateTo!!,
            status = rental.status!!
        )
}
