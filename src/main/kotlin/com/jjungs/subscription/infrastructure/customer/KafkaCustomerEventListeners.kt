package com.jjungs.subscription.infrastructure.customer

import com.jjungs.subscription.domain.customer.*
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaCustomerEventListeners {
    private val logger = LoggerFactory.getLogger(KafkaCustomerEventListeners::class.java)

    @KafkaListener(topics = ["customer-events"], containerFactory = "customerKafkaListenerContainerFactory")
    fun handle(event: CustomerDomainEvent) {
        when (event) {
            is CustomerCreated -> handle(event)
            is CustomerActivated -> handle(event)
            is CustomerSuspended -> handle(event)
            is CustomerDeactivated -> handle(event)
            is CustomerEmailUpdated -> handle(event)
        }
    }

    fun handle(event: CustomerCreated) {
        logger.info("Received CustomerCreated event from Kafka: customerId=${event.customerId}, email=${event.email}, occurredAt=${event.occurredAt}")

        // Here you could:
        // - Send welcome email
        // - Create customer profile in external systems
        // - Initialize customer settings
        // - Update CRM systems
        // - Send to analytics systems

        try {
            // Example: Send welcome email
            sendWelcomeEmail(event.email.value)

            // Example: Create customer profile
            createCustomerProfile(event.customerId, event.email.value)

            logger.info("Successfully processed CustomerCreated event for customerId=${event.customerId}")
        } catch (e: Exception) {
            logger.error("Failed to process CustomerCreated event for customerId=${event.customerId}", e)
            // In a real application, you might want to send to a dead letter queue
        }
    }

    fun handle(event: CustomerActivated) {
        logger.info("Received CustomerActivated event from Kafka: customerId=${event.customerId}, email=${event.email}, occurredAt=${event.occurredAt}")

        // Here you could:
        // - Send activation confirmation email
        // - Enable customer features in external systems
        // - Update customer status in CRM
        // - Initialize billing account

        try {
            sendActivationConfirmation(event.email.value)
            enableCustomerFeatures(event.customerId)

            logger.info("Successfully processed CustomerActivated event for customerId=${event.customerId}")
        } catch (e: Exception) {
            logger.error("Failed to process CustomerActivated event for customerId=${event.customerId}", e)
        }
    }

    fun handle(event: CustomerSuspended) {
        logger.info("Received CustomerSuspended event from Kafka: customerId=${event.customerId}, email=${event.email}, occurredAt=${event.occurredAt}")

        // Here you could:
        // - Send suspension notification email
        // - Disable customer features
        // - Log suspension reason
        // - Notify support team
        // - Update external systems

        try {
            sendSuspensionNotification(event.email.value)
            disableCustomerFeatures(event.customerId)

            logger.info("Successfully processed CustomerSuspended event for customerId=${event.customerId}")
        } catch (e: Exception) {
            logger.error("Failed to process CustomerSuspended event for customerId=${event.customerId}", e)
        }
    }

    fun handle(event: CustomerDeactivated) {
        logger.info("Received CustomerDeactivated event from Kafka: customerId=${event.customerId}, email=${event.email}, occurredAt=${event.occurredAt}")

        // Here you could:
        // - Send deactivation confirmation email
        // - Archive customer data
        // - Update external systems
        // - Trigger data retention policies
        // - Update CRM systems

        try {
            sendDeactivationConfirmation(event.email.value)
            archiveCustomerData(event.customerId)

            logger.info("Successfully processed CustomerDeactivated event for customerId=${event.customerId}")
        } catch (e: Exception) {
            logger.error("Failed to process CustomerDeactivated event for customerId=${event.customerId}", e)
        }
    }

    fun handle(event: CustomerEmailUpdated) {
        logger.info("Received CustomerEmailUpdated event from Kafka: customerId=${event.customerId}, oldEmail=${event.oldEmail}, newEmail=${event.newEmail}, occurredAt=${event.occurredAt}")

        // Here you could:
        // - Send email change confirmation to old email
        // - Send email change notification to new email
        // - Update external systems with new email
        // - Log email change for audit purposes
        // - Update CRM systems

        try {
            sendEmailChangeConfirmation(event.oldEmail.value)
            sendEmailChangeNotification(event.newEmail.value)
            updateExternalSystems(event.customerId, event.newEmail.value)

            logger.info("Successfully processed CustomerEmailUpdated event for customerId=${event.customerId}")
        } catch (e: Exception) {
            logger.error("Failed to process CustomerEmailUpdated event for customerId=${event.customerId}", e)
        }
    }

    // Helper methods (these would be implemented with actual services)
    private fun sendWelcomeEmail(email: String) {
        logger.info("Sending welcome email to: $email")
        // Implementation would call email service
    }

    private fun createCustomerProfile(customerId: CustomerId, email: String) {
        logger.info("Creating customer profile for customerId=${customerId.id}, email=$email")
        // Implementation would call profile service
    }

    private fun sendActivationConfirmation(email: String) {
        logger.info("Sending activation confirmation to: $email")
        // Implementation would call email service
    }

    private fun enableCustomerFeatures(customerId: CustomerId) {
        logger.info("Enabling customer features for customerId=${customerId.id}")
        // Implementation would call feature service
    }

    private fun sendSuspensionNotification(email: String) {
        logger.info("Sending suspension notification to: $email")
        // Implementation would call email service
    }

    private fun disableCustomerFeatures(customerId: CustomerId) {
        logger.info("Disabling customer features for customerId=${customerId.id}")
        // Implementation would call feature service
    }

    private fun sendDeactivationConfirmation(email: String) {
        logger.info("Sending deactivation confirmation to: $email")
        // Implementation would call email service
    }

    private fun archiveCustomerData(customerId: CustomerId) {
        logger.info("Archiving customer data for customerId=${customerId.id}")
        // Implementation would call archive service
    }

    private fun sendEmailChangeConfirmation(oldEmail: String) {
        logger.info("Sending email change confirmation to old email: $oldEmail")
        // Implementation would call email service
    }

    private fun sendEmailChangeNotification(newEmail: String) {
        logger.info("Sending email change notification to new email: $newEmail")
        // Implementation would call email service
    }

    private fun updateExternalSystems(customerId: CustomerId, newEmail: String) {
        logger.info("Updating external systems for customerId=${customerId.id} with new email: $newEmail")
        // Implementation would call external system services
    }
}
