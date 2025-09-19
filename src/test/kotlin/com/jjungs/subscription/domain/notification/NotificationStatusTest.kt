package com.jjungs.subscription.domain.notification

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class NotificationStatusTest {
    @Test
    fun `NotificationStatus 에 PENDING, SENT, FAILED 가 포함되어야함`() {
        assertEquals("PENDING", NotificationStatus.PENDING.name)
        assertEquals("SENT", NotificationStatus.SENT.name)
        assertEquals("FAILED", NotificationStatus.FAILED.name)
    }

    @Test
    fun `NotificationStatus should be enum`() {
        assertTrue(NotificationStatus.entries.toTypedArray().isNotEmpty())
        assertEquals(3, NotificationStatus.entries.size)
    }
}
