package com.jjungs.subscription.infrastructure.customer

import com.jjungs.subscription.domain.customer.*
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.ZoneOffset

@Repository
class PostgresCustomerRepository(
    private val jdbcTemplate: JdbcTemplate,
) : CustomerRepository {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val rowMapper = RowMapper { rs: ResultSet, _: Int ->
        fun setPrivateField(obj: Any, fieldName: String, value: Any?) {
            try {
                val field = obj.javaClass.getDeclaredField(fieldName)
                field.isAccessible = true
                field.set(obj, value)
            } catch (e: Exception) {
                logger.error("Could not set field $fieldName", e)
            }
        }

        Customer(
            id = CustomerId(rs.getString("id")),
            email = Email(rs.getString("email")),
            version = rs.getLong("version"),
        ).apply {
            setPrivateField(this, "status", rs.getString("status"))
            setPrivateField(
                this,
                "createdAt",
                rs.getTimestamp("created_at").toLocalDateTime().atOffset(ZoneOffset.of("+09:00")),
            )
            setPrivateField(
                this,
                "updatedAt",
                rs.getTimestamp("updated_at").toLocalDateTime().atOffset(ZoneOffset.of("+09:00")),
            )
        }
    }

    override fun save(customer: Customer) {
        if (customer.version == 0L) {
            jdbcTemplate.update(
                """
                INSERT INTO customers (id, email, status, created_at, updated_at, version)
                VALUES (?, ?, ?, ?, ?, ?)
                """,
                customer.id.id,
                customer.email.value,
                customer.status.name,
                customer.createdAt,
                customer.updatedAt,
                1L,
            )
            customer.incrementVersion()
        } else {
            val rowsAffected = jdbcTemplate.update(
                """
                UPDATE customers 
                SET email = ?, status = ?, updated_at = ?, version = version + 1
                WHERE id = ? AND version = ?
                """,
                customer.email.value,
                customer.status.name,
                customer.updatedAt,
                customer.id.id,
                customer.version,
            )

            if (rowsAffected == 0) {
                throw CustomerConcurrentModificationException(customer.id)
            }

            customer.incrementVersion()
        }
    }

    override fun findById(id: CustomerId): Customer? {
        return jdbcTemplate.queryForObject(
            "SELECT id, email, status, created_at, updated_at, version FROM customers WHERE id = ?",
            rowMapper,
            id.id,
        )
    }

    override fun findByEmail(email: Email): Customer? {
        return jdbcTemplate.queryForObject(
            "SELECT id, email, status, created_at, updated_at, version FROM customers WHERE email = ?",
            rowMapper,
            email.value,
        )
    }

    override fun findByStatus(status: CustomerStatus): List<Customer> {
        return jdbcTemplate.query(
            "SELECT id, email, status, created_at, updated_at, version FROM customers WHERE status = ?",
            rowMapper,
            status.name,
        )
    }

    override fun findAll(): List<Customer> {
        return jdbcTemplate.query(
            "SELECT id, email, status, created_at, updated_at, version FROM customers",
            rowMapper,
        )
    }

    override fun deleteById(id: CustomerId) {
        val rowsAffected = jdbcTemplate.update("DELETE FROM customers WHERE id = ?", id.id)
        if (rowsAffected == 0) {
            throw CustomerNotFoundException(id)
        }
    }
}

class CustomerNotFoundException(
    val customerId: CustomerId,
    message: String = "Customer not found with id: ${customerId.id}",
) : RuntimeException(message)

class CustomerConcurrentModificationException(
    val customerId: CustomerId,
    message: String = "Status of Customer has changed. Please refresh the page and try it again.",
) : RuntimeException(message)
