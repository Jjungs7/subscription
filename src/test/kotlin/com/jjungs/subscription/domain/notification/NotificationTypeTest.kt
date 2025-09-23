package com.jjungs.subscription.domain.notification

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class NotificationTypeTest : StringSpec(
    {
        "NotificationType should have EMAIL, SMS, PUSH, DISCORD values" {
            NotificationType.EMAIL.name shouldBe "EMAIL"
            NotificationType.SMS.name shouldBe "SMS"
            NotificationType.PUSH.name shouldBe "PUSH"
            NotificationType.DISCORD.name shouldBe "DISCORD"
        }

        "NotificationType should be enum with 4 entries" {
            NotificationType.entries.shouldHaveSize(4)
        }
    },
)
