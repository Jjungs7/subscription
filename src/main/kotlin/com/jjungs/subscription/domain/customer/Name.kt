package com.jjungs.subscription.domain.customer

class Name(firstName: String, lastName: String) {
    init {
        require(firstName.isNotBlank()) { "Name must not be blank" }
        require(lastName.isNotBlank()) { "Name must not be blank" }
    }

    val firstName: String = firstName.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

    val lastName: String = lastName.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

    val fullName: String
        get() = "$lastName$firstName"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Name

        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        return result
    }

    override fun toString(): String = fullName
}

fun Name.setFirstName(firstName: String) = Name(firstName, this.lastName)

fun Name.setLastName(lastName: String) = Name(this.firstName, lastName)

fun Name.setName(firstName: String, lastName: String) = Name(firstName, lastName)
