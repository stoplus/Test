package com.test.network.models.domain

data class UserResult(
    var id: Int = 0,
    var username: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var photo: String = ""
) {
    companion object {
        fun emptyModel() =
            UserResult(
                id = 0,
                username = "",
                firstName = "",
                lastName = "",
                email = "",
                photo = ""
            )
    }
}
