package com.jjungs.subscription.infrastructure

import com.jjungs.subscription.domain.notification.Notification
import com.jjungs.subscription.domain.notification.NotificationPort
import org.springframework.stereotype.Component

@Component
class EmailNotificationAdapter : NotificationPort {
    override fun send(notification: Notification) {
        try {
            Thread.sleep(100)
            notification.markAsSent()
        } catch (e: Exception) {
            notification.markAsFailed()
            println(e.message)
            println(e.stackTrace)
        }
    }
}
