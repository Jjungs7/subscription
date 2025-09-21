package com.jjungs.subscription.infrastructure

import com.jjungs.subscription.domain.notification.Notification
import com.jjungs.subscription.domain.notification.NotificationType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import java.time.LocalDateTime

@SpringBootTest
class PostgresNotificationRepositoryTest(
    private val repository: PostgresNotificationRepository,
    private val jdbcTemplate: JdbcTemplate,
) : StringSpec(
    {
        "should save notification" {
            val notification = Notification(
                recipient = "test@example.com",
                subject = "Test Subject",
                message = "Test Message",
                type = NotificationType.EMAIL,
                timestamp = LocalDateTime.now(),
            )

            repository.save(notification)

            val savedNotification = repository.findById(notification.id)
            savedNotification.shouldNotBeNull()
            savedNotification.recipient shouldBe notification.recipient
            savedNotification.subject shouldBe notification.subject
            savedNotification.message shouldBe notification.message
            savedNotification.type shouldBe notification.type
            savedNotification.timestamp shouldBe notification.timestamp
        }

        "should find notification by id" {
            val notification = Notification(
                recipient = "user@example.com",
                subject = "Find Test",
                message = "Find Message",
                type = NotificationType.SMS,
                timestamp = LocalDateTime.now(),
            )

            repository.save(notification)

            val foundNotification = repository.findById(notification.id)
            foundNotification.shouldNotBeNull()
            foundNotification.id shouldBe notification.id
            foundNotification.recipient shouldBe "user@example.com"
            foundNotification.subject shouldBe "Find Test"
            foundNotification.message shouldBe "Find Message"
            foundNotification.type shouldBe NotificationType.SMS
        }

        "should throw EmptyResultDataAccessException when finding non-existent notification" {
            shouldThrow<EmptyResultDataAccessException> { repository.findById("non-existent-id") }
        }

        "should find all notifications" {
            // Clean up any existing data
            jdbcTemplate.execute("DELETE FROM notifications")

            val notification1 = Notification(
                recipient = "user1@example.com",
                subject = "Test 1",
                message = "Message 1",
                type = NotificationType.EMAIL,
                timestamp = LocalDateTime.now(),
            )

            val notification2 = Notification(
                recipient = "user2@example.com",
                subject = "Test 2",
                message = "Message 2",
                type = NotificationType.SMS,
                timestamp = LocalDateTime.now(),
            )

            repository.save(notification1)
            repository.save(notification2)

            val allNotifications = repository.findAll()
            allNotifications.shouldHaveSize(2)
        }

        "should delete notification by id" {
            val notification = Notification(
                recipient = "delete@example.com",
                subject = "Delete Test",
                message = "Delete Message",
                type = NotificationType.EMAIL,
                timestamp = LocalDateTime.now(),
            )

            repository.save(notification)

            repository.findById(notification.id)
            repository.deleteById(notification.id)

            shouldThrow<EmptyResultDataAccessException> { repository.findById(notification.id) }
        }
    },
)
