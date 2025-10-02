package com.jjungs.subscription.domain.subscription

import com.jjungs.subscription.domain.billing.BillingCycle
import com.jjungs.subscription.domain.billing.Money
import com.jjungs.subscription.domain.billing.PlanId
import com.jjungs.subscription.domain.customer.CustomerId
import java.time.OffsetDateTime

class Subscription(
    val id: SubscriptionId,
    val customerId: CustomerId,
    var planId: PlanId,
    val billingCycle: BillingCycle,
    var price: Money,
    val startDate: OffsetDateTime,
    var version: Long = 0,
) {
    val createdAt: OffsetDateTime = OffsetDateTime.now()
    var updatedAt: OffsetDateTime = createdAt
        private set

    var status: SubscriptionStatus = SubscriptionStatus.PENDING
        private set

    var trialEndDate: OffsetDateTime? = null
        private set

    var pausedEndDate: OffsetDateTime? = null
        private set

    var terminatedAt: OffsetDateTime? = null
        private set

    companion object {
        const val DEFAULT_TRIAL_DAYS = 30L
        const val DEFAULT_PAUSED_DAYS = 365L
    }

    fun activate() {
        require(status == SubscriptionStatus.PENDING) {
            "Subscription can only be activated from PENDING status. Current status: $status"
        }
        status = SubscriptionStatus.ACTIVE
        updateTimestamp()
        incrementVersion()
    }

    fun startTrial(trialEndDate: OffsetDateTime = OffsetDateTime.now().plusDays(DEFAULT_TRIAL_DAYS)) {
        require(status == SubscriptionStatus.PENDING) {
            "Trial can only be started from PENDING status. Current status: $status"
        }
        require(this.trialEndDate == null) { "Trial already expired for this subscription." }
        require(trialEndDate.isAfter(OffsetDateTime.now())) {
            "Trial end date must be in the future. Provided: $trialEndDate"
        }
        this.trialEndDate = trialEndDate
        status = SubscriptionStatus.TRIAL
        updateTimestamp()
        incrementVersion()
    }

    fun pause(pausedEndDate: OffsetDateTime = OffsetDateTime.now().plusDays(DEFAULT_PAUSED_DAYS)) {
        require(status == SubscriptionStatus.ACTIVE) {
            "Subscription can only be paused from ACTIVE status. Current status: $status"
        }
        require(pausedEndDate.isAfter(OffsetDateTime.now())) {
            "Paused end date must be in the future. Provided: $pausedEndDate"
        }
        this.pausedEndDate = pausedEndDate
        status = SubscriptionStatus.PAUSED
        updateTimestamp()
        incrementVersion()
    }

    fun resume() {
        require(status == SubscriptionStatus.PAUSED) {
            "Subscription can only be resumed from PAUSED status. Current status: $status"
        }
        status = SubscriptionStatus.ACTIVE
        updateTimestamp()
        incrementVersion()
    }

    fun suspend() {
        require(status in listOf(SubscriptionStatus.ACTIVE, SubscriptionStatus.PAUSED)) {
            "Subscription can only be suspended from ACTIVE or PAUSED status. Current status: $status"
        }
        status = SubscriptionStatus.SUSPENDED
        updateTimestamp()
        incrementVersion()
    }

    fun markPastDue() {
        require(status == SubscriptionStatus.ACTIVE) {
            "Subscription can only be marked as past due from ACTIVE status. Current status: $status"
        }
        status = SubscriptionStatus.PAST_DUE
        updateTimestamp()
        incrementVersion()
    }

    fun cancel() {
        require(status != SubscriptionStatus.EXPIRED) {
            "Subscription cannot be cancelled if subscription has already expired. Current status: $status"
        }
        status = SubscriptionStatus.CANCELLED
        terminatedAt = OffsetDateTime.now()
        updateTimestamp()
        incrementVersion()
    }

    fun expire() {
        require(status in listOf(SubscriptionStatus.ACTIVE, SubscriptionStatus.PAUSED, SubscriptionStatus.PAST_DUE)) {
            "Subscription can only be expired from ACTIVE, PAUSED, or PAST_DUE status. Current status: $status"
        }
        status = SubscriptionStatus.EXPIRED
        terminatedAt = OffsetDateTime.now()
        updateTimestamp()
        incrementVersion()
    }

    fun handleTrialExpiration() {
        require(status == SubscriptionStatus.TRIAL) {
            "Trial expiration can only be handled from TRIAL status. Current status: $status"
        }
        status = SubscriptionStatus.PENDING
        updateTimestamp()
        incrementVersion()
    }

    fun updatePlan(newPlanId: PlanId, newPrice: Money) {
        require(status in listOf(SubscriptionStatus.ACTIVE, SubscriptionStatus.PAUSED)) {
            "Plan can only be updated for ACTIVE or PAUSED subscriptions. Current status: $status"
        }
        require(newPrice.amount > 0) {
            "New price must be positive. Provided: ${newPrice.amount}"
        }
        planId = newPlanId
        price = newPrice
        updateTimestamp()
        incrementVersion()
    }

    fun handlePaymentFailure() {
        require(status == SubscriptionStatus.ACTIVE) {
            "Payment failure can only be handled from ACTIVE status. Current status: $status"
        }
        status = SubscriptionStatus.PAST_DUE
        updateTimestamp()
        incrementVersion()
    }

    fun handlePaymentSuccess() {
        require(status == SubscriptionStatus.PAST_DUE) {
            "Payment success can only be handled from PAST_DUE status. Current status: $status"
        }
        status = SubscriptionStatus.ACTIVE
        updateTimestamp()
        incrementVersion()
    }

    fun calculateNextBillingDate(currentDate: OffsetDateTime): OffsetDateTime {
        return billingCycle.calculateNextBillingDate(currentDate)
    }

    fun isActive(): Boolean {
        return status == SubscriptionStatus.ACTIVE
    }

    fun isInTrial(): Boolean {
        return status == SubscriptionStatus.TRIAL
    }

    fun isPending(): Boolean {
        return status == SubscriptionStatus.PENDING
    }

    fun isPpaused(): Boolean {
        return status == SubscriptionStatus.PAUSED
    }

    fun isSuspended(): Boolean {
        return status == SubscriptionStatus.SUSPENDED
    }

    fun isPastDue(): Boolean {
        return status == SubscriptionStatus.PAST_DUE
    }

    fun isCancelled(): Boolean {
        return status == SubscriptionStatus.CANCELLED
    }

    fun isExpired(): Boolean {
        return status == SubscriptionStatus.EXPIRED
    }

    fun canBeRenewed(): Boolean {
        return status in listOf(SubscriptionStatus.ACTIVE, SubscriptionStatus.PAUSED)
    }

    fun calculateTotalAmount(): Money {
        return price
    }

    fun needsRenewal(checkDate: OffsetDateTime): Boolean {
        if (!canBeRenewed()) return false
        val nextBillingDate = calculateNextBillingDate(startDate)
        return checkDate.isAfter(nextBillingDate) || checkDate.isEqual(nextBillingDate)
    }

    private fun updateTimestamp() {
        updatedAt = OffsetDateTime.now()
    }

    private fun incrementVersion() {
        version++
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Subscription) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
