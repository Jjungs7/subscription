package com.jjungs.subscription.interfaces

import com.jjungs.subscription.application.notification.NotificationApplicationService
import com.jjungs.subscription.domain.notification.Notification
import com.jjungs.subscription.domain.notification.NotificationPort
import com.jjungs.subscription.domain.notification.NotificationType
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/notifications")
class NotificationController(
    private val notificationPort: NotificationPort,
    private val notificationApplicationService: NotificationApplicationService,
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

        // Save to repository through application service
        notificationApplicationService.saveNotification(notification)

        // Send via notification port
        notificationPort.send(notification)

        // Save updated status
        notificationApplicationService.saveNotification(notification)

        return notification
    }

    @GetMapping("/{id}")
    fun getNotification(@PathVariable id: String): Notification? {
        return notificationApplicationService.getNotification(id)
    }

    @GetMapping
    fun getAllNotifications(): List<Notification> {
        return notificationApplicationService.getAllNotifications()
    }

    @DeleteMapping("/{id}")
    fun deleteNotification(@PathVariable id: String) {
        notificationApplicationService.deleteNotification(id)
    }
}

data class SendNotificationRequest(
    val recipient: String,
    val subject: String,
    val message: String,
    val type: NotificationType,
)
