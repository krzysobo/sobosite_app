package com.krzysobo.sobositeapp.http

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)


@Serializable
data class UserProfileOwnWithPasswordUpdateRequest(
    val email: String,
    val first_name: String,
    val last_name: String,
    val old_password: String,
    val new_password: String,
)


@Serializable
data class RegisterRequest(
    val email: String,
    val first_name: String,
    val last_name: String,
    val password: String,
)


@Serializable
data class ResetPasswordRequest(
    val email: String,
)


@Serializable
data class ResetPasswordConfirmRequest(
    // email and token are passed in the URL
    val new_password: String,
)


@Serializable
data class AdminUserUpdateRequest(
//    val id: String = "",  // NO ID HERE, it's being sent in the URL
    val email: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val is_staff: Boolean = false,
    val is_active: Boolean = false,
    val password: String = "",

    val no_failed_logins: Int = 0,
    val failed_is_blocked: Boolean = false,
    val failed_is_blocked_thru: String = "",
)

@Serializable
data class AdminUserCreateRequest(
    val email: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val is_staff: Boolean = false,
    val is_active: Boolean = false,
    val password: String = "",

    val no_failed_logins: Int = 0,
    val failed_is_blocked: Boolean = false,
    val failed_is_blocked_thru: String = "",
)
