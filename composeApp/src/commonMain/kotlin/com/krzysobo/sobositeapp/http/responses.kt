package com.krzysobo.sobositeapp.http

import kotlinx.serialization.Serializable


@Serializable
data class DataResponse<T>(val data: T?, val errors: List<List<String>>?)


@Serializable
data class LoginResponse(
    val id: String,
    val status: Int,
    val email: String,
    val token: String,
    val first_name: String,
    val last_name: String,
    val is_staff: Boolean,
)


@Serializable
data class AdminUserListResponse(
    val items: List<AdminUserModel> = emptyList(),
    val count: Int = 0,
)


@Serializable
data class ProfileOwnGetResponse(
    val id: String = "",
    val status: Int = 10,
    val email: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val role: String = "USR",   // USR - user, ADM - admin
    val is_staff: Boolean = false,
)

@Serializable
data class ProfileOwnUpdateResponse(
    val id: String = "",
    val status: Int = 10,
    val email: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val role: String = "USR",   // USR - user, ADM - admin
    val is_staff: Boolean = false,
)

@Serializable
data class AdminUserModel(
    val id: String = "",
    val status: Int = 10,
    val email: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val role: String = "USR",   // USR - user, ADM - admin
    val is_staff: Boolean = false,
    val is_active: Boolean = false,
    val no_failed_logins: Int = 0,
    val failed_is_blocked: Boolean = false,
    val failed_is_blocked_thru: String = "",
) {
    fun fieldsForList(): List<Any> {
        return listOf(
            email, first_name, last_name, is_active, is_staff
        )
    }
}


@Serializable
data class RegisterCreateResponse(
    val email: String = "",
    val first_name: String = "",
    val last_name: String = "",
)
