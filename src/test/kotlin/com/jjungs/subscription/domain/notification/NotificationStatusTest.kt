package com.jjungs.subscription.domain.notification

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldHaveSize

class NotificationStatusTest : StringSpec({
    "NotificationStatus should have PENDING, SENT, FAILED values" {
        NotificationStatus.PENDING.name shouldBe "PENDING"
        NotificationStatus.SENT.name shouldBe "SENT"
        NotificationStatus.FAILED.name shouldBe "FAILED"
    }

    "NotificationStatus should be enum with 3 entries" {
        NotificationStatus.entries.shouldHaveSize(3)
    }
})