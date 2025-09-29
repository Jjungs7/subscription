package com.jjungs.subscription.domain.billing

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class MoneyTest : StringSpec(
    {
        "should create Money with valid amount" {
            val money = Money(1000L)
            money.amount shouldBe 1000L
        }

        "should create Money with zero amount" {
            val money = Money(0L)
            money.amount shouldBe 0L
        }

        "should throw exception for negative amount" {
            shouldThrow<IllegalArgumentException> {
                Money(-100L)
            }
        }

        "should throw exception for very large negative amount" {
            shouldThrow<IllegalArgumentException> {
                Money(Long.MIN_VALUE)
            }
        }

        "should be equal when amounts are the same" {
            val money1 = Money(1000L)
            val money2 = Money(1000L)
            money1 shouldBe money2
        }

        "should not be equal when amounts are different" {
            val money1 = Money(1000L)
            val money2 = Money(2000L)
            money1 shouldNotBe money2
        }

        "should be equal to itself" {
            val money = Money(1000L)
            money shouldBe money
        }

        "should not be equal to null" {
            val money = Money(1000L)
            money shouldNotBe null
        }

        "should not be equal to different type" {
            val money = Money(1000L)
            money shouldNotBe "1000"
        }

        "should have same hashCode for equal objects" {
            val money1 = Money(1000L)
            val money2 = Money(1000L)
            money1.hashCode() shouldBe money2.hashCode()
        }

        "should have different hashCode for different amounts" {
            val money1 = Money(1000L)
            val money2 = Money(2000L)
            money1.hashCode() shouldNotBe money2.hashCode()
        }

        "should add two Money objects" {
            val money1 = Money(1000L)
            val money2 = Money(500L)
            val result = money1.plus(money2)
            result.amount shouldBe 1500L
        }

        "should subtract Money objects" {
            val money1 = Money(1000L)
            val money2 = Money(300L)
            val result = money1.minus(money2)
            result.amount shouldBe 700L
        }

        "should multiply Money by factor" {
            val money = Money(1000L)
            val result = money.times(2.5)
            result.amount shouldBe 2500L
        }

        "should divide Money by factor" {
            val money = Money(1000L)
            val result = money.div(2.0)
            result.amount shouldBe 500L
        }

        "should throw exception when subtraction results in negative amount" {
            val money1 = Money(500L)
            val money2 = Money(1000L)
            shouldThrow<IllegalArgumentException> {
                money1.minus(money2)
            }
        }

        "should throw exception when multiplication results in negative amount" {
            val money = Money(1000L)
            shouldThrow<IllegalArgumentException> {
                money.times(-1.0)
            }
        }

        "should throw exception when division results in negative amount" {
            val money = Money(1000L)
            shouldThrow<IllegalArgumentException> {
                money.div(-1.0)
            }
        }

        "should compare Money objects correctly" {
            val money1 = Money(1000L)
            val money2 = Money(2000L)
            val money3 = Money(1000L)

            money1.compareTo(money2) shouldBe -1
            money2.compareTo(money1) shouldBe 1
            money1.compareTo(money3) shouldBe 0
        }

        "should convert to dollars" {
            val money = Money(1050L)
            money.toDollars() shouldBe 10.50
        }

        "should convert to cents" {
            val money = Money(1050L)
            money.toCents() shouldBe 1050L
        }

        "Zero should return cached instance" {
            val money = Money(0L)
            money shouldBe Money.ZERO
        }
    },
)
