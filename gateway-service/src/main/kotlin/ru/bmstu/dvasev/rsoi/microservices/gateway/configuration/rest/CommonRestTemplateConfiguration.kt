package ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest

import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory
import ru.bmstu.dvasev.rsoi.microservices.gateway.configuration.rest.properties.CommonRestTemplateProperties

@Configuration
class CommonRestTemplateConfiguration(
    private val restTemplateBuilder: RestTemplateBuilder
) {

    fun buildRestTemplate(properties: CommonRestTemplateProperties): RestTemplate {
        return this.restTemplateBuilder.requestFactory {
            requestFactory(
                properties
            )
        }.uriTemplateHandler(uriBuilderFactory(properties.baseUrl))
            .setConnectTimeout(properties.connectionTimeout).setReadTimeout(properties.readTimeout).build()
    }

    private fun requestFactory(properties: CommonRestTemplateProperties): HttpComponentsClientHttpRequestFactory {
        val httpClient: CloseableHttpClient = HttpClientBuilder.create()
            .setConnectionManager(connectionManager(properties))
            .build()
        val requestFactory = HttpComponentsClientHttpRequestFactory()
        requestFactory.httpClient = httpClient
        return requestFactory
    }

    private fun connectionManager(properties: CommonRestTemplateProperties): PoolingHttpClientConnectionManager {
        val connectionManager = PoolingHttpClientConnectionManager()
        connectionManager.maxTotal = properties.maxThreads
        connectionManager.defaultMaxPerRoute = properties.maxThreads
        return connectionManager
    }

    private fun uriBuilderFactory(baseUrl: String): DefaultUriBuilderFactory {
        return DefaultUriBuilderFactory(baseUrl)
    }
}
