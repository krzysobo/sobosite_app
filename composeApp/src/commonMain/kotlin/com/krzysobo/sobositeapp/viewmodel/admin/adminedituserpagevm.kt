package com.krzysobo.sobositeapp.viewmodel.admin

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.krzysobo.soboapptpl.viewmodel.SoboViewModel
import com.krzysobo.soboapptpl.viewmodel.toggleRefreshCompose
import com.krzysobo.soboapptpl.widgets.strNotEmpty
import com.krzysobo.soboapptpl.widgets.validateWithLambda
import com.krzysobo.sobositeapp.http.AdminUserModel
import com.krzysobo.sobositeapp.http.DataResponse
import com.krzysobo.sobositeapp.service.ApiServerUnavailableException
import com.krzysobo.sobositeapp.service.EmailAlreadyExistsException
import com.krzysobo.sobositeapp.service.HttpException
import com.krzysobo.sobositeapp.service.HttpService
import com.krzysobo.sobositeapp.service.InvalidEmailException
import com.krzysobo.sobositeapp.service.NetworkConnectionUnavailableException
import com.krzysobo.sobositeapp.service.findNetworkExceptions
import com.krzysobo.sobositeapp.viewmodel.doRefreshAdminUserList
import com.krzysobo.sobositeapp.viewmodel.getUserToken
import io.ktor.client.call.NoTransformationFoundException


class AdminEditUserPageVM : SoboViewModel(
) {
    // field states
    var userId: MutableState<String> = mutableStateOf("")

    var login: MutableState<String> = mutableStateOf("")
    var firstName: MutableState<String> = mutableStateOf("")
    var lastName: MutableState<String> = mutableStateOf("")

    var pass: MutableState<String> = mutableStateOf("")
    var passConfirm: MutableState<String> = mutableStateOf("")
    var passVisible: MutableState<Boolean> = mutableStateOf(false)

    var isStaff: MutableState<Boolean> = mutableStateOf(false)
    var isActive: MutableState<Boolean> = mutableStateOf(false)
    var role: MutableState<String> = mutableStateOf("USR")

    var noFailedLogins: MutableState<Int> = mutableStateOf(0)
    var failedIsBlocked: MutableState<Boolean> = mutableStateOf(false)
    var failedIsBlockedThru: MutableState<String> = mutableStateOf("")

    // error states
    var isErrorLogin: MutableState<Boolean> = mutableStateOf(false)
    var isErrorFirstName: MutableState<Boolean> = mutableStateOf(false)
    var isErrorLastName: MutableState<Boolean> = mutableStateOf(false)
    var isErrorPass: MutableState<Boolean> = mutableStateOf(false)
    var isErrorPassConfirm: MutableState<Boolean> = mutableStateOf(false)

    var isErrorPassDontMatch: MutableState<Boolean> = mutableStateOf(false)

    var isPassVisible = false
        get() = passVisible.value
    var isPassChangeOpen: MutableState<Boolean> = mutableStateOf(false);
    var isEditedUserUpToDate: MutableState<Boolean> = mutableStateOf(false)

    fun isNewUser(): Boolean {
        return (userId.value == "")
    }

    fun togglePassVisible() {
        passVisible.value = !passVisible.value
    }

    private fun restoreUserFromResp(resp: DataResponse<AdminUserModel>) {
        if (resp.data != null) {
            userId.value = resp.data.id
            login.value = resp.data.email
            firstName.value = resp.data.first_name
            lastName.value = resp.data.last_name
            isStaff.value = resp.data.is_staff
            isActive.value = resp.data.is_active
            role.value = resp.data.role
            noFailedLogins.value = resp.data.no_failed_logins
            failedIsBlocked.value = resp.data.failed_is_blocked
            failedIsBlockedThru.value = resp.data.failed_is_blocked_thru
        }
    }

    suspend fun doRefreshUserFromApi(): Boolean {
        println("=======> doRefreshUserFromApi ----> NO USER ID - QUITTING. ")
        if (userId.value == "") {
            return false
        }
        println("=======> doRefreshUserFromApi ----> USER ID: ${userId.value} - REFRESHING... ")

        try {
            val resp: DataResponse<AdminUserModel> = HttpService().adminUserGet(
                getUserToken(), userId.value
            )
            restoreUserFromResp(resp)
            return true
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
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = findNetworkExceptions(e)
        }

        return false
    }

    suspend fun doAdminSaveUser(): Boolean {
        try {
            if (isNewUser()) {
                val resp = HttpService().adminUserCreate(
                    getUserToken(),
                    login.value,
                    firstName.value,
                    lastName.value,
                    isStaff.value,
                    isActive.value,
                    pass.value
                )
                restoreUserFromResp(resp)
            } else {
                val resp = HttpService().adminUserUpdate(
                    getUserToken(),
                    userId.value,
                    login.value,
                    firstName.value,
                    lastName.value,
                    isStaff.value,
                    isActive.value,
                    noFailedLogins.value,
                    failedIsBlocked.value,
                    failedIsBlockedThru.value,
                    if (isPassChangeOpen.value) pass.value else "",
                )
                restoreUserFromResp(resp)
            }

            isFormSent.value = true
            isApiError.value = false
            apiErrorDetails.value = ""

            doRefreshAdminUserList()
            toggleRefreshCompose()

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
        val res: Boolean = (pass.value == passConfirm.value)
        isErrorPassDontMatch.value = !res
        return res
    }

    fun validate(): Boolean {
        clearErrors()
        val resLogin = validateWithLambda(isErrorLogin, { strNotEmpty(login.value) })
        val resFirstName = validateWithLambda(isErrorFirstName, { strNotEmpty(firstName.value) })
        val resLastName = validateWithLambda(isErrorLastName, { strNotEmpty(lastName.value) })

        var res = resLogin && resFirstName && resLastName

        if (isPassChangeOpen.value) {
            val resPass = validateWithLambda(isErrorPass, { strNotEmpty(pass.value) })
            val resPassConfirm =
                validateWithLambda(isErrorPassConfirm, { strNotEmpty(passConfirm.value) })
            res = res && resPass && resPassConfirm
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
    }

}
