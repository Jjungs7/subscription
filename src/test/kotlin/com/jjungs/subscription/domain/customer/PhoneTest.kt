package com.jjungs.subscription.domain.customer

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PhoneTest : StringSpec(
    {
        "should have either 010-0000-0000 or 010-000-0000 format" {
            Phone("01012341234").number shouldBe "010-1234-1234"
            Phone("0101231234").number shouldBe "010-123-1234"
        }

        "should gracefully transform non-standard delimiters" {
            Phone("010_1234_1234").number shouldBe "010-1234-1234"
            Phone("010.1234.1234").number shouldBe "010-1234-1234"
        }

        "should remove all whitespaces" {
            Phone(" 01012341234").number shouldBe "010-1234-1234"
            Phone("01012341234 ").number shouldBe "010-1234-1234"
            Phone("010 1234 1234").number shouldBe "010-1234-1234"
            Phone("01 012 341 234").number shouldBe "010-1234-1234"
        }

        "should throw on invalid digits" {
            shouldThrow<IllegalArgumentException> {
                Phone("010/1234/1234")
                Phone("010#1234#1234")
            }.message shouldBe "Invalid phone number"
        }
    },
)
