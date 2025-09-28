package com.jjungs.subscription.interfaces.notification

import com.fasterxml.jackson.databind.ObjectMapper
import com.jjungs.subscription.application.notification.NotificationApplicationService
import com.jjungs.subscription.config.TestConfig
import com.jjungs.subscription.domain.notification.Notification
import com.jjungs.subscription.domain.notification.NotificationPort
import com.jjungs.subscription.domain.notification.NotificationType
import io.kotest.core.spec.style.BehaviorSpec
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.whenever
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.web.server.ResponseStatusException
import java.time.OffsetDateTime
import java.util.*

@WebMvcTest(NotificationController::class)
@Import(TestConfig::class)
@ExtendWith(MockitoExtension::class)
class NotificationControllerTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
    private val notificationApplicationService: NotificationApplicationService,
    private val notificationPort: NotificationPort,
) : BehaviorSpec() {

    init {
        given("A notification controller") {
            val recipient = "test@example.com"
            val subject = "Test Subject"
            val message = "Test Message"
            val type = NotificationType.EMAIL

            `when`("sending a notification") {
                val request = SendNotificationRequest(recipient, subject, message, type)
                val jsonRequest = objectMapper.writeValueAsString(request)
                val notification = Notification(recipient, subject, message, type, OffsetDateTime.now()).apply {
                    javaClass.getDeclaredField("id").apply { isAccessible = true }.set(this, "test-id")
                }

                then("it should create and send the notification successfully") {
                    doReturn(notification).whenever(notificationApplicationService)
                        .sendNotification(any())

                    mockMvc.perform(
                        MockMvcRequestBuilders.post("/notifications")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequest),
                    )
                        .andExpect(MockMvcResultMatchers.status().isOk)
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("test-id"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.recipient").value(recipient))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.subject").value(subject))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(type.name))
                }
            }

            `when`("getting a notification by ID") {
                val notificationId = UUID.randomUUID().toString()
                val notification = Notification(recipient, subject, message, type, OffsetDateTime.now()).apply {
                    javaClass.getDeclaredField("id").apply { isAccessible = true }.set(this, notificationId)
                }

                then("it should return the notification if it exists") {
                    doReturn(notification).`when`(notificationApplicationService)
                        .getNotification(notificationId)

                    mockMvc.perform(MockMvcRequestBuilders.get("/notifications/$notificationId"))
                        .andExpect(MockMvcResultMatchers.status().isOk)
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(notificationId))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.recipient").value(recipient))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.subject").value(subject))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.type").value(type.name))
                }

                then("it should return 400 if the notification does not exist") {
                    val nonExistingId = "some-non-existing-ID"
                    doThrow(
                        ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            EmptyResultDataAccessException(1).toString(),
                        ),
                    ).`when`(notificationApplicationService).getNotification(nonExistingId)

                    mockMvc.perform(MockMvcRequestBuilders.get("/notifications/$nonExistingId"))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest)
                }
            }

            `when`("getting all notifications") {
                val notification1 = Notification(
                    "user1@example.com",
                    "Subject 1",
                    "Message 1",
                    NotificationType.EMAIL,
                    OffsetDateTime.now(),
                ).apply {
                    javaClass.getDeclaredField("id").apply { isAccessible = true }
                        .set(this, UUID.randomUUID().toString())
                }

                val notification2 = Notification(
                    "user2@example.com",
                    "Subject 2",
                    "Message 2",
                    NotificationType.SMS,
                    OffsetDateTime.now(),
                ).apply {
                    javaClass.getDeclaredField("id").apply { isAccessible = true }
                        .set(this, UUID.randomUUID().toString())
                }

                then("it should return all notifications") {
                    doReturn(listOf(notification1, notification2)).`when`(notificationApplicationService)
                        .getAllNotifications()

                    mockMvc.perform(MockMvcRequestBuilders.get("/notifications"))
                        .andExpect(MockMvcResultMatchers.status().isOk)
                        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].recipient").value("user1@example.com"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[1].recipient").value("user2@example.com"))
                }

                then("it should return an empty list when no notifications exist") {
                    doReturn(emptyList<Notification>()).`when`(notificationApplicationService).getAllNotifications()

                    mockMvc.perform(MockMvcRequestBuilders.get("/notifications"))
                        .andExpect(MockMvcResultMatchers.status().isOk)
                        .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0))
                }
            }

            `when`("deleting a notification by ID") {
                val notificationId = UUID.randomUUID().toString()

                then("it should delete the notification successfully") {
                    mockMvc.perform(MockMvcRequestBuilders.delete("/notifications/$notificationId"))
                        .andExpect(MockMvcResultMatchers.status().isOk)
                }
            }
        }
    }
}
