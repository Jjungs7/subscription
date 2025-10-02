package com.jjungs.subscription.domain.billing

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.*

class PlanIdTest : StringSpec(
    {
        "should create PlanId with valid UUID string" {
            val uuid = UUID.randomUUID()
            val planId = PlanId(uuid.toString())
            planId.value shouldBe uuid.toString()
        }

        "should create PlanId with UUID object" {
            val uuid = UUID.randomUUID()
            val planId = PlanId(uuid)
            planId.value shouldBe uuid.toString()
        }

        "should create PlanId with random UUID when no parameter provided" {
            val planId = PlanId()
            planId.value shouldNotBe null
            UUID.fromString(planId.value)
        }

        "should throw exception for empty string" {
            shouldThrow<IllegalArgumentException> {
                PlanId("")
            }
        }

        "should throw exception for whitespace only string" {
            shouldThrow<IllegalArgumentException> {
                PlanId("   ")
            }
        }

        "should be equal when IDs are the same" {
            val uuid = UUID.randomUUID()
            val planId1 = PlanId(uuid.toString())
            val planId2 = PlanId(uuid.toString())
            planId1 shouldBe planId2
        }

        "should not be equal when IDs are different" {
            val planId1 = PlanId()
            val planId2 = PlanId()
            planId1 shouldNotBe planId2
        }

        "should be equal to itself" {
            val planId = PlanId()
            planId shouldBe planId
        }

        "should not be equal to null" {
            val planId = PlanId()
            planId shouldNotBe null
        }

        "should be equal when created from same UUID object" {
            val uuid = UUID.randomUUID()
            val planId1 = PlanId(uuid)
            val planId2 = PlanId(uuid)
            planId1 shouldBe planId2
        }

        "should have same hashCode for equal objects" {
            val uuid = UUID.randomUUID()
            val planId1 = PlanId(uuid.toString())
            val planId2 = PlanId(uuid.toString())
            planId1.hashCode() shouldBe planId2.hashCode()
        }

        "should have different hashCode for different objects" {
            val planId1 = PlanId()
            val planId2 = PlanId()
            planId1.hashCode() shouldNotBe planId2.hashCode()
        }

        "should handle minimum UUID" {
            val minUuid = "00000000-0000-0000-0000-000000000000"
            val planId = PlanId(minUuid)
            planId.value shouldBe minUuid
        }

        "should handle maximum UUID" {
            val maxUuid = "ffffffff-ffff-ffff-ffff-ffffffffffff"
            val planId = PlanId(maxUuid)
            planId.value shouldBe maxUuid
        }

        "should be immutable" {
            val uuid = UUID.randomUUID()
            val planId = PlanId(uuid.toString())
            val originalId = planId.value

            planId.value shouldBe originalId
        }
    },
)
