package com.krzysobo.sobositeapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.krzysobo.soboapptpl.viewmodel.SoboViewModel
import com.krzysobo.soboapptpl.viewmodel.toggleRefreshCompose
import com.krzysobo.sobositeapp.http.LoginResponse
import com.krzysobo.sobositeapp.service.ApiServerUnavailableException
import com.krzysobo.sobositeapp.service.HttpException
import com.krzysobo.sobositeapp.service.HttpService
import com.krzysobo.sobositeapp.service.NetworkConnectionUnavailableException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.TimeSource.Monotonic.markNow


class UserStateVM : SoboViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    private val _uiStateReg = MutableStateFlow(SobositeRegisterUiState())

    var isLoggedInRmb: MutableState<Boolean> = mutableStateOf(false)
    var isAdminRmb: MutableState<Boolean> = mutableStateOf(false)

    fun loginUser(loginResponse: LoginResponse) {
        val loginUiState = LoginUiState(
            isLoggedIn = ((loginResponse.id != "") && (loginResponse.token != "")),
            isAdmin = loginResponse.is_staff,
            token = loginResponse.token,
            loginTs = markNow(),
            userData = UserData(
                id = loginResponse.id,
                status = loginResponse.status,
                email = loginResponse.email,
                token = loginResponse.token,
                firstName = loginResponse.first_name,
                lastName = loginResponse.last_name,
                isStaff = loginResponse.is_staff
            )
        )

        isLoggedInRmb.value = loginUiState.isLoggedIn
        isAdminRmb.value = loginUiState.isAdmin

        setLoggedInUser(loginUiState)
    }

    private fun setLoggedInUser(loginUiState: LoginUiState) {
        toggleRefreshCompose()

        _uiState.update { currentState ->
            currentState.copy(
                isLoggedIn = loginUiState.isLoggedIn,
                isAdmin = loginUiState.isAdmin,
                token = loginUiState.token,
                loginTs = loginUiState.loginTs,
                userData = loginUiState.userData
            )
        }
    }

    suspend fun doRefreshProfileFromApi(): Boolean {
        try {
            if (token != "") {
                val resp = HttpService().userProfileOwnGet(getUserToken())
                if (resp.data != null) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoggedIn = currentState.isLoggedIn, // this doesn't change here
                            isAdmin = resp.data.is_staff,
                            token = currentState.token, // this doesn't change here
                            loginTs = currentState.loginTs, // this doesn't change here
                            userData = UserData(
                                id = resp.data.id,
                                status = resp.data.status,
                                email = resp.data.email,
                                token = currentState.token, // this doesn't change here
                                firstName = resp.data.first_name,
                                lastName = resp.data.last_name,
                                isStaff = resp.data.is_staff,
                            )
                        )
                    }
                    toggleRefreshCompose()
                    return true
                }
            }
        } catch (e: NetworkConnectionUnavailableException) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: ApiServerUnavailableException) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: HttpException) {
            toggleRefreshCompose()
            apiErrorDetails.value = "${e.message}"
        }

        return false
    }

    suspend fun doLogOutUser() {
        val userdataToken = _uiState.value.userData.token
        var isLoggedIn = _uiState.value.isLoggedIn

//        println("==== doLogOutUser - token: $token userdata token: $userdataToken is logged in? $isLoggedIn")

        if ((token != "") && (_uiState.value.isLoggedIn)) {
            val resp = HttpService().logout(token)

            toggleRefreshCompose()
            _uiState.update { currentState ->
                currentState.copy(
                    isLoggedIn = false,
                    isAdmin = false,
                    token = "",
                    loginTs = markNow(),
                    userData = UserData(
                        id = "",
                        status = 10,
                        email = "",
                        token = "",
                        firstName = "",
                        lastName = "",
                        isStaff = false
                    )
                )
            }
            isLoggedInRmb.value = false
            isAdminRmb.value = false
        }
    }

    val isLoggedIn
        get() = _uiState.value.isLoggedIn

    var isLoggedInShow: MutableState<Boolean> = mutableStateOf(false)

    val isAdmin
        get() = _uiState.value.userData.isStaff

    val loginTs
        get() = _uiState.value.loginTs

    val userData
        get() = _uiState.value.userData

    val token
        get() = _uiState.value.token

    val regEmail
        get() = _uiStateReg.value.email

    val regError
        get() = _uiStateReg.value.error

}
