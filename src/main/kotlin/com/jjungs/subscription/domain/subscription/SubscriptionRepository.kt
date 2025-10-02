package com.jjungs.subscription.domain.subscription

import com.jjungs.subscription.domain.billing.PlanId
import com.jjungs.subscription.domain.customer.CustomerId
import java.time.LocalDate

interface SubscriptionRepository {
    fun save(subscription: Subscription): Subscription
    fun findById(id: SubscriptionId): Subscription?
    fun findByCustomerId(customerId: CustomerId): List<Subscription>
    fun findActiveByCustomerId(customerId: CustomerId): List<Subscription>
    fun findSubscriptionsNeedingRenewal(date: LocalDate): List<Subscription>
    fun findPastDueSubscriptions(): List<Subscription>
    fun findTrialSubscriptionsExpiringOn(date: LocalDate): List<Subscription>
    fun getActiveSubscriptionForPlan(customerId: CustomerId, planId: PlanId): Subscription?
    fun findByCreatedDateRange(startDate: LocalDate, endDate: LocalDate): List<Subscription>
    fun deleteById(id: SubscriptionId)
}
