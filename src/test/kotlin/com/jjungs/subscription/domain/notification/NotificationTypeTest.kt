package com.jjungs.subscription.domain.notification

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class NotificationTypeTest {
    @Test
    fun `NotificationType 에 EMAIL, SMS, PUSH 이 있어야함`() {
        assertEquals("EMAIL", NotificationType.EMAIL.name)
        assertEquals("SMS", NotificationType.SMS.name)
        assertEquals("PUSH", NotificationType.PUSH.name)
    }

    @Test
    fun `NotificationType 은 enum 타입이어야함`() {
        assertTrue(NotificationType.entries.toTypedArray().isNotEmpty())
        assertEquals(3, NotificationType.entries.size)
    }
}