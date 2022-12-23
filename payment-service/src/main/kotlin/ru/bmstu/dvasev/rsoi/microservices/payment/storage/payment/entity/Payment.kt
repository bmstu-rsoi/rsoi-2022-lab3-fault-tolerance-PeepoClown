package ru.bmstu.dvasev.rsoi.microservices.payment.storage.payment.entity

import ru.bmstu.dvasev.rsoi.microservices.payment.model.PaymentStatus
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "payment")
@Table(schema = "public", name = "payment")
data class Payment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @Column(name = "payment_uid", nullable = false, unique = true)
    val paymentUid: UUID? = null,

    @Enumerated(STRING)
    @Column(name = "status", nullable = false)
    val status: PaymentStatus? = null,

    @Column(name = "price", nullable = false)
    val price: Int? = null
)
