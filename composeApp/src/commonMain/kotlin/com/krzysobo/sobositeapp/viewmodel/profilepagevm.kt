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
import com.krzysobo.sobositeapp.service.findNetworkExceptions
import io.ktor.client.call.NoTransformationFoundException


class ProfilePageVM : SoboViewModel(
) {
    var login: MutableState<String> = mutableStateOf("")
    var firstName: MutableState<String> = mutableStateOf("")
    var lastName: MutableState<String> = mutableStateOf("")
    var oldPassword: MutableState<String> = mutableStateOf("")
    var newPassword: MutableState<String> = mutableStateOf("")
    var newPasswordConfirm: MutableState<String> = mutableStateOf("")

    var isPassChangeOpen: MutableState<Boolean> = mutableStateOf(false);
    var passVisible: MutableState<Boolean> = mutableStateOf(false)

    var isErrorLogin: MutableState<Boolean> = mutableStateOf(false)
    var isErrorFirstName: MutableState<Boolean> = mutableStateOf(false)
    var isErrorLastName: MutableState<Boolean> = mutableStateOf(false)
    var isErrorOldPassword: MutableState<Boolean> = mutableStateOf(false)
    var isErrorNewPassword: MutableState<Boolean> = mutableStateOf(false)
    var isErrorNewPasswordConfirm: MutableState<Boolean> = mutableStateOf(false)
    var isErrorNewPasswordsDontMatch: MutableState<Boolean> = mutableStateOf(false)

    var isProfileUpToDate: MutableState<Boolean> = mutableStateOf(false)

    fun doRefreshProfileFromUserState() {
        val userData = getUserData()
        login.value = userData.email
        firstName.value = userData.firstName
        lastName.value = userData.lastName
    }


    suspend fun doUpdateProfile(): Boolean {
        try {
            val resp = HttpService().userProfileOwnUpdate(
                getUserToken(),
                login.value,
                firstName.value,
                lastName.value,
                if (isPassChangeOpen.value) oldPassword.value else "",
                if (isPassChangeOpen.value) newPassword.value else "",
            )

            toggleRefreshCompose()
            isFormSent.value = true
            isApiError.value = false
            apiErrorDetails.value = ""

            return true
        } catch (e: EmailAlreadyExistsException) {
            toggleRefreshCompose()
            apiErrorDetails.value = "Email already exists"
        } catch (e: InvalidEmailException) {
            apiErrorDetails.value = "Email is invalid"
        } catch (e: HttpException) {
            apiErrorDetails.value = "A network error has occurred. " +
                    "Please check your internet connection."
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

        isFormSent.value = false
        isApiError.value = true
        toggleRefreshCompose()
        return false
    }


    fun checkPassMatch(): Boolean {
        val res: Boolean = (newPassword.value == newPasswordConfirm.value)
        isErrorNewPasswordsDontMatch.value = !res
        return res
    }

    fun validate(): Boolean {
        clearErrors()

        val resLogin = validateWithLambda(isErrorLogin, { strNotEmpty(login.value) })
        val resFirstName = validateWithLambda(isErrorFirstName, { strNotEmpty(firstName.value) })
        val resLastName = validateWithLambda(isErrorLastName, { strNotEmpty(lastName.value) })

        var res = resLogin && resFirstName && resLastName

        if (isPassChangeOpen.value) {
            val resNewPassword = validateWithLambda(isErrorNewPassword, {
                strNotEmpty(newPassword.value)
            })
            val resNewPasswordConfirm = validateWithLambda(isErrorNewPasswordConfirm, {
                strNotEmpty(newPasswordConfirm.value)
            })
            val resOldPassword = validateWithLambda(isErrorOldPassword, {
                strNotEmpty(oldPassword.value)
            })
            res = res && resNewPassword && resNewPasswordConfirm && resOldPassword
        }

        return res
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

    fun clearNewPasswordError() {
        clearErrorFlag(isErrorNewPassword)
    }

    fun clearNewPasswordConfirmError() {
        clearErrorFlag(isErrorNewPasswordConfirm)
    }

    fun clearOldPasswordError() {
        clearErrorFlag(isErrorOldPassword)
    }

    fun clearErrors() {
        clearLoginError()
        clearFirstNameError()
        clearLastNameError()
        clearNewPasswordError()
        clearNewPasswordConfirmError()
        clearOldPasswordError()

        clearApiError()
        clearFormSent()
    }

    fun togglePassVisible() {
        passVisible.value = !passVisible.value
    }

    var isPassVisible = false
        get() = passVisible.value
}
