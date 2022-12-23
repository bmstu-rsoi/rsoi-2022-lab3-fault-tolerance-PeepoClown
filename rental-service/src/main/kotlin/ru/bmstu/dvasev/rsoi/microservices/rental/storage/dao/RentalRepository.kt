package ru.bmstu.dvasev.rsoi.microservices.rental.storage.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import ru.bmstu.dvasev.rsoi.microservices.rental.storage.entity.Rental
import java.util.Optional
import java.util.UUID

@Repository
interface RentalRepository : JpaRepository<Rental, Long> {

    @Query("select r from rental r where r.rentalUid = :rentalUid")
    fun findByRentalUid(@Param("rentalUid") rentalUid: UUID): Optional<Rental>

    @Query("select r from rental r where r.username = :username")
    fun findAllByUsername(@Param("username") username: String): List<Rental>

    @Query("select r from rental r where r.username = :username and r.rentalUid = :rentalUid")
    fun findByUsernameAndRentalUid(
        @Param("username") username: String,
        @Param("rentalUid") rentalUid: UUID
    ): Optional<Rental>
}
