package com.jjungs.subscription.domain.customer

class Phone(number: String) {
    // data class 로 변경하고싶지만, private constructor 를 사용해도 copy() 때문에 constructor 가 노출되는 문제가 있어 factory method 에 제한이 있음
    // https://youtrack.jetbrains.com/issue/KT-11914/Confusing-data-class-copy-with-private-constructor?_gl=1*1k6ie9g*_gcl_au*OTQ4NDc2ODE5LjE3NTYxMjMxMjU.*_ga*MTkwNTc1MzYxMC4xNzU2MTIzMTI3*_ga_9J976DJZ68*czE3NTk0NjQxNDckbzckZzEkdDE3NTk0NjQ2MzEkajU0JGwwJGgw&_cl=MTsxOzE7SkxXVkhyNk1RNEJZSFIwME01RXdJRHRHejMxd3hjd0lYT1RIRmVMc3Y2d2R6eWVQNUlLVklGRDM5SFZWUGc5Rjs=
    val countryCode: CountryCode
    val number: String

    init {
        val normalized = number.replace("[\\s._-]".toRegex(), "")
        if (!normalized.matches("[0-9]{10,11}".toRegex())) {
            throw IllegalArgumentException("Invalid phone number")
        }

        this.countryCode = CountryCode.KOREA
        this.number = when (normalized.length) {
            11 -> {
                "${normalized.take(3)}-${normalized.substring(3, 7)}-${normalized.substring(7)}"
            }

            10 -> {
                "${normalized.take(3)}-${normalized.substring(3, 6)}-${normalized.substring(6)}"
            }

            else -> throw IllegalArgumentException("Invalid phone number")
        }
    }

    companion object {
        fun newNumber(number: String) = Phone(number)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Phone

        if (countryCode != other.countryCode) return false
        if (number != other.number) return false

        return true
    }

    override fun hashCode(): Int {
        var result = countryCode.hashCode()
        result = 31 * result + number.hashCode()
        return result
    }

    override fun toString(): String {
        if (countryCode != CountryCode.KOREA) {
            return "$countryCode-${number.substring(1)}"
        }

        return number
    }
}

enum class CountryCode(val code: Int) {
    KOREA(82),
}
