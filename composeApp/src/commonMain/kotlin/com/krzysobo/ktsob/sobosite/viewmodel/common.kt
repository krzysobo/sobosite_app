package com.krzysobo.ktsob.sobosite.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.krzysobo.ktsob.sobosite.http.LoginResponse
import com.krzysobo.ktsob.apptpl.service.SoboRouter
import com.krzysobo.ktsob.sobosite.viewmodel.admin.AdminListUsersPageVM
import com.krzysobo.ktsob.sobosite.viewmodel.admin.AdminEditUserPageVM


class VmNotFoundException(message: String?) : Exception(message)


fun getResetPasswordPageVM(): ResetPasswordPageVM {
    return AllViewModels.resetPasswordPageVM
}

fun getResetPasswordConfirmPageVM(): ResetPasswordConfirmPageVM {
    return AllViewModels.resetPasswordConfirmPageVM
}
fun getRegisterConfirmPageVM(): RegisterConfirmPageVM {
    return AllViewModels.registerConfirmPageVM
}

fun getRegisterPageVM(): RegisterPageVM {
    return AllViewModels.registerPageVM
}

fun getLoginPageVM(): LoginPageVM {
    return AllViewModels.loginPageVM
}

fun getProfilePageVM(): ProfilePageVM {
    return AllViewModels.profilePageVM
}

fun getSettingsPageVM(): SettingsPageVM {
    return AllViewModels.settingsPageVM
}

fun getAdminListUsersPageVM(): AdminListUsersPageVM {
    return AllViewModels.adminListUsersPageVM
}

fun getAdminEditUserPageVM(): AdminEditUserPageVM {
    return AllViewModels.adminEditUserPageVM
}

fun getUserStateVM(): UserStateVM {
    return AllViewModels.userStateViewModel
}

open class SoboViewModel: ViewModel() {
    var isApiError: MutableState<Boolean> = mutableStateOf(false)
    var isFormSent: MutableState<Boolean> = mutableStateOf(false)
    var apiErrorDetails: MutableState<String> = mutableStateOf("")

    protected fun clearErrorFlag(errorFlag: MutableState<Boolean>) {
        errorFlag.value = false
    }

    protected fun clearApiError() {
        clearErrorFlag(isApiError)
    }

    protected fun clearFormSent() {
        isFormSent.value = false
    }
}

class AppViewModelVM : SoboViewModel(){
    var isDarkMode: MutableState<Boolean> = mutableStateOf(false)
}


private object AllViewModels {


    val userStateViewModel = UserStateVM()
    val appViewModel = AppViewModelVM()


    // ------ sobosite pages ------
    val resetPasswordConfirmPageVM = ResetPasswordConfirmPageVM()
    val resetPasswordPageVM = ResetPasswordPageVM()
    val registerConfirmPageVM = RegisterConfirmPageVM()
    val registerPageVM = RegisterPageVM()
    val loginPageVM = LoginPageVM()
    val profilePageVM = ProfilePageVM()
    val settingsPageVM = SettingsPageVM()

    val adminListUsersPageVM = AdminListUsersPageVM()
    val adminEditUserPageVM = AdminEditUserPageVM()
    // ------ /sobosite ------

    var refreshCompose: MutableState<Boolean> = mutableStateOf(false)

//    fun toggleRefreshCompose() {
//        refreshCompose.value = !refreshCompose.value
//    }

}


fun toggleDarkMode() {
    AllViewModels.appViewModel.isDarkMode.value = !AllViewModels.appViewModel.isDarkMode.value
//    println(">> COMMON: toggleDarkMode: ${AllViewModels.appViewModel.isDarkMode.value}")
}

fun isDarkMode(): Boolean {
    val state = AllViewModels.appViewModel.isDarkMode.value
//    println(">> COMMON: isDarkMode: $state")
    return state
}

fun isLoggedIn(): Boolean {
    val state = AllViewModels.userStateViewModel.isLoggedIn
    return state
}

fun isLoggedInAdmin(): Boolean {
    val state = (AllViewModels.userStateViewModel.isLoggedIn && AllViewModels.userStateViewModel.isAdmin)
    return state
}

fun toggleRefreshCompose() {
    AllViewModels.refreshCompose.value = !AllViewModels.refreshCompose.value
//    refreshCompose.value = !refreshCompose.value
}

fun getUserToken(): String {
    val token = AllViewModels.userStateViewModel.token
    return token
}

fun getUserData(): UserData {
    return AllViewModels.userStateViewModel.userData
}

fun isRefreshCompose(): Boolean {
    return AllViewModels.refreshCompose.value
}

fun actionLoginUser(data: LoginResponse) {
    AllViewModels.userStateViewModel.loginUser(data)
}

suspend fun actionLogoutUser() {
    AllViewModels.userStateViewModel.doLogOutUser()
}

fun isCurrentRouteSet(): Boolean {
    return SoboRouter.isRouteSet()
}