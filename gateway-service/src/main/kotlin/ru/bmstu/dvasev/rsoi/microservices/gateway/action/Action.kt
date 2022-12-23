package ru.bmstu.dvasev.rsoi.microservices.gateway.action

interface Action<Rq, Rs> {
    fun process(request: Rq): Rs
}
