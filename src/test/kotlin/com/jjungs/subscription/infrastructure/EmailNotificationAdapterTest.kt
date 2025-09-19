package com.jjungs.subscription.infrastructure

import com.jjungs.subscription.domain.notification.Notification
import com.jjungs.subscription.domain.notification.NotificationStatus
import com.jjungs.subscription.domain.notification.NotificationType
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class EmailNotificationAdapterTest : BehaviorSpec(
    {
        given("A notification adapter") {
            val adapter = EmailNotificationAdapter()
            val notification = Notification(
                recipient = "user@example.com",
                subject = "Test Subject",
                message = "Test Message",
                type = NotificationType.EMAIL,
                timestamp = LocalDateTime.now(),
            )

            `when`("adapter sends the notification") {
                adapter.send(notification)

                then("the notification status should be updated to SENT") {
                    notification.status shouldBe NotificationStatus.SENT
                }
            }
        }
    },
)
