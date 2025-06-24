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


class ResetPasswordPageVM : SoboViewModel(
) {

    private val _uiState = MutableStateFlow(SobositeResetPasswordUiState())
    val uiState: StateFlow<SobositeResetPasswordUiState> = _uiState.asStateFlow()

    var login: MutableState<String> = mutableStateOf("")

    var isErrorLogin: MutableState<Boolean> = mutableStateOf(false)

    suspend fun doResetPassword(): Boolean {
        try {
            HttpService().resetPasswordSendRequest(login.value)
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
        }

        return false
    }

    fun validate(): Boolean {
        clearErrors()
        val resLogin = validateWithLambda(isErrorLogin, { strNotEmpty(login.value) })

        return resLogin
    }

    fun clearLoginError() {
        clearErrorFlag(isErrorLogin)
    }

    fun clearErrors() {
        clearLoginError()

        clearApiError()
        clearFormSent()
    }
}
