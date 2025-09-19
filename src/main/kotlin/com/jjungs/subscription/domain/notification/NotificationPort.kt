package com.jjungs.subscription.domain.notification

interface NotificationPort {
    fun send(notification: Notification)
}
