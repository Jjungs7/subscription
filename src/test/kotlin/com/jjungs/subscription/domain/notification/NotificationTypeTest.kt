package com.jjungs.subscription.domain.notification

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldHaveSize

class NotificationTypeTest : StringSpec({
    "NotificationType should have EMAIL, SMS, PUSH values" {
        NotificationType.EMAIL.name shouldBe "EMAIL"
        NotificationType.SMS.name shouldBe "SMS"
        NotificationType.PUSH.name shouldBe "PUSH"
    }

    "NotificationType should be enum with 3 entries" {
        NotificationType.entries.shouldHaveSize(3)
    }
})