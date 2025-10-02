package com.jjungs.subscription.interfaces.customer

import com.jjungs.subscription.application.customer.CustomerApplicationService
import com.jjungs.subscription.application.customer.CustomerNotFoundException
import com.jjungs.subscription.domain.customer.Customer
import com.jjungs.subscription.domain.customer.CustomerId
import com.jjungs.subscription.domain.customer.Email
import com.jjungs.subscription.infrastructure.customer.CustomerConcurrentModificationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/customers")
class CustomerController(
    private val customerApplicationService: CustomerApplicationService,
) {
    @GetMapping
    fun findAll(): ResponseEntity<List<CustomerResponse>> {
        val customers = customerApplicationService.getCustomers()
        return ResponseEntity.status(HttpStatus.OK).body(customers.stream().map(CustomerResponse::from).toList())
    }

    @PostMapping
    fun createCustomer(@RequestBody request: CreateCustomerRequest): ResponseEntity<CustomerResponse> {
        val customer = customerApplicationService.createCustomer(Email(request.email))
        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerResponse.from(customer))
    }

    @PutMapping("/{customerId}/activate")
    fun activateCustomer(@PathVariable customerId: String): ResponseEntity<Any> {
        return try {
            val customer = customerApplicationService.activateCustomer(CustomerId(customerId))
            ResponseEntity.ok(CustomerResponse.from(customer))
        } catch (e: CustomerConcurrentModificationException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponse(
                    error = "CONCURRENT_MODIFICATION",
                    message = e.message ?: "Status of Customer has changed. Please refresh the page and try it again.",
                    customerId = customerId,
                ),
            )
        } catch (e: CustomerNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse(
                    error = "CUSTOMER_NOT_FOUND",
                    message = e.message ?: "Customer not found",
                    customerId = customerId,
                ),
            )
        }
    }

    @PutMapping("/{customerId}/suspend")
    fun suspendCustomer(@PathVariable customerId: String): ResponseEntity<Any> {
        return try {
            val customer = customerApplicationService.suspendCustomer(CustomerId(customerId))
            ResponseEntity.ok(CustomerResponse.from(customer))
        } catch (e: CustomerConcurrentModificationException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponse(
                    error = "CONCURRENT_MODIFICATION",
                    message = e.message ?: "Status of Customer has changed. Please refresh the page and try it again.",
                    customerId = customerId,
                ),
            )
        } catch (e: CustomerNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse(
                    error = "CUSTOMER_NOT_FOUND",
                    message = e.message ?: "Customer not found",
                    customerId = customerId,
                ),
            )
        }
    }

    @PutMapping("/{customerId}/deactivate")
    fun deactivateCustomer(@PathVariable customerId: String): ResponseEntity<Any> {
        return try {
            val customer = customerApplicationService.deactivateCustomer(CustomerId(customerId))
            ResponseEntity.ok(CustomerResponse.from(customer))
        } catch (e: CustomerConcurrentModificationException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponse(
                    error = "CONCURRENT_MODIFICATION",
                    message = e.message ?: "Status of Customer has changed. Please refresh the page and try it again.",
                    customerId = customerId,
                ),
            )
        } catch (e: CustomerNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse(
                    error = "CUSTOMER_NOT_FOUND",
                    message = e.message ?: "Customer not found",
                    customerId = customerId,
                ),
            )
        }
    }

    @PutMapping("/{customerId}/email")
    fun updateCustomerEmail(
        @PathVariable customerId: String,
        @RequestBody request: UpdateEmailRequest,
    ): ResponseEntity<Any> {
        return try {
            val customer = customerApplicationService.updateCustomerEmail(
                CustomerId(customerId),
                Email(request.email),
            )
            ResponseEntity.ok(CustomerResponse.from(customer))
        } catch (e: CustomerConcurrentModificationException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(
                ErrorResponse(
                    error = "CONCURRENT_MODIFICATION",
                    message = e.message ?: "Status of Customer has changed. Please refresh the page and try it again.",
                    customerId = customerId,
                ),
            )
        } catch (e: CustomerNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponse(
                    error = "CUSTOMER_NOT_FOUND",
                    message = e.message ?: "Customer not found",
                    customerId = customerId,
                ),
            )
        }
    }
}

data class CreateCustomerRequest(val email: String)
data class UpdateEmailRequest(val email: String)
data class CustomerResponse(
    val id: String,
    val email: String,
    val status: String,
    val version: Long,
    val createdAt: String,
    val updatedAt: String,
) {
    companion object {
        fun from(customer: Customer): CustomerResponse {
            return CustomerResponse(
                id = customer.id.value,
                email = customer.email.value,
                status = customer.status.name,
                version = customer.version,
                createdAt = customer.createdAt.toString(),
                updatedAt = customer.updatedAt.toString(),
            )
        }
    }
}

data class ErrorResponse(
    val error: String,
    val message: String,
    val customerId: String? = null,
)
