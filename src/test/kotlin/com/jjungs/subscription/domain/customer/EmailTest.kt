package com.jjungs.subscription.domain.customer

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class EmailTest : StringSpec(
    {
        "should create valid email" {
            val emailString = "user@example.com"

            val email = Email(emailString)

            email.value shouldBe emailString
        }

        "should create valid email with subdomain" {
            val emailString = "user@subdomain.example.com"

            val email = Email(emailString)

            email.value shouldBe emailString
        }

        "should create valid email with plus sign" {
            val emailString = "user+tag@example.com"

            val email = Email(emailString)

            email.value shouldBe emailString
        }

        "should throw exception for empty email" {
            shouldThrow<IllegalArgumentException> {
                Email("")
            }
        }

        "should throw exception for blank email" {
            shouldThrow<IllegalArgumentException> {
                Email("   ")
            }
        }

        "should throw exception for email without at symbol" {
            shouldThrow<IllegalArgumentException> {
                Email("userexample.com")
            }
        }

        "should throw exception for email with multiple at symbols" {
            shouldThrow<IllegalArgumentException> {
                Email("user@@example.com")
            }
        }

        "should throw exception for email without domain" {
            shouldThrow<IllegalArgumentException> {
                Email("user@")
            }
        }

        "should throw exception for email without local part" {
            shouldThrow<IllegalArgumentException> {
                Email("@example.com")
            }
        }

        "should throw exception for email with invalid characters" {
            shouldThrow<IllegalArgumentException> {
                Email("user name@example.com")
            }
        }

        "should throw exception for email with invalid domain format" {
            shouldThrow<IllegalArgumentException> {
                Email("user@example")
            }
        }

        "should be equal when emails have same value" {
            val email1 = Email("user@example.com")
            val email2 = Email("user@example.com")

            email1 shouldBe email2
            email1.hashCode() shouldBe email2.hashCode()
        }

        "should not be equal when emails have different values" {
            val email1 = Email("user1@example.com")
            val email2 = Email("user2@example.com")

            email1 shouldNotBe email2
            email1.hashCode() shouldNotBe email2.hashCode()
        }

        "should return email value in toString" {
            val emailString = "user@example.com"
            val email = Email(emailString)

            email.toString() shouldBe emailString
        }
    },
)
