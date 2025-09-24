package com.jjungs.subscription.domain.customer

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class CustomerStatusTest : StringSpec(
    {
        "should have values ACTIVE, INACTIVE, SUSPENDED" {
            CustomerStatus.PENDING.name shouldBe "PENDING"
            CustomerStatus.ACTIVE.name shouldBe "ACTIVE"
            CustomerStatus.INACTIVE.name shouldBe "INACTIVE"
            CustomerStatus.SUSPENDED.name shouldBe "SUSPENDED"
        }

        "should have 4 values" {
            CustomerStatus.entries.shouldHaveSize(4)
        }
    },
)
