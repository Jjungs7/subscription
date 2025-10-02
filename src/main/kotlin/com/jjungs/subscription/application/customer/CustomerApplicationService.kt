package com.jjungs.subscription.application.customer

import com.jjungs.subscription.domain.customer.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomerApplicationService(
    private val customerRepository: CustomerRepository,
    private val customerEventPublisher: CustomerEventPublisher,
) {
    fun getCustomerById(customerId: CustomerId): Customer {
        return customerRepository.findById(customerId) ?: throw CustomerNotFoundException(
            customerId,
            "Customer not found",
        )
    }

    fun getCustomers(): List<Customer> {
        return customerRepository.findAll()
    }

    @Transactional
    fun createCustomer(email: Email): Customer {
        val customer = Customer(CustomerId(), email)
        customerRepository.save(customer)

        customerEventPublisher.publish(
            CustomerCreated(customer.id, customer.createdAt, customer.email),
        )
        return customer
    }

    @Transactional
    fun activateCustomer(customerId: CustomerId): Customer {
        val customer = getCustomerById(customerId)

        customer.activate()
        customerRepository.save(customer)

        customerEventPublisher.publish(
            CustomerActivated(customer.id, customer.createdAt, customer.email),
        )
        return customer
    }

    @Transactional
    fun suspendCustomer(customerId: CustomerId): Customer {
        val customer = getCustomerById(customerId)

        customer.suspend()
        customerRepository.save(customer)

        customerEventPublisher.publish(
            CustomerSuspended(customer.id, customer.createdAt, customer.email),
        )
        return customer
    }

    @Transactional
    fun deactivateCustomer(customerId: CustomerId): Customer {
        val customer = getCustomerById(customerId)

        customer.deactivate()
        customerRepository.save(customer)

        customerEventPublisher.publish(
            CustomerDeactivated(customer.id, customer.createdAt, customer.email),
        )
        return customer
    }

    @Transactional
    fun updateCustomerEmail(customerId: CustomerId, newEmail: Email): Customer {
        val customer = getCustomerById(customerId)
        val oldEmail = customer.email

        customer.updateEmail(newEmail)
        customerRepository.save(customer)

        customerEventPublisher.publish(
            CustomerEmailUpdated(customer.id, customer.createdAt, oldEmail, customer.email),
        )
        return customer
    }
}

class CustomerNotFoundException(val customerId: CustomerId, message: String) : RuntimeException(message)
