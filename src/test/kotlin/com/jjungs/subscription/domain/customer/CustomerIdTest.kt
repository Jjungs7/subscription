package com.jjungs.subscription.domain.customer

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.*

class CustomerIdTest : StringSpec(
    {

        "should create valid CustomerId from string" {
            val idString = "customer-123"

            val customerId = CustomerId(idString)

            customerId.value shouldBe idString
        }

        "should create valid CustomerId from UUID" {
            val uuid = UUID.randomUUID()

            val customerId = CustomerId(uuid)

            customerId.value shouldBe uuid.toString()
        }

        "should generate unique CustomerId when no value provided" {
            val customerId1 = CustomerId()
            val customerId2 = CustomerId()

            customerId1.value shouldNotBe customerId2.value
            customerId1.value.isNotBlank() shouldBe true
            customerId2.value.isNotBlank() shouldBe true
        }

        "should throw exception for empty string" {
            shouldThrow<IllegalArgumentException> {
                CustomerId("")
            }
        }

        "should throw exception for blank string" {
            shouldThrow<IllegalArgumentException> {
                CustomerId("   ")
            }
        }


        "should be equal when CustomerIds have same value" {
            val idString = "customer-123"
            val customerId1 = CustomerId(idString)
            val customerId2 = CustomerId(idString)

            customerId1 shouldBe customerId2
        }

        "should not be equal when CustomerIds have different values" {
            val customerId1 = CustomerId("customer-123")
            val customerId2 = CustomerId("customer-456")

            customerId1 shouldNotBe customerId2
            customerId1.hashCode() shouldNotBe customerId2.hashCode()
        }

        "should return id value in toString" {
            val idString = "customer-123"
            val customerId = CustomerId(idString)

            customerId.toString() shouldBe idString
        }

        "should validate UUID format when created from string" {
            val validUuid = UUID.randomUUID().toString()

            val customerId = CustomerId(validUuid)

            customerId.value shouldBe validUuid
        }

        "should accept custom string format for CustomerId" {
            val customId = "CUST-2024-001"

            val customerId = CustomerId(customId)

            customerId.value shouldBe customId
        }
    },
)
