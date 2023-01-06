package ru.bmstu.dvasev.rsoi.microservices.gateway.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

@EnableRetry
@Configuration
class RetryConfiguration {

    @Bean("paymentCancelRetryTemplate")
    fun paymentCancelRetryTemplate(): RetryTemplate {
        val retryTemplate = RetryTemplate()

        val fixedBackOffPolicy = FixedBackOffPolicy()
        fixedBackOffPolicy.backOffPeriod = 10000L
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy)

        val retryPolicy = SimpleRetryPolicy()
        retryPolicy.maxAttempts = Integer.MAX_VALUE
        retryTemplate.setRetryPolicy(retryPolicy)

        return retryTemplate
    }

    @Bean("rentCancelRetryTemplate")
    fun rentCancelRetryTemplate(): RetryTemplate {
        val retryTemplate = RetryTemplate()

        val fixedBackOffPolicy = FixedBackOffPolicy()
        fixedBackOffPolicy.backOffPeriod = 10000L
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy)

        val retryPolicy = SimpleRetryPolicy()
        retryPolicy.maxAttempts = Integer.MAX_VALUE
        retryTemplate.setRetryPolicy(retryPolicy)
        return retryTemplate
    }
}
