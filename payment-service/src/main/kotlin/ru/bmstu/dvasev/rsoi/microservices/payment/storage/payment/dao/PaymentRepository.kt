package ru.bmstu.dvasev.rsoi.microservices.payment.storage.payment.dao

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.bmstu.dvasev.rsoi.microservices.payment.storage.payment.entity.Payment
import java.util.Optional
import java.util.UUID

@Repository
interface PaymentRepository: JpaRepository<Payment, Long> {

    fun findByPaymentUid(paymentUid: UUID): Optional<Payment>
}
