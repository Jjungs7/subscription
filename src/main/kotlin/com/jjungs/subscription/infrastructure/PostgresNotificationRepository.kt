package com.jjungs.subscription.infrastructure

import com.jjungs.subscription.domain.notification.Notification
import com.jjungs.subscription.domain.notification.NotificationRepository
import com.jjungs.subscription.domain.notification.NotificationStatus
import com.jjungs.subscription.domain.notification.NotificationType
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
        val rowsAffected = jdbcTemplate.update(
            "UPDATE notifications SET recipient = ?, subject = ?, message = ?, type = ?, timestamp = ?, status = ? WHERE id = ?",
            notification.recipient,
            notification.subject,
            notification.message,
            notification.type.name,
            notification.timestamp,
            notification.status.name,
            notification.id
        )

        if (rowsAffected == 0) {
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
    }

    override fun findById(id: String): Notification? {
        return jdbcTemplate.queryForObject(
            "SELECT id, recipient, subject, message, type, timestamp, status FROM notifications WHERE id = ?",
            rowMapper,
            id,
        )
    }

    override fun findAll(): List<Notification> {
        return jdbcTemplate.query(
            "SELECT id, recipient, subject, message, type, timestamp, status FROM notifications",
            rowMapper,
        )
    }

    override fun deleteById(id: String) {
        val notification =
            findById(id) ?: throw Exception("this should not return null. maybe rowmapper is not correct")
        jdbcTemplate.update("DELETE FROM notifications WHERE id = ?", notification.id)
    }
}
