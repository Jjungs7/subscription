package com.jjungs.subscription.domain.customer

class Name(firstName: String, lastName: String) {
    // data class 로 변경하고싶지만, private constructor 를 사용해도 copy() 때문에 constructor 가 노출되는 문제가 있어 factory method 에 제한이 있음
    // https://youtrack.jetbrains.com/issue/KT-11914/Confusing-data-class-copy-with-private-constructor?_gl=1*1k6ie9g*_gcl_au*OTQ4NDc2ODE5LjE3NTYxMjMxMjU.*_ga*MTkwNTc1MzYxMC4xNzU2MTIzMTI3*_ga_9J976DJZ68*czE3NTk0NjQxNDckbzckZzEkdDE3NTk0NjQ2MzEkajU0JGwwJGgw&_cl=MTsxOzE7SkxXVkhyNk1RNEJZSFIwME01RXdJRHRHejMxd3hjd0lYT1RIRmVMc3Y2d2R6eWVQNUlLVklGRDM5SFZWUGc5Rjs=
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
