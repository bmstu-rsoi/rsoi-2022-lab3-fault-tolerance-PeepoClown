package ru.bmstu.dvasev.rsoi.microservices.payment.storage.payment

import org.springframework.stereotype.Service
import ru.bmstu.dvasev.rsoi.microservices.payment.model.PaymentModel
import ru.bmstu.dvasev.rsoi.microservices.payment.model.PaymentStatus
import ru.bmstu.dvasev.rsoi.microservices.payment.model.PaymentStatus.CANCELED
import ru.bmstu.dvasev.rsoi.microservices.payment.model.PaymentStatus.PAID
import ru.bmstu.dvasev.rsoi.microservices.payment.storage.payment.dao.PaymentRepository
import ru.bmstu.dvasev.rsoi.microservices.payment.storage.payment.entity.Payment
import java.util.Optional
import java.util.UUID.fromString
import java.util.UUID.randomUUID

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository
) {

    fun createPayment(price: Int): PaymentModel {
        val payment = Payment(
            paymentUid = randomUUID(),
            status = PAID,
            price = price
        )
        val savedPayment = paymentRepository.save(payment)
        return toPaymentModel(savedPayment)
    }

    fun cancelPayment(paymentUid: String): Optional<PaymentModel> {
        return paymentRepository
            .findByPaymentUid(fromString(paymentUid))
            .map { updatePaymentStatusByUid(it, CANCELED) }
            .map { toPaymentModel(it) }
    }

    fun findPaymentByUid(paymentUid: String): Optional<PaymentModel> {
        return paymentRepository
            .findByPaymentUid(fromString(paymentUid))
            .map { toPaymentModel(it) }
    }

    private fun updatePaymentStatusByUid(payment: Payment, status: PaymentStatus): Payment {
        val updatedPayment = Payment(
            id = payment.id,
            paymentUid = payment.paymentUid,
            status = status,
            price = payment.price
        )
        return paymentRepository.save(updatedPayment)
    }

    private fun toPaymentModel(payment: Payment) =
        PaymentModel(
            paymentUid = payment.paymentUid.toString(),
            status = payment.status!!,
            price = payment.price!!
        )
}
