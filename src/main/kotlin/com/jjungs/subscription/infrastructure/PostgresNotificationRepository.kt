package com.jjungs.subscription.infrastructure

import com.jjungs.subscription.domain.notification.Notification
import com.jjungs.subscription.domain.notification.NotificationRepository
import com.jjungs.subscription.domain.notification.NotificationStatus
import com.jjungs.subscription.domain.notification.NotificationType
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class PostgresNotificationRepository(private val jdbcTemplate: JdbcTemplate) : NotificationRepository {

    private val rowMapper = RowMapper { rs: ResultSet, _: Int ->
        Notification(
            recipient = rs.getString("recipient"),
            subject = rs.getString("subject"),
            message = rs.getString("message"),
            type = NotificationType.valueOf(rs.getString("type")),
            timestamp = rs.getTimestamp("timestamp").toLocalDateTime(),
        ).apply {
            val idField = Notification::class.java.getDeclaredField("id")
            idField.isAccessible = true
            idField.set(this, rs.getString("id"))

            val statusField = Notification::class.java.getDeclaredField("status")
            statusField.isAccessible = true
            statusField.set(this, NotificationStatus.valueOf(rs.getString("status")))
        }
    }

    override fun save(notification: Notification) {
        jdbcTemplate.update(
            "INSERT INTO notifications (id, recipient, subject, message, type, timestamp, status)" +
                    " VALUES(?, ?, ?, ?, ?, ?, ?)",
            notification.id,
            notification.recipient,
            notification.subject,
            notification.message,
            notification.type.name,
            notification.timestamp,
            notification.status.name,
        )
    }

    override fun findById(id: String): Notification? {
        return try {
            jdbcTemplate.queryForObject(
                "SELECT id, recipient, subject, message, type, timestamp, status FROM notifications WHERE id = ?",
                rowMapper,
                id,
            )
        } catch (_: IncorrectResultSizeDataAccessException) {
            null
        }
    }

    override fun findAll(): List<Notification> {
        return jdbcTemplate.query(
            "SELECT id, recipient, subject, message, type, timestamp, status FROM notifications",
            rowMapper,
        )
    }

    override fun deleteById(id: String) {
        jdbcTemplate.update("DELETE FROM notifications WHERE id = ?", id)
    }
}
