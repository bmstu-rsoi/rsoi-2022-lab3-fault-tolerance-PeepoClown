package ru.bmstu.dvasev.rsoi.microservices.rental.storage.entity

import ru.bmstu.dvasev.rsoi.microservices.rental.model.RentStatus
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "rental")
@Table(schema = "public", name = "rental")
data class Rental(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Column(name = "rental_uid", nullable = false, unique = true)
    val rentalUid: UUID? = null,

    @Column(name = "username", nullable = false)
    val username: String? = null,

    @Column(name = "payment_uid", nullable = false)
    val paymentUid: UUID? = null,

    @Column(name = "car_uid", nullable = false)
    val carUid: UUID? = null,

    @Column(name = "date_from", nullable = false)
    val dateFrom: LocalDate? = null,

    @Column(name = "date_to", nullable = false)
    val dateTo: LocalDate? = null,

    @Enumerated(STRING)
    @Column(name = "status", nullable = false)
    val status: RentStatus? = null,
)
