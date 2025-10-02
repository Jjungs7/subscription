package com.jjungs.subscription.infrastructure.customer

import com.jjungs.subscription.domain.customer.*
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.ZoneOffset

@Repository
class PostgresCustomerRepository(
    private val jdbcTemplate: JdbcTemplate,
) : CustomerRepository {

    private val rowMapper = RowMapper { rs: ResultSet, _: Int ->
        Customer(
            id = CustomerId(rs.getString("id")),
            email = Email(rs.getString("email")),
            version = rs.getLong("version"),
        ).apply {
            // Set status and timestamps using reflection
            val statusField = Customer::class.java.getDeclaredField("status")
            statusField.isAccessible = true
            statusField.set(this, CustomerStatus.valueOf(rs.getString("status")))

            val createdAtField = Customer::class.java.getDeclaredField("createdAt")
            createdAtField.isAccessible = true
            createdAtField.set(
                this, rs.getTimestamp("created_at").toLocalDateTime().atOffset(ZoneOffset.of("+09:00")),
            )

            val updatedAtField = Customer::class.java.getDeclaredField("updatedAt")
            updatedAtField.isAccessible = true
            updatedAtField.set(
                this, rs.getTimestamp("updated_at").toLocalDateTime().atOffset(ZoneOffset.of("+09:00")),
            )
        }
    }

    override fun save(customer: Customer) {
        if (customer.version == 0L) {
            // Insert new customer
            jdbcTemplate.update(
                """
                INSERT INTO customers (id, email, status, created_at, updated_at, version)
                VALUES (?, ?, ?, ?, ?, ?)
                """,
                customer.id.value,
                customer.email.value,
                customer.status.name,
                customer.createdAt,
                customer.updatedAt,
                1L,
            )
            customer.incrementVersion()
        } else {
            // Update existing customer with optimistic locking
            val rowsAffected = jdbcTemplate.update(
                """
                UPDATE customers 
                SET email = ?, status = ?, updated_at = ?, version = version + 1
                WHERE id = ? AND version = ?
                """,
                customer.email.value,
                customer.status.name,
                customer.updatedAt,
                customer.id.value,
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
            id.value,
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
        jdbcTemplate.update("DELETE FROM customers WHERE id = ?", id.value)
    }
}

class CustomerConcurrentModificationException(
    val customerId: CustomerId,
    message: String = "Status of Customer has changed. Please refresh the page and try it again.",
) : RuntimeException(message)
