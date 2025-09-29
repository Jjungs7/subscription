package com.jjungs.subscription.domain.billing

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDate

class BillingCycleTest : StringSpec(
    {
        "should create BillingCycle with valid type" {
            val cycle = BillingCycle(BillingCycleType.MONTHLY)
            cycle.type shouldBe BillingCycleType.MONTHLY
        }

        "should create BillingCycle with custom interval" {
            val cycle = BillingCycle(BillingCycleType.WEEKLY, 2)
            cycle.type shouldBe BillingCycleType.WEEKLY
            cycle.interval shouldBe 2
        }

        "should default to interval of 1" {
            val cycle = BillingCycle(BillingCycleType.MONTHLY)
            cycle.interval shouldBe 1
        }

        "should throw exception for zero interval" {
            shouldThrow<IllegalArgumentException> {
                BillingCycle(BillingCycleType.MONTHLY, 0)
            }
        }

        "should throw exception for negative interval" {
            shouldThrow<IllegalArgumentException> {
                BillingCycle(BillingCycleType.MONTHLY, -1)
            }
        }

        "should be equal when type and interval are the same" {
            val cycle1 = BillingCycle(BillingCycleType.MONTHLY, 1)
            val cycle2 = BillingCycle(BillingCycleType.MONTHLY, 1)
            cycle1 shouldBe cycle2
        }

        "should not be equal when types are different" {
            val cycle1 = BillingCycle(BillingCycleType.MONTHLY)
            val cycle2 = BillingCycle(BillingCycleType.YEARLY)
            cycle1 shouldNotBe cycle2
        }

        "should not be equal when intervals are different" {
            val cycle1 = BillingCycle(BillingCycleType.MONTHLY, 1)
            val cycle2 = BillingCycle(BillingCycleType.MONTHLY, 2)
            cycle1 shouldNotBe cycle2
        }

        "should be equal to itself" {
            val cycle = BillingCycle(BillingCycleType.MONTHLY)
            cycle shouldBe cycle
        }

        "should not be equal to null" {
            val cycle = BillingCycle(BillingCycleType.MONTHLY)
            cycle shouldNotBe null
        }

        "should not be equal to different type" {
            val cycle = BillingCycle(BillingCycleType.MONTHLY)
            cycle shouldNotBe "MONTHLY"
        }

        "should have same hashCode for equal objects" {
            val cycle1 = BillingCycle(BillingCycleType.MONTHLY, 1)
            val cycle2 = BillingCycle(BillingCycleType.MONTHLY, 1)
            cycle1.hashCode() shouldBe cycle2.hashCode()
        }

        "should have different hashCode for different objects" {
            val cycle1 = BillingCycle(BillingCycleType.MONTHLY, 1)
            val cycle2 = BillingCycle(BillingCycleType.YEARLY, 1)
            cycle1.hashCode() shouldNotBe cycle2.hashCode()
        }

        "should calculate next monthly billing date" {
            val cycle = BillingCycle(BillingCycleType.MONTHLY)
            val currentDate = LocalDate.of(2024, 1, 15)
            val nextDate = cycle.calculateNextBillingDate(currentDate)
            nextDate shouldBe LocalDate.of(2024, 2, 15)
        }

        "should calculate next yearly billing date" {
            val cycle = BillingCycle(BillingCycleType.YEARLY)
            val currentDate = LocalDate.of(2024, 1, 15)
            val nextDate = cycle.calculateNextBillingDate(currentDate)
            nextDate shouldBe LocalDate.of(2025, 1, 15)
        }

        "should calculate next weekly billing date" {
            val cycle = BillingCycle(BillingCycleType.WEEKLY)
            val currentDate = LocalDate.of(2024, 1, 15)
            val nextDate = cycle.calculateNextBillingDate(currentDate)
            nextDate shouldBe LocalDate.of(2024, 1, 22)
        }

        "should calculate next daily billing date" {
            val cycle = BillingCycle(BillingCycleType.DAILY)
            val currentDate = LocalDate.of(2024, 1, 15)
            val nextDate = cycle.calculateNextBillingDate(currentDate)
            nextDate shouldBe LocalDate.of(2024, 1, 16)
        }

        "should handle custom interval billing dates" {
            val cycle = BillingCycle(BillingCycleType.MONTHLY, 3)
            val currentDate = LocalDate.of(2024, 1, 15)
            val nextDate = cycle.calculateNextBillingDate(currentDate)
            nextDate shouldBe LocalDate.of(2024, 4, 15)
        }

        "should handle leap year correctly" {
            val cycle = BillingCycle(BillingCycleType.YEARLY)
            val currentDate = LocalDate.of(2024, 2, 29) // Leap year
            val nextDate = cycle.calculateNextBillingDate(currentDate)
            nextDate shouldBe LocalDate.of(2025, 2, 28) // Non-leap year
        }

        "should create monthly cycle" {
            val cycle = BillingCycle.monthly()
            cycle.type shouldBe BillingCycleType.MONTHLY
            cycle.interval shouldBe 1
        }

        "should create yearly cycle" {
            val cycle = BillingCycle.yearly()
            cycle.type shouldBe BillingCycleType.YEARLY
            cycle.interval shouldBe 1
        }

        "should create weekly cycle" {
            val cycle = BillingCycle.weekly()
            cycle.type shouldBe BillingCycleType.WEEKLY
            cycle.interval shouldBe 1
        }

        "should create daily cycle" {
            val cycle = BillingCycle.daily()
            cycle.type shouldBe BillingCycleType.DAILY
            cycle.interval shouldBe 1
        }

        "should compare BillingCycle objects correctly" {
            val monthly = BillingCycle(BillingCycleType.MONTHLY)
            val yearly = BillingCycle(BillingCycleType.YEARLY)
            val monthly2 = BillingCycle(BillingCycleType.MONTHLY)

            monthly.compareTo(yearly) shouldBe -1
            yearly.compareTo(monthly) shouldBe 1
            monthly.compareTo(monthly2) shouldBe 0
        }
    },
)
