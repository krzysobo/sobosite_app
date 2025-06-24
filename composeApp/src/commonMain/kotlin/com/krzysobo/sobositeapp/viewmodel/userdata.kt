package com.krzysobo.sobositeapp.viewmodel

import kotlin.time.TimeSource
import kotlin.time.TimeSource.Monotonic.markNow

data class UserData(
    val id: String = "",
    val status: Int = 10,
    val email: String = "",
    val token: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val isStaff: Boolean = false
)

data class FullUserData(
    val id: String = "",
    val status: Int = 10,
    val email: String = "",
    val token: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val isStaff: Boolean = false,
    val isActive: Boolean = false,
    val role: String = "",
    val noFailedLogins: Int = 0,
    val failedIsBlocked: Boolean = false,
    val failedIsBlockedThru: String = "",
)



data class LoginUiState(
    val userData: UserData = UserData(),
    val isLoggedIn: Boolean = false,
    val isAdmin: Boolean = false,
    val token: String = "",
    val loginTs: TimeSource.Monotonic.ValueTimeMark = markNow()
)

data class SobositeRegisterUiState(
    val isRegistered: Boolean = false,
    val email: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val error: String = "",
)

data class SobositeUpdateUiState(
    val isUpdated: Boolean = false,
    val email: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val error: String = "",
)

data class SobositeRegisterConfirmUiState(
    val isRegistrationConfirmed: Boolean = false
)

data class SobositeResetPasswordUiState(
    val isPasswordResetSent: Boolean = false
)
