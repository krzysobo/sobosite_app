package com.krzysobo.sobositeapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.krzysobo.soboapptpl.viewmodel.SoboViewModel
import com.krzysobo.soboapptpl.widgets.strNotEmpty
import com.krzysobo.soboapptpl.widgets.validateWithLambda
import com.krzysobo.sobositeapp.http.DataResponse
import com.krzysobo.sobositeapp.http.LoginResponse
import com.krzysobo.sobositeapp.service.ApiServerUnavailableException
import com.krzysobo.sobositeapp.service.BadCredentialsException
import com.krzysobo.sobositeapp.service.HttpService
import com.krzysobo.sobositeapp.service.NetworkConnectionUnavailableException
import com.krzysobo.sobositeapp.service.findNetworkExceptions
import io.ktor.client.call.NoTransformationFoundException


class LoginPageVM : SoboViewModel(
) {
    var login: MutableState<String> = mutableStateOf("")
    var pass: MutableState<String> = mutableStateOf("")
    var passVisible: MutableState<Boolean> = mutableStateOf(false)
    var isErrorLogin: MutableState<Boolean> = mutableStateOf(false)
    var isErrorPass: MutableState<Boolean> = mutableStateOf(false)
    var isAuthError: MutableState<Boolean> = mutableStateOf(false)

    suspend fun doLogIn() {
        println("LoginPageVM: logging in...")
        // TODO - REAL AUTH, business logic using KTOR
        try {
            val resp: DataResponse<LoginResponse> = HttpService().login(login.value, pass.value)

            if (resp.data != null) {
                actionLoginUser(resp.data)
                isFormSent.value = true
                isApiError.value = false
            } else {
                isFormSent.value = false
                isApiError.value = true
            }
        } catch (e: BadCredentialsException) {
            isAuthError.value = true
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: NetworkConnectionUnavailableException) {
            isAuthError.value = false
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: ApiServerUnavailableException) {
            isAuthError.value = false
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: NoTransformationFoundException) {
            isAuthError.value = false
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "Server error. Please contact the administrator"
        } catch (e: Exception) {
            apiErrorDetails.value = findNetworkExceptions(e)
            isAuthError.value = false
            isFormSent.value = false
            isApiError.value = true
        }
    }

    fun validate(): Boolean {
        clearErrors()
        val resLogin = validateWithLambda(isErrorLogin, { strNotEmpty(login.value) })

        val resPass = validateWithLambda(isErrorPass, { strNotEmpty(pass.value) })

        return resLogin && resPass
    }

    fun clearLoginError() {
        clearErrorFlag(isErrorLogin)
    }

    fun clearPassError() {
        clearErrorFlag(isErrorPass)
    }

    fun clearErrors() {
        clearLoginError()
        clearPassError()
        clearErrorFlag(isAuthError)

        clearApiError()
        clearFormSent()
    }

    fun togglePassVisible() {
        passVisible.value = !passVisible.value
    }

    var isPassVisible = false
        get() = passVisible.value

}

