package com.jjungs.subscription.domain.subscription

import com.jjungs.subscription.domain.billing.BillingCycle
import com.jjungs.subscription.domain.billing.Money
import com.jjungs.subscription.domain.billing.PlanId
import com.jjungs.subscription.domain.customer.CustomerId
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows
import java.time.OffsetDateTime
import java.time.ZoneId

class SubscriptionTest : StringSpec(
    {
        val customerId = CustomerId()
        val planId = PlanId()
        val subscriptionId = SubscriptionId()
        val monthlyBillingCycle = BillingCycle.monthly()
        val monthlyPrice = Money.valueOf(2999L)
        val currentDate = OffsetDateTime.now(ZoneId.of("Asia/Seoul"))

        fun createActiveSubscription(): Subscription {
            val subscription = Subscription(
                id = subscriptionId,
                customerId = customerId,
                planId = planId,
                billingCycle = monthlyBillingCycle,
                price = monthlyPrice,
                startDate = currentDate,
            )
            subscription.activate()
            return subscription
        }

        "should create subscription with valid parameters" {
            val subscription = Subscription(
                id = subscriptionId,
                customerId = customerId,
                planId = planId,
                billingCycle = monthlyBillingCycle,
                price = monthlyPrice,
                startDate = currentDate,
            )

            subscriptionId shouldBe subscription.id
            customerId shouldBe subscription.customerId
            planId shouldBe subscription.planId
            monthlyBillingCycle shouldBe subscription.billingCycle
            monthlyPrice shouldBe subscription.price
            currentDate shouldBe subscription.startDate
            SubscriptionStatus.PENDING shouldBe subscription.status
            subscription.createdAt.shouldNotBeNull()
            subscription.updatedAt.shouldNotBeNull()
            0L shouldBe subscription.version
        }

        "should activate subscription" {
            val subscription = Subscription(
                id = subscriptionId,
                customerId = customerId,
                planId = planId,
                billingCycle = monthlyBillingCycle,
                price = monthlyPrice,
                startDate = currentDate,
            )

            subscription.activate()

            SubscriptionStatus.ACTIVE shouldBe subscription.status
            subscription.updatedAt.isAfter(subscription.createdAt).shouldBeTrue()
        }

        "should start trial period" {
            val subscription = Subscription(
                id = subscriptionId,
                customerId = customerId,
                planId = planId,
                billingCycle = monthlyBillingCycle,
                price = monthlyPrice,
                startDate = currentDate,
            )
            val trialEndDate = currentDate.plusDays(14)

            subscription.startTrial(trialEndDate)

            SubscriptionStatus.TRIAL shouldBe subscription.status
            trialEndDate shouldBe subscription.trialEndDate
        }

        "should not allow trial start with past end date" {
            val subscription = Subscription(
                id = subscriptionId,
                customerId = customerId,
                planId = planId,
                billingCycle = monthlyBillingCycle,
                price = monthlyPrice,
                startDate = currentDate,
            )
            val pastDate = currentDate.minusDays(1)

            assertThrows<IllegalArgumentException> {
                subscription.startTrial(pastDate)
            }
        }

        "should pause subscription" {
            val subscription = createActiveSubscription()

            subscription.pause()

            SubscriptionStatus.PAUSED shouldBe subscription.status
        }

        "should not allow pause from non-active status" {
            val subscription = Subscription(
                id = subscriptionId,
                customerId = customerId,
                planId = planId,
                billingCycle = monthlyBillingCycle,
                price = monthlyPrice,
                startDate = currentDate,
            )

            assertThrows<IllegalArgumentException> {
                subscription.pause()
            }
        }

        "should resume paused subscription" {
            val subscription = createActiveSubscription()
            subscription.pause()

            subscription.resume()

            SubscriptionStatus.ACTIVE shouldBe subscription.status
        }

        "should not allow resume from non-paused status" {
            val subscription = createActiveSubscription()

            assertThrows<IllegalArgumentException> {
                subscription.resume()
            }
        }

        "should suspend subscription" {
            val subscription = createActiveSubscription()

            subscription.suspend()

            SubscriptionStatus.SUSPENDED shouldBe subscription.status
        }

        "should mark subscription as past due" {
            val subscription = createActiveSubscription()

            subscription.markPastDue()

            SubscriptionStatus.PAST_DUE shouldBe subscription.status
        }

        "should cancel subscription" {
            val subscription = createActiveSubscription()

            subscription.cancel()

            SubscriptionStatus.CANCELLED shouldBe subscription.status
            subscription.terminatedAt.shouldNotBeNull()
        }

        "should expire subscription" {
            val subscription = createActiveSubscription()

            subscription.expire()

            SubscriptionStatus.EXPIRED shouldBe subscription.status
            subscription.terminatedAt.shouldNotBeNull()
        }

        "should calculate next billing date" {
            val subscription = createActiveSubscription()

            val nextBillingDate = subscription.calculateNextBillingDate(currentDate)

            currentDate.plusMonths(1) shouldBe nextBillingDate
        }

        "should calculate next billing date for weekly cycle" {
            val weeklyBillingCycle = BillingCycle.weekly()
            val subscription = Subscription(
                id = subscriptionId,
                customerId = customerId,
                planId = planId,
                billingCycle = weeklyBillingCycle,
                price = monthlyPrice,
                startDate = currentDate,
            )
            subscription.activate()

            val nextBillingDate = subscription.calculateNextBillingDate(currentDate)

            currentDate.plusWeeks(1) shouldBe nextBillingDate
        }

        "should check if subscription is active" {
            val activeSubscription = createActiveSubscription()
            val pendingSubscription = Subscription(
                id = subscriptionId,
                customerId = customerId,
                planId = planId,
                billingCycle = monthlyBillingCycle,
                price = monthlyPrice,
                startDate = currentDate,
            )

            activeSubscription.isActive().shouldBeTrue()
            pendingSubscription.isActive().shouldBeFalse()
        }

        "should check if subscription is in trial" {
            val subscription = Subscription(
                id = subscriptionId,
                customerId = customerId,
                planId = planId,
                billingCycle = monthlyBillingCycle,
                price = monthlyPrice,
                startDate = currentDate,
            )
            subscription.startTrial(currentDate.plusDays(14))

            subscription.isInTrial().shouldBeTrue()
        }

        "should check if subscription is cancelled" {
            val subscription = createActiveSubscription()
            subscription.cancel()

            subscription.isCancelled().shouldBeTrue()
        }

        "should check if subscription is expired" {
            val subscription = createActiveSubscription()
            subscription.expire()

            subscription.isExpired().shouldBeTrue()
        }

        "should check if subscription can be renewed" {
            val activeSubscription = createActiveSubscription()
            val cancelledSubscription = createActiveSubscription().apply { cancel() }
            val expiredSubscription = createActiveSubscription().apply { expire() }

            activeSubscription.canBeRenewed().shouldBeTrue()
            cancelledSubscription.canBeRenewed().shouldBeFalse()
            expiredSubscription.canBeRenewed().shouldBeFalse()
        }

        "should update subscription plan" {
            val subscription = createActiveSubscription()
            val newPlanId = PlanId()
            val newPrice = Money.valueOf(4999L)

            subscription.updatePlan(newPlanId, newPrice)

            newPlanId shouldBe subscription.planId
            newPrice shouldBe subscription.price
        }

        "should not allow plan update for cancelled subscription" {
            val subscription = createActiveSubscription()
            subscription.cancel()

            assertThrows<IllegalArgumentException> {
                subscription.updatePlan(PlanId(), Money.valueOf(4999L))
            }
        }

        "should increment version on state changes" {
            val subscription = Subscription(
                id = subscriptionId,
                customerId = customerId,
                planId = planId,
                billingCycle = monthlyBillingCycle,
                price = monthlyPrice,
                startDate = currentDate,
            )
            val initialVersion = subscription.version

            subscription.activate()

            initialVersion + 1 shouldBe subscription.version
        }

        "should update timestamp on state changes" {
            val subscription = Subscription(
                id = subscriptionId,
                customerId = customerId,
                planId = planId,
                billingCycle = monthlyBillingCycle,
                price = monthlyPrice,
                startDate = currentDate,
            )
            val initialUpdatedAt = subscription.updatedAt

            Thread.sleep(1)

            subscription.activate()

            subscription.updatedAt.isAfter(initialUpdatedAt).shouldBeTrue()
        }

        "should handle trial expiration" {
            val subscription = Subscription(
                id = subscriptionId,
                customerId = customerId,
                planId = planId,
                billingCycle = monthlyBillingCycle,
                price = monthlyPrice,
                startDate = currentDate,
            )
            val trialEndDate = currentDate.plusDays(14)
            subscription.startTrial(trialEndDate)

            subscription.handleTrialExpiration()

            SubscriptionStatus.PENDING shouldBe subscription.status
            subscription.trialEndDate.shouldNotBeNull()
        }

        "should not allow trial expiration for non-trial subscription" {
            val subscription = createActiveSubscription()

            assertThrows<IllegalArgumentException> {
                subscription.handleTrialExpiration()
            }
        }

        "should calculate total amount for billing period" {
            val subscription = createActiveSubscription()

            val totalAmount = subscription.calculateTotalAmount()

            monthlyPrice shouldBe totalAmount
        }

        "should check if subscription needs renewal" {
            val subscription = createActiveSubscription()
            val nextBillingDate = subscription.calculateNextBillingDate(currentDate)

            subscription.needsRenewal(nextBillingDate.plusDays(1)).shouldBeTrue()
            subscription.needsRenewal(nextBillingDate.minusDays(1)).shouldBeFalse()
        }

        "should handle payment failure" {
            val subscription = createActiveSubscription()

            subscription.handlePaymentFailure()

            SubscriptionStatus.PAST_DUE shouldBe subscription.status
        }

        "should handle payment success after failure" {
            val subscription = createActiveSubscription()
            subscription.handlePaymentFailure()

            subscription.handlePaymentSuccess()

            SubscriptionStatus.ACTIVE shouldBe subscription.status
        }

        "should not allow payment success for non-past-due subscription" {
            val subscription = createActiveSubscription()

            assertThrows<IllegalArgumentException> {
                subscription.handlePaymentSuccess()
            }
        }
    },
)
