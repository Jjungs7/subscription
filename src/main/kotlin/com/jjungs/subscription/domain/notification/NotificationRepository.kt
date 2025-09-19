package com.jjungs.subscription.domain.notification

interface NotificationRepository {
    fun save(notification: Notification)
    fun findById(id: String): Notification?
    fun findAll(): List<Notification>
    fun deleteById(id: String)
}
