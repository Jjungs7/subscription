package com.jjungs.subscription.domain.customer

import com.jjungs.subscription.domain.vo.Email

interface CustomerRepository {
    fun save(customer: Customer)
    fun findById(id: CustomerId): Customer?
    fun findByEmail(email: Email): Customer?
    fun findByStatus(status: CustomerStatus): List<Customer>
    fun findAll(): List<Customer>
    fun deleteById(id: CustomerId)
}
