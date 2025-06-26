package com.krzysobo.sobositeapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.krzysobo.soboapptpl.viewmodel.SoboViewModel
import com.krzysobo.soboapptpl.widgets.strNotEmpty
import com.krzysobo.soboapptpl.widgets.validateWithLambda
import com.krzysobo.sobositeapp.service.ApiServerUnavailableException
import com.krzysobo.sobositeapp.service.HttpException
import com.krzysobo.sobositeapp.service.HttpService
import com.krzysobo.sobositeapp.service.NetworkConnectionUnavailableException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class ResetPasswordConfirmPageVM : SoboViewModel(
) {

    private val _uiState = MutableStateFlow(SobositeResetPasswordUiState())
    val uiState: StateFlow<SobositeResetPasswordUiState> = _uiState.asStateFlow()

    var login: MutableState<String> = mutableStateOf("")
    var token: MutableState<String> = mutableStateOf("")
    var pass: MutableState<String> = mutableStateOf("")
    var passConfirm: MutableState<String> = mutableStateOf("")

    var isErrorLogin: MutableState<Boolean> = mutableStateOf(false)
    var isErrorToken: MutableState<Boolean> = mutableStateOf(false)
    var isErrorPass: MutableState<Boolean> = mutableStateOf(false)
    var isErrorPassConfirm: MutableState<Boolean> = mutableStateOf(false)
    var isErrorPassDontMatch: MutableState<Boolean> = mutableStateOf(false)

    var passVisible: MutableState<Boolean> = mutableStateOf(false)
    var isPassVisible = false
        get() = passVisible.value

    fun togglePassVisible() {
        passVisible.value = !passVisible.value
    }

    suspend fun doConfirmPasswordReset(): Boolean {
        try {
            HttpService().resetPasswordConfirm(login.value, token.value, pass.value)
            _uiState.update { currentState ->
                currentState.copy(
                    isPasswordResetSent = true,
                )
            }

            isFormSent.value = true
            isApiError.value = false
            return true
        } catch (e: NetworkConnectionUnavailableException) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: ApiServerUnavailableException) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: HttpException) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: Exception) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        }

        return false
    }

    fun checkPassMatch(): Boolean {
        val res: Boolean = (pass.value == passConfirm.value)
        isErrorPassDontMatch.value = !res
        return res
    }

    fun validate(): Boolean {
        clearErrors()
        val resLogin = validateWithLambda(isErrorLogin, { strNotEmpty(login.value) })
        val resToken = validateWithLambda(isErrorToken, { strNotEmpty(token.value) })
        val resPass = validateWithLambda(isErrorPass, { strNotEmpty(pass.value) })
        val resPassConfirm =
            validateWithLambda(isErrorPassConfirm, { strNotEmpty(passConfirm.value) })

        return resLogin && resToken && resPass && resPassConfirm && checkPassMatch()
    }

    fun clearLoginError() {
        clearErrorFlag(isErrorLogin)
    }

    fun clearTokenError() {
        clearErrorFlag(isErrorToken)
    }

    fun clearPassError() {
        clearErrorFlag(isErrorPass)
    }

    fun clearPassConfirmError() {
        clearErrorFlag(isErrorPassConfirm)
    }

    fun clearErrors() {
        clearLoginError()
        clearTokenError()
        clearPassError()
        clearPassConfirmError()

        clearApiError()
        clearFormSent()
    }
}
