package ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest.properties

import java.time.Duration

interface CommonRestTemplateProperties {
    val baseUrl: String
    val connectionTimeout: Duration
    val readTimeout: Duration
    val maxThreads: Int
}
