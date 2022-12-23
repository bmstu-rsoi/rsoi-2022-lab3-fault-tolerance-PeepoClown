package ru.bmstu.dvasev.rsoi.microservices.cars.storage.car.entity

import ru.bmstu.dvasev.rsoi.microservices.cars.model.CarType
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "cars")
@Table(schema = "public", name = "cars")
data class Car(

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Column(name = "car_uid", nullable = false, unique = true)
    val carUid: UUID? = null,

    @Column(name = "brand", nullable = false)
    val brand: String? = null,

    @Column(name = "model", nullable = false)
    val model: String? = null,

    @Column(name = "registration_number", nullable = false)
    val registrationNumber: String? = null,

    @Column(name = "power")
    val power: Int? = null,

    @Column(name = "price", nullable = false)
    val price: Int? = null,

    @Enumerated(STRING)
    @Column(name = "type", nullable = false)
    val type: CarType? = null,

    @Column(name = "availability", nullable = false)
    val availability: Boolean? = null
)
