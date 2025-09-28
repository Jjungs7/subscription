package com.jjungs.subscription.infrastructure.customer

import com.jjungs.subscription.domain.customer.*
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

/**
 * Kafka-based adapter for CustomerEventPublisher.
 * This adapter implements the CustomerEventPublisher port by publishing events
 * to Kafka topics for external system consumption.
 */
@Component
class CustomerKafkaEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
) : CustomerEventPublisher {

    private val logger = LoggerFactory.getLogger(CustomerKafkaEventPublisher::class.java)
    private val topicName = "customer-events"

    override fun publish(event: CustomerCreated) {
        try {
            logger.info("Publishing CustomerCreated event to Kafka: customerId=${event.customerId}, email=${event.email}")
            kafkaTemplate.send(topicName, event.customerId.value, event)
        } catch (e: Exception) {
            logger.error("Failed to publish CustomerCreated event to Kafka", e)
            throw e
        }
    }

    override fun publish(event: CustomerActivated) {
        try {
            logger.info("Publishing CustomerActivated event to Kafka: customerId=${event.customerId}, email=${event.email}")
            kafkaTemplate.send(topicName, event.customerId.value, event)
        } catch (e: Exception) {
            logger.error("Failed to publish CustomerActivated event to Kafka", e)
            throw e
        }
    }

    override fun publish(event: CustomerSuspended) {
        try {
            logger.info("Publishing CustomerSuspended event to Kafka: customerId=${event.customerId}, email=${event.email}")
            kafkaTemplate.send(topicName, event.customerId.value, event)
        } catch (e: Exception) {
            logger.error("Failed to publish CustomerSuspended event to Kafka", e)
            throw e
        }
    }

    override fun publish(event: CustomerDeactivated) {
        try {
            logger.info("Publishing CustomerDeactivated event to Kafka: customerId=${event.customerId}, email=${event.email}")
            kafkaTemplate.send(topicName, event.customerId.value, event)
        } catch (e: Exception) {
            logger.error("Failed to publish CustomerDeactivated event to Kafka", e)
            throw e
        }
    }

    override fun publish(event: CustomerEmailUpdated) {
        try {
            logger.info("Publishing CustomerEmailUpdated event to Kafka: customerId=${event.customerId}, oldEmail=${event.oldEmail}, newEmail=${event.newEmail}")
            kafkaTemplate.send(topicName, event.customerId.value, event)
        } catch (e: Exception) {
            logger.error("Failed to publish CustomerEmailUpdated event to Kafka", e)
            throw e
        }
    }
}
