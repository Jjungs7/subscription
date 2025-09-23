package com.jjungs.subscription.domain.notification

import java.time.LocalDateTime
import java.util.*

class Notification(
    val recipient: String,
    val subject: String,
    val message: String,
    val type: NotificationType,
    val timestamp: LocalDateTime,
) {
    val id = UUID.randomUUID().toString()
    var status = NotificationStatus.PENDING
        private set

    fun markAsSent() {
        status = NotificationStatus.SENT
    }

    fun markAsFailed() {
        status = NotificationStatus.FAILED
    }
}
