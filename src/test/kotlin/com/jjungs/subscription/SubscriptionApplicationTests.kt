package com.jjungs.subscription

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(properties = [
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop"
])
class SubscriptionApplicationTests : AnnotationSpec() {

    override fun extensions() = listOf(
        SpringTestExtension(SpringTestLifecycleMode.Root)
    )

    @Test
    fun contextLoads() {
    }
}