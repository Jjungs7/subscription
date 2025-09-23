package com.jjungs.subscription

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SubscriptionApplication

fun main(args: Array<String>) {
    val context = runApplication<SubscriptionApplication>(*args)

//    val beanNames = context.beanDefinitionNames.sorted()
//    println("Beans provided by Spring Boot:")
//    for (beanName in beanNames) {
//        println(beanName)
//    }
}
