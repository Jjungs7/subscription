package com.jjungs.subscription.application.notification

import com.jjungs.subscription.domain.notification.Notification
import com.jjungs.subscription.domain.notification.NotificationRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class NotificationApplicationService(
    private val notificationRepository: NotificationRepository,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun saveNotification(notification: Notification): Notification {
        notificationRepository.save(notification)
        return notification
    }

    fun getNotification(id: String): Notification? {
        return runCatching {
            notificationRepository.findById(id)
        }.onFailure {
            logger.debug(it.stackTraceToString())
            when (it) {
                is EmptyResultDataAccessException -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, it.message)
                is IncorrectResultSizeDataAccessException -> throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    it.message,
                )
            }
        }.getOrThrow()
    }

    fun getAllNotifications(): List<Notification> {
        return notificationRepository.findAll()
    }

    fun deleteNotification(id: String) {
        runCatching {
            notificationRepository.deleteById(id)
        }.onFailure {
            logger.debug(it.stackTraceToString())
            when (it) {
                is EmptyResultDataAccessException -> throw ResponseStatusException(HttpStatus.BAD_REQUEST, it.message)
                is IncorrectResultSizeDataAccessException -> throw ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    it.message,
                )
            }
        }
    }
}
