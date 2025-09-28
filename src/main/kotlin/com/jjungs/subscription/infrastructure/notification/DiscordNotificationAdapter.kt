package com.jjungs.subscription.infrastructure.notification

import com.jjungs.subscription.domain.notification.Notification
import com.jjungs.subscription.domain.notification.NotificationPort
import org.springframework.stereotype.Component

@Component
class DiscordNotificationAdapter : NotificationPort {
    override fun send(notification: Notification) {
        try {
            // Simulate Discord API call
            Thread.sleep(150)
            println("Sending Discord notification to ${notification.recipient}: ${notification.subject}")
            println("Message: ${notification.message}")
        } catch (e: Exception) {
            println("Failed to send Discord notification: ${e.message}")
            println(e.stackTrace)
        }
    }
}
