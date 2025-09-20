package com.jjungs.subscription.interfaces

import com.jjungs.subscription.domain.notification.Notification
import com.jjungs.subscription.domain.notification.NotificationPort
import com.jjungs.subscription.domain.notification.NotificationRepository
import com.jjungs.subscription.domain.notification.NotificationType
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/notifications")
class NotificationController(
    private val notificationPort: NotificationPort,
    private val notificationRepository: NotificationRepository,
) {

    @PostMapping
    fun sendNotification(@RequestBody request: SendNotificationRequest): Notification {
        val notification = Notification(
            recipient = request.recipient,
            subject = request.subject,
            message = request.message,
            type = request.type,
            timestamp = LocalDateTime.now(),
        )

        // Save to repository
        notificationRepository.save(notification)

        // Send via notification port
        notificationPort.send(notification)

        // Save updated status
        notificationRepository.save(notification)

        return notification
    }

    @GetMapping("/{id}")
    fun getNotification(@PathVariable id: String): Notification? {
        return notificationRepository.findById(id)
    }

    @GetMapping
    fun getAllNotifications(): List<Notification> {
        return notificationRepository.findAll()
    }

    @DeleteMapping("/{id}")
    fun deleteNotification(@PathVariable id: String) {
        notificationRepository.deleteById(id)
    }
}

data class SendNotificationRequest(
    val recipient: String,
    val subject: String,
    val message: String,
    val type: NotificationType,
)
