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
import com.krzysobo.sobositeapp.service.RegisterConfirmationException
import com.krzysobo.sobositeapp.service.findNetworkExceptions
import io.ktor.client.call.NoTransformationFoundException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterConfirmPageVM : SoboViewModel(
) {

    private val _uiState = MutableStateFlow(SobositeRegisterConfirmUiState())
    val uiState: StateFlow<SobositeRegisterConfirmUiState> = _uiState.asStateFlow()

    var login: MutableState<String> = mutableStateOf("")
    var token: MutableState<String> = mutableStateOf("")

    var isErrorLogin: MutableState<Boolean> = mutableStateOf(false)
    var isErrorToken: MutableState<Boolean> = mutableStateOf(false)
    var isConfirmationError: MutableState<Boolean> = mutableStateOf(false)

    suspend fun doRegisterConfirm() {
        try {
            HttpService().registerConfirm(login.value, token.value)
            _uiState.update { currentState ->
                currentState.copy(
                    isRegistrationConfirmed = true,
                )
            }
            isFormSent.value = true
            isApiError.value = false
        } catch (e: RegisterConfirmationException) {
            isConfirmationError.value = true
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: HttpException) {
            isConfirmationError.value = true
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: NetworkConnectionUnavailableException) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: ApiServerUnavailableException) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: NoTransformationFoundException) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "Server error. Please contact the administrator"
        } catch (e: Exception) {
            apiErrorDetails.value = findNetworkExceptions(e)
            isFormSent.value = false
            isApiError.value = true
        }


    }

    fun validate(): Boolean {
        clearErrors()
        val resLogin = validateWithLambda(isErrorLogin, { strNotEmpty(login.value) })
        val resToken = validateWithLambda(isErrorToken, { strNotEmpty(token.value) })

        return resLogin && resToken
    }

    fun clearLoginError() {
        clearErrorFlag(isErrorLogin)
    }

    fun clearTokenError() {
        clearErrorFlag(isErrorToken)
    }


    fun clearErrors() {
        clearLoginError()
        clearTokenError()

        clearApiError()
        clearFormSent()
    }

}
