package com.jjungs.subscription.domain.customer

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class CustomerStatusTest : StringSpec(
    {
        "should have values ACTIVE, INACTIVE, SUSPENDED" {
            CustomerStatus.ACTIVE.name shouldBe "ACTIVE"
            CustomerStatus.INACTIVE.name shouldBe "INACTIVE"
            CustomerStatus.SUSPENDED.name shouldBe "SUSPENDED"
        }

        "should have 3 values" {
            CustomerStatus.entries.shouldHaveSize(3)
        }
    },
)
