package com.jjungs.subscription.application.notification

import com.jjungs.subscription.domain.notification.Notification
import com.jjungs.subscription.domain.notification.NotificationRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class NotificationApplicationService(
    private val notificationAdapterFactory: NotificationAdapterFactory,
    private val notificationRepository: NotificationRepository,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

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

    fun saveNotification(notification: Notification): Notification {
        notificationRepository.save(notification)
        return notification
    }

    @Transactional
    fun sendNotification(notification: Notification): Notification {
        // First transaction: Save notification
        saveNotification(notification)
        sendAndSaveNotification(notification)

        return notification
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private fun sendAndSaveNotification(notification: Notification) {
        val adapter = notificationAdapterFactory.getAdapter(notification.type)

        runCatching {
            adapter.send(notification)
            notification.markAsSent()
            saveNotification(notification)
        }.onFailure {
            notification.markAsFailed()
            saveNotification(notification)
        }
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
