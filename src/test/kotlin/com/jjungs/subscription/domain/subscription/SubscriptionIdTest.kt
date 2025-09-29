package com.jjungs.subscription.domain.subscription

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.*

class SubscriptionIdTest : StringSpec(
    {
        "should create SubscriptionId with valid UUID string" {
            val uuid = UUID.randomUUID()
            val subscriptionId = SubscriptionId(uuid.toString())
            subscriptionId.id shouldBe uuid.toString()
        }

        "should create SubscriptionId with UUID object" {
            val uuid = UUID.randomUUID()
            val subscriptionId = SubscriptionId(uuid)
            subscriptionId.id shouldBe uuid.toString()
        }

        "should create SubscriptionId with random UUID when no parameter provided" {
            val subscriptionId = SubscriptionId()
            subscriptionId.id shouldNotBe null
            UUID.fromString(subscriptionId.id)
        }

        "should throw exception for blank string" {
            shouldThrow<IllegalArgumentException> {
                SubscriptionId("")
            }
        }

        "should throw exception for whitespace only string" {
            shouldThrow<IllegalArgumentException> {
                SubscriptionId("   ")
            }
        }

        "should be equal when IDs are the same" {
            val uuid = UUID.randomUUID()
            val subscriptionId1 = SubscriptionId(uuid.toString())
            val subscriptionId2 = SubscriptionId(uuid.toString())
            subscriptionId1 shouldBe subscriptionId2
        }

        "should not be equal when IDs are different" {
            val subscriptionId1 = SubscriptionId()
            val subscriptionId2 = SubscriptionId()
            subscriptionId1 shouldNotBe subscriptionId2
        }

        "should be equal to itself" {
            val subscriptionId = SubscriptionId()
            subscriptionId shouldBe subscriptionId
        }

        "should not be equal to null" {
            val subscriptionId = SubscriptionId()
            subscriptionId shouldNotBe null
        }

        "should be equal when created from same UUID object" {
            val uuid = UUID.randomUUID()
            val subscriptionId1 = SubscriptionId(uuid)
            val subscriptionId2 = SubscriptionId(uuid)
            subscriptionId1 shouldBe subscriptionId2
        }

        "should have same hashCode for equal objects" {
            val uuid = UUID.randomUUID()
            val subscriptionId1 = SubscriptionId(uuid.toString())
            val subscriptionId2 = SubscriptionId(uuid.toString())
            subscriptionId1.hashCode() shouldBe subscriptionId2.hashCode()
        }

        "should have different hashCode for different objects" {
            val subscriptionId1 = SubscriptionId()
            val subscriptionId2 = SubscriptionId()
            subscriptionId1.hashCode() shouldNotBe subscriptionId2.hashCode()
        }

        "should handle UUID with uppercase" {
            val uuid = UUID.randomUUID()
            val upperUuid = uuid.toString().uppercase()
            val subscriptionId = SubscriptionId(upperUuid)
            subscriptionId.id shouldBe upperUuid
        }

        "should handle UUID with lowercase" {
            val uuid = UUID.randomUUID()
            val lowerUuid = uuid.toString().lowercase()
            val subscriptionId = SubscriptionId(lowerUuid)
            subscriptionId.id shouldBe lowerUuid
        }
    },
)
