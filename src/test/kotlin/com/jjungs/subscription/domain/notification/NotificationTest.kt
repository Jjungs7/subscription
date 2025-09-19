package com.jjungs.subscription.domain.notification

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDateTime

class NotificationTest : StringSpec(
    {
        "Notification should be created with required properties" {
            val recipient = "user@example.com"
            val subject = "Test Subject"
            val message = "Test Message"
            val type = NotificationType.EMAIL
            val timestamp = LocalDateTime.now()

            val notification = Notification(recipient, subject, message, type, timestamp)

            recipient shouldBe notification.recipient
            subject shouldBe notification.subject
            message shouldBe notification.message
            type shouldBe notification.type
            NotificationStatus.PENDING shouldBe notification.status
            timestamp shouldBe notification.timestamp
        }

        "Notification should have unique identifier" {
            val notification1 = Notification(
                "user@example.com",
                "Subject 1",
                "Message 1",
                NotificationType.EMAIL,
                LocalDateTime.now(),
            )

            val notification2 = Notification(
                "user@example.com",
                "Subject 2",
                "Message 2",
                NotificationType.EMAIL,
                LocalDateTime.now(),
            )

            notification1.shouldNotBeNull()
            notification1.id shouldNotBe notification2.id
        }

        "Notification should be able to change status" {
            val notification = Notification(
                "user@example.com",
                "Test Subject",
                "Test Message",
                NotificationType.EMAIL,
                LocalDateTime.now(),
            )

            NotificationStatus.PENDING shouldBe notification.status

            notification.markAsSent()
            NotificationStatus.SENT shouldBe notification.status

            notification.markAsFailed()
            NotificationStatus.FAILED shouldBe notification.status
        }
    },
)