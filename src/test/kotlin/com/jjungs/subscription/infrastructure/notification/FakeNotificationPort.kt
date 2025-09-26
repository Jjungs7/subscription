package com.jjungs.subscription.infrastructure.notification

import com.jjungs.subscription.domain.notification.Notification
import com.jjungs.subscription.domain.notification.NotificationPort

class FakeNotificationPort : NotificationPort {
    override fun send(notification: Notification) {
        // Do nothing for testing
    }
}
