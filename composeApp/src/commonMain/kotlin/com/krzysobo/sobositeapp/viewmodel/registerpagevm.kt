package com.krzysobo.sobositeapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.krzysobo.soboapptpl.viewmodel.SoboViewModel
import com.krzysobo.soboapptpl.viewmodel.toggleRefreshCompose
import com.krzysobo.soboapptpl.widgets.strNotEmpty
import com.krzysobo.soboapptpl.widgets.validateWithLambda
import com.krzysobo.sobositeapp.service.ApiServerUnavailableException
import com.krzysobo.sobositeapp.service.EmailAlreadyExistsException
import com.krzysobo.sobositeapp.service.HttpException
import com.krzysobo.sobositeapp.service.HttpService
import com.krzysobo.sobositeapp.service.InvalidEmailException
import com.krzysobo.sobositeapp.service.NetworkConnectionUnavailableException


class RegisterPageVM : SoboViewModel(
) {
    var login: MutableState<String> = mutableStateOf("")
    var firstName: MutableState<String> = mutableStateOf("")
    var lastName: MutableState<String> = mutableStateOf("")
    var pass: MutableState<String> = mutableStateOf("")
    var passConfirm: MutableState<String> = mutableStateOf("")
    var passVisible: MutableState<Boolean> = mutableStateOf(false)

    var isErrorLogin: MutableState<Boolean> = mutableStateOf(false)
    var isErrorFirstName: MutableState<Boolean> = mutableStateOf(false)
    var isErrorLastName: MutableState<Boolean> = mutableStateOf(false)
    var isErrorPass: MutableState<Boolean> = mutableStateOf(false)
    var isErrorPassConfirm: MutableState<Boolean> = mutableStateOf(false)
    var isErrorPassDontMatch: MutableState<Boolean> = mutableStateOf(false)

    suspend fun doRegister(): Boolean {
        try {
            val resp = HttpService().registerCreate(
                login.value,
                firstName.value,
                lastName.value,
                pass.value
            )

            if (resp.data != null) {
                isFormSent.value = true
                isApiError.value = false
            } else {
                isFormSent.value = false
                isApiError.value = true
            }

            return true
        } catch (e: NetworkConnectionUnavailableException) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: ApiServerUnavailableException) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: EmailAlreadyExistsException) {
            toggleRefreshCompose()
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "Email already exists"
        } catch (e: InvalidEmailException) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "Email is invalid"
            toggleRefreshCompose()
        } catch (e: HttpException) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "A network error has occurred. " +
                    "Please check your internet connection."
        } catch (e: Exception) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "An error has occurred. " +
                    "Please check your internet connection. Message: ${e.message}"
        }

        toggleRefreshCompose()
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
        val resPass = validateWithLambda(isErrorPass, { strNotEmpty(pass.value) })
        val resPassConfirm =
            validateWithLambda(isErrorPassConfirm, { strNotEmpty(passConfirm.value) })
        val resFirstName = validateWithLambda(isErrorFirstName, { strNotEmpty(firstName.value) })
        val resLastName = validateWithLambda(isErrorLastName, { strNotEmpty(lastName.value) })

        return resLogin && resPass && resPassConfirm && checkPassMatch() && resFirstName && resLastName
    }

    fun clearLoginError() {
        clearErrorFlag(isErrorLogin)
    }

    fun clearFirstNameError() {
        clearErrorFlag(isErrorFirstName)
    }

    fun clearLastNameError() {
        clearErrorFlag(isErrorLastName)
    }

    fun clearPassError() {
        clearErrorFlag(isErrorPass)
    }

    fun clearPassConfirmError() {
        clearErrorFlag(isErrorPassConfirm)
    }

    fun clearErrors() {
        clearLoginError()
        clearFirstNameError()
        clearLastNameError()
        clearPassError()
        clearPassConfirmError()

        clearApiError()
        clearFormSent()
    }

    fun togglePassVisible() {
        passVisible.value = !passVisible.value
    }

    var isPassVisible = false
        get() = passVisible.value
}
