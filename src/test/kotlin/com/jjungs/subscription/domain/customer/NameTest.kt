package com.jjungs.subscription.domain.customer

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class NameTest : StringSpec(
    {

        "should create Name with valid English values" {
            val firstName = "Jjungs"
            val lastName = "Yy"

            val name = Name(firstName, lastName)

            name.firstName shouldBe "Jjungs"
            name.lastName shouldBe "Yy"
            name.fullName shouldBe "YyJjungs"
        }

        "should create Name with valid Korean values" {
            val firstName = "명"
            val lastName = "청"

            val name = Name(firstName, lastName)

            name.firstName shouldBe "명"
            name.lastName shouldBe "청"
            name.fullName shouldBe "청명"
        }

        "should throw exception on invalid inputs" {
            shouldThrow<IllegalArgumentException> {
                Name("", "청")
            }.message shouldBe "Name must not be blank"

            shouldThrow<IllegalArgumentException> {
                Name("명", "")
            }.message shouldBe "Name must not be blank"

            shouldThrow<IllegalArgumentException> {
                Name("   ", "청")
            }.message shouldBe "Name must not be blank"

            shouldThrow<IllegalArgumentException> {
                Name("명", "   ")
            }.message shouldBe "Name must not be blank"
        }

        "should be equal when firstName and lastName are the same" {
            val name1 = Name("명", "청")
            val name2 = Name("명", "청")

            name1 shouldBe name2
            name1.hashCode() shouldBe name2.hashCode()
        }

        "should be equal to itself" {
            val name = Name("명", "청")
            name shouldBe name
        }

        "should not be equal when Name is different" {
            Name("명", "청") shouldNotBe Name("묭", "청")
            Name("명", "청") shouldNotBe Name("명", "백")
        }

        "should not be equal to null" {
            val name = Name("명", "청")
            name shouldNotBe null
        }

        "should not be equal to different type" {
            val name = Name("명", "청")
            name shouldNotBe "명 청"
        }

        "should handle case insensitive equality correctly" {
            val name1 = Name("john", "doe")
            val name2 = Name("John", "Doe")

            name1 shouldBe name2
        }

        "should create new Name with setFirstName extension function" {
            val originalName = Name("명", "청")
            val newName = originalName.setFirstName("묭")

            newName.firstName shouldBe "묭"
            newName.lastName shouldBe "청"
            newName.fullName shouldBe "청묭"
        }

        "should create new Name with setLastName extension function" {
            val originalName = Name("명", "청")
            val newName = originalName.setLastName("백")

            newName.firstName shouldBe "명"
            newName.lastName shouldBe "백"
            newName.fullName shouldBe "백명"
        }

        "should create new Name with setName extension function" {
            val originalName = Name("명", "청")
            val newName = originalName.setName("묭", "백")

            newName.firstName shouldBe "묭"
            newName.lastName shouldBe "백"
            newName.fullName shouldBe "백묭"
        }

        "should maintain immutability with extension functions" {
            val originalName = Name("명", "청")
            val newName = originalName.setFirstName("묭")

            // Original should remain unchanged
            originalName.firstName shouldBe "명"
            originalName.lastName shouldBe "청"

            // New name should have new values
            newName.firstName shouldBe "묭"
            newName.lastName shouldBe "청"
        }

        "should handle very long names" {
            val longFirstName = "A".repeat(100)
            val longLastName = "B".repeat(100)

            val name = Name(longFirstName, longLastName)

            name.firstName shouldBe longFirstName
            name.lastName shouldBe longLastName
            name.fullName shouldBe "$longLastName$longFirstName"
        }

        "should handle names with numbers" {
            val name = Name("명2", "청3")
            name.firstName shouldBe "명2"
            name.lastName shouldBe "청3"
            name.fullName shouldBe "청3명2"
        }

        "should return fullName when toString is called" {
            val name = Name("명", "청")
            name.toString() shouldBe "청명"
        }

        "should have consistent toString and fullName" {
            val name = Name("묭", "백")
            name.toString() shouldBe name.fullName
        }
    },
)
