package com.jjungs.subscription.domain.customer

import com.jjungs.subscription.domain.vo.Email
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class CustomerTest : StringSpec(
    {
        "should create customer with required properties" {
            val customerId = CustomerId()
            val email = Email("user@example.com")

            val customer = Customer(customerId, email)

            customer.id shouldBe customerId
            customer.email shouldBe email
            customer.status shouldBe CustomerStatus.PENDING
            customer.createdAt shouldBe customer.updatedAt
        }

        "should create customer with default status as PENDING" {
            val customerId = CustomerId()
            val email = Email("user@example.com")

            val customer = Customer(customerId, email)

            customer.status shouldBe CustomerStatus.PENDING
        }

        "should throw exception for blank email" {
            val customerId = CustomerId()

            shouldThrow<IllegalArgumentException> {
                Customer(customerId, Email(""))
            }
        }

        "should throw exception for whitespace email" {
            val customerId = CustomerId()

            shouldThrow<IllegalArgumentException> {
                Customer(customerId, Email("   "))
            }
        }

        "should activate customer" {
            val customer = Customer(
                CustomerId(),
                Email("test@example.com"),
            )
            customer.suspend()

            customer.activate()

            customer.status shouldBe CustomerStatus.ACTIVE
            customer.updatedAt shouldNotBe customer.createdAt
        }

        "should deactivate customer" {
            val customer = Customer(
                CustomerId(),
                Email("test@example.com"),
            )

            customer.deactivate()

            customer.status shouldBe CustomerStatus.INACTIVE
            customer.updatedAt shouldNotBe customer.createdAt
        }

        "should suspend customer" {
            val customer = Customer(
                CustomerId(),
                Email("test@example.com"),
            )

            customer.suspend()

            customer.status shouldBe CustomerStatus.SUSPENDED
            customer.updatedAt shouldNotBe customer.createdAt
        }

        "should update email" {
            val customer = Customer(
                CustomerId(),
                Email("test@example.com"),
            )
            val newEmail = "newemail@example.com"
            val originalUpdatedAt = customer.updatedAt

            customer.updateEmail(Email(newEmail))

            customer.email shouldBe Email(newEmail)
            customer.updatedAt shouldNotBe originalUpdatedAt
        }

        "should throw exception when updating email to empty string" {
            val customer = Customer(
                CustomerId(),
                Email("test@example.com"),
            )

            shouldThrow<IllegalArgumentException> {
                customer.updateEmail(Email(""))
            }
        }

        "should throw exception when updating email to whitespace" {
            val customer = Customer(
                CustomerId(),
                Email("test@example.com"),
            )

            shouldThrow<IllegalArgumentException> {
                customer.updateEmail(Email("   "))
            }
        }

        "should be equal when customers have same id" {
            val customerId = CustomerId("same-id")
            val email1 = "user1@example.com"
            val email2 = "user2@example.com"

            val customer1 = Customer(customerId, Email(email1))
            val customer2 = Customer(customerId, Email(email2))

            customer1 shouldBe customer2
            customer1.hashCode() shouldBe customer2.hashCode()
        }

        "should not be equal when customers have different ids" {
            val customerId1 = CustomerId("id-1")
            val customerId2 = CustomerId("id-2")
            val email = Email("user@example.com")

            val customer1 = Customer(customerId1, email)
            val customer2 = Customer(customerId2, email)

            customer1 shouldNotBe customer2
            customer1.hashCode() shouldNotBe customer2.hashCode()
        }

        "should update timestamp when status changes" {
            val customer = Customer(
                CustomerId(),
                Email("test@example.com"),
            )
            val originalUpdatedAt = customer.updatedAt

            Thread.sleep(1)

            customer.suspend()

            customer.updatedAt shouldNotBe originalUpdatedAt
            customer.updatedAt.isAfter(originalUpdatedAt) shouldBe true
        }

        "should update timestamp when email changes" {
            val customer = Customer(
                CustomerId(),
                Email("test@example.com"),
            )
            val originalUpdatedAt = customer.updatedAt

            Thread.sleep(1)

            customer.updateEmail(Email("newemail@example.com"))

            customer.updatedAt shouldNotBe originalUpdatedAt
            customer.updatedAt.isAfter(originalUpdatedAt) shouldBe true
        }
    },
)
