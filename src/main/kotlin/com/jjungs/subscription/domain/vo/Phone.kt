package com.jjungs.subscription.domain.vo

class Phone(number: String) {
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
