package com.jjungs.subscription.application.notification

import com.jjungs.subscription.domain.notification.NotificationPort
import com.jjungs.subscription.domain.notification.NotificationType
import org.springframework.stereotype.Component

@Component
class NotificationAdapterFactory(
    private val adapters: Map<String, NotificationPort>,
) {
    fun getAdapter(notificationType: NotificationType): NotificationPort =
        adapters["${notificationType.name.lowercase()}NotificationAdapter"]
            ?: throw IllegalArgumentException("No adapter found for notification type: ${notificationType.name.lowercase()}NotificationAdapter")
}
