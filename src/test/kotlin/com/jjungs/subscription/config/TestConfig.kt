package com.jjungs.subscription.config

import com.jjungs.subscription.application.notification.NotificationApplicationService
import com.jjungs.subscription.domain.notification.NotificationPort
import com.jjungs.subscription.domain.notification.NotificationRepository
import com.jjungs.subscription.infrastructure.FakeNotificationPort
import org.mockito.Mockito.mock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestConfig {

    @Bean
    fun notificationPort(): NotificationPort {
        return FakeNotificationPort()
    }

    @Bean
    fun notificationRepository(): NotificationRepository {
        return mock(NotificationRepository::class.java)
    }

    @Bean
    fun notificationApplicationService(): NotificationApplicationService {
        return mock(NotificationApplicationService::class.java)
    }
}
