package com.jjungs.subscription.domain.customer

import com.jjungs.subscription.domain.vo.Name
import com.jjungs.subscription.domain.vo.Phone
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.OffsetDateTime
import java.time.ZoneOffset

class CustomerProfileTest : StringSpec(
    {
        "should create customer profile with required properties" {
            val customerId = CustomerId()
            val firstName = "명"
            val lastName = "청"
            val phoneNumber = "01012345678"

            val profile = CustomerProfile(customerId, Name(firstName, lastName), Phone(phoneNumber))

            profile.customerId shouldBe customerId
            profile.name.firstName shouldBe firstName
            profile.name.lastName shouldBe lastName
            profile.phoneNumber shouldBe Phone(phoneNumber)
            profile.createdAt shouldBe profile.updatedAt
        }

        "should create customer profile with optional fields" {
            val customerId = CustomerId()
            val firstName = "명"
            val lastName = "청"
            val phoneNumber = "01012345678"
            val address = "서울시 경기구 인천동"
            val dateOfBirth = OffsetDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneOffset.of("+09:00"))

            val profile = CustomerProfile(
                customerId,
                Name(firstName, lastName),
                Phone(phoneNumber),
                address,
                dateOfBirth,
            )

            profile.customerId shouldBe customerId
            profile.name.firstName shouldBe firstName
            profile.name.lastName shouldBe lastName
            profile.phoneNumber shouldBe Phone(phoneNumber)
            profile.address shouldBe address
            profile.dateOfBirth shouldBe dateOfBirth
            profile.createdAt shouldBe profile.updatedAt
        }

        "should throw exception for blank firstName" {
            val customerId = CustomerId()
            val lastName = "청"
            val phoneNumber = "01012345678"

            shouldThrow<IllegalArgumentException> {
                CustomerProfile(customerId, Name("", lastName), Phone(phoneNumber))
            }
        }

        "should throw exception for blank lastName" {
            val customerId = CustomerId()
            val firstName = "명"
            val phoneNumber = "01012345678"

            shouldThrow<IllegalArgumentException> {
                CustomerProfile(customerId, Name(firstName, ""), Phone(phoneNumber))
            }
        }

        "should throw exception for blank phoneNumber" {
            val customerId = CustomerId()
            val firstName = "명"
            val lastName = "청"

            shouldThrow<IllegalArgumentException> {
                CustomerProfile(customerId, Name(firstName, lastName), Phone(""))
            }
        }

        "should update firstName" {
            val profile = CustomerProfile(
                CustomerId(),
                Name("명", "청"),
                Phone("01012345678"),
            )
            val newFirstName = "묭"
            val originalUpdatedAt = profile.updatedAt

            profile.updateFirstName(newFirstName)

            profile.name.firstName shouldBe newFirstName
            profile.updatedAt shouldNotBe originalUpdatedAt
        }

        "should update lastName" {
            val profile = CustomerProfile(
                CustomerId(),
                Name("명", "청"),
                Phone("01012345678"),
            )
            val newLastName = "백"
            val originalUpdatedAt = profile.updatedAt

            profile.updateLastName(newLastName)

            profile.name.lastName shouldBe newLastName
            profile.updatedAt shouldNotBe originalUpdatedAt
        }

        "should update phoneNumber" {
            val profile = CustomerProfile(
                CustomerId(),
                Name("명", "청"),
                Phone("01012345678"),
            )
            val newPhoneNumber = "01098765432"
            val originalUpdatedAt = profile.updatedAt

            profile.updatePhoneNumber(Phone(newPhoneNumber))

            profile.phoneNumber shouldBe Phone(newPhoneNumber)
            profile.updatedAt shouldNotBe originalUpdatedAt
        }

        "should update address" {
            val profile = CustomerProfile(
                CustomerId(),
                Name("명", "청"),
                Phone("01012345678"),
            )
            val newAddress = "경기시 인천구 서울동"
            val originalUpdatedAt = profile.updatedAt

            profile.updateAddress(newAddress)

            profile.address shouldBe newAddress
            profile.updatedAt shouldNotBe originalUpdatedAt
        }

        "should update dateOfBirth" {
            val profile = CustomerProfile(
                CustomerId(),
                Name("명", "청"),
                Phone("01012345678"),
            )
            val newDateOfBirth = OffsetDateTime.of(1985, 5, 15, 0, 0, 0, 0, ZoneOffset.of("+09:00"))
            val originalUpdatedAt = profile.updatedAt

            profile.updateDateOfBirth(newDateOfBirth)

            profile.dateOfBirth shouldBe newDateOfBirth
            profile.updatedAt shouldNotBe originalUpdatedAt
        }

        "should throw exception when updating firstName to blank" {
            val profile = CustomerProfile(
                CustomerId(),
                Name("명", "청"),
                Phone("01012345678"),
            )

            shouldThrow<IllegalArgumentException> {
                profile.updateFirstName("")
            }
        }

        "should throw exception when updating lastName to blank" {
            val profile = CustomerProfile(
                CustomerId(),
                Name("명", "청"),
                Phone("01012345678"),
            )

            shouldThrow<IllegalArgumentException> {
                profile.updateLastName("")
            }
        }

        "should throw exception when updating phoneNumber to blank" {
            val profile = CustomerProfile(
                CustomerId(),
                Name("명", "청"),
                Phone("01012345678"),
            )

            shouldThrow<IllegalArgumentException> {
                profile.updatePhoneNumber(Phone(""))
            }
        }

        "should be equal when profiles have same customerId" {
            val customerId = CustomerId("same-id")
            val firstName1 = "명"
            val firstName2 = "묭"
            val lastName = "청"
            val phoneNumber = "01012345678"

            val profile1 = CustomerProfile(customerId, Name(firstName1, lastName), Phone(phoneNumber))
            val profile2 = CustomerProfile(customerId, Name(firstName2, lastName), Phone(phoneNumber))

            profile1 shouldBe profile2
            profile1.hashCode() shouldBe profile2.hashCode()
        }

        "should not be equal when profiles have different customerIds" {
            val customerId1 = CustomerId("id-1")
            val customerId2 = CustomerId("id-2")
            val firstName = "명"
            val lastName = "청"
            val phoneNumber = "01012345678"

            val profile1 = CustomerProfile(customerId1, Name(firstName, lastName), Phone(phoneNumber))
            val profile2 = CustomerProfile(customerId2, Name(firstName, lastName), Phone(phoneNumber))

            profile1 shouldNotBe profile2
            profile1.hashCode() shouldNotBe profile2.hashCode()
        }

        "should update timestamp when any field changes" {
            val profile = CustomerProfile(
                CustomerId(),
                Name("명", "청"),
                Phone("01012345678"),
            )
            val originalUpdatedAt = profile.updatedAt

            Thread.sleep(1)

            profile.updateFirstName("묭")

            profile.updatedAt shouldNotBe originalUpdatedAt
            profile.updatedAt.isAfter(originalUpdatedAt) shouldBe true
        }

        "should get full name correctly" {
            val profile = CustomerProfile(
                CustomerId(),
                Name("명", "청"),
                Phone("01012345678"),
            )

            profile.name.fullName shouldBe "${profile.name.lastName}${profile.name.firstName}"
        }
    },
)
