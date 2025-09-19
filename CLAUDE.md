# Subscription Billing System - DDD & Hexagonal Architecture

## Project Overview
This is a **Subscription Billing System** built with **Domain-Driven Design (DDD)** and **Hexagonal Architecture (Ports & Adapters)**. The system handles recurring payments, plan management, billing cycles, and payment processing with external integrations.

## Technology Stack
- **Infrastructure**: Kubernetes (in-house) + Dockerfile
- **Application**: Spring Boot + Kotlin + Java 21
- **Database**: PostgreSQL
- **Build Tool**: Gradle (Kotlin DSL)

## Architecture Layers

### 1. Domain Layer (Pure Kotlin)
**Location**: `src/main/kotlin/com/jjungs/subscription/domain/`

Contains pure business logic with no framework dependencies:
- **Entities**: `Subscription`, `Invoice`, `Customer`
- **Value Objects**: `Money`, `BillingCycle`, `PaymentStatus`
- **Domain Services**: `BillingService`, `PaymentService`
- **Ports (Interfaces)**: `SubscriptionRepository`, `PaymentPort`, `NotificationPort`

### 2. Application Layer
**Location**: `src/main/kotlin/com/jjungs/subscription/application/`

Use cases and services that orchestrate domain objects:
- Application services that coordinate domain entities
- Transaction boundaries
- Event publishing

### 3. Infrastructure Layer
**Location**: `src/main/kotlin/com/jjungs/subscription/infrastructure/`

Concrete implementations of domain ports:
- `PostgresSubscriptionRepository` implements `SubscriptionRepository`
- `StripePaymentAdapter` implements `PaymentPort`
- `PaypalPaymentAdapter` implements `PaymentPort`
- `EmailNotificationAdapter` implements `NotificationPort`

### 4. Interface Layer (Inbound Adapters)
**Location**: `src/main/kotlin/com/jjungs/subscription/interfaces/`

How external systems interact with the application:
- REST controllers
- Kafka consumers
- GraphQL resolvers

## Bounded Contexts

### 1. Subscription/Billing Context (Main)
- Manages subscriptions, invoices, charges
- Core domain with richest business logic
- Handles payment provider integrations

### 2. Customer/Account Context
- Manages users, authentication, profiles
- Publishes events: `UserCreated`, `UserDeactivated`

### 3. Notification Context
- Listens to billing events
- Sends emails/SMS notifications

## Key Design Principles

### Hexagonal Architecture Rules
1. **Domain layer must be pure** - no Spring annotations, no database dependencies
2. **Dependency direction** - Domain defines ports, infrastructure implements them
3. **Adapters are replaceable** - Mock adapters for testing, real adapters for production
4. **Domain events** - Use events for cross-context communication

### DDD Patterns
1. **Entities** - Have identity and lifecycle (Subscription, Customer)
2. **Value Objects** - Immutable, no identity (Money, BillingCycle)
3. **Aggregates** - Consistency boundaries (Subscription aggregate)
4. **Domain Services** - Business logic that doesn't belong to entities
5. **Repositories** - Abstract persistence concerns

## Real-World Challenges to Address

### 1. Idempotency
- Payment provider retries must not create duplicate charges
- Use idempotency keys for all external API calls

### 2. Eventual Consistency
- Kafka events between bounded contexts
- Handle out-of-order event processing

### 3. Error Handling
- Payment gateway downtime
- Retry mechanisms with exponential backoff
- Dead letter topics for failed events

### 4. Testing Strategy
- **Unit tests** - Domain logic in isolation
- **Integration tests** - Adapter implementations
- **Contract tests** - Between bounded contexts
- **End-to-end tests** - Full payment flows

## File Naming Conventions
- **Entities**: `Subscription.kt`, `Invoice.kt`
- **Value Objects**: `Money.kt`, `BillingCycle.kt`
- **Ports**: `SubscriptionRepository.kt`, `PaymentPort.kt`
- **Adapters**: `PostgresSubscriptionRepository.kt`, `StripePaymentAdapter.kt`
- **Controllers**: `SubscriptionController.kt`, `BillingController.kt`

## Database Considerations
- Use Flyway for schema migrations
- Design for audit trails (created_at, updated_at, version)
- Consider event sourcing for critical business events
- Plan for data archival strategies

## Kubernetes Deployment
- Health checks via Spring Boot Actuator
- ConfigMaps for database credentials
- Secrets for payment provider API keys
- Resource limits and requests
- Horizontal pod autoscaling based on metrics

## Development Workflow
1. Start with domain entities and value objects
2. Define ports (interfaces) in domain layer
3. Implement adapters in infrastructure layer
4. Create application services for use cases
5. Add REST controllers and Kafka listeners
6. Write tests for each layer
7. Configure Spring Boot for dependency injection

## Common Anti-Patterns to Avoid
- Don't put business logic in controllers
- Don't leak infrastructure concerns into domain
- Don't use anemic domain models
- Don't create god services that do everything
- Don't ignore event ordering and consistency

## Writing code

always follow these four steps

1. write tests codes. do not write any implementations
2. git commit tests codes. do not git push
3. write implementations
4. git commit implementations. do not git push
