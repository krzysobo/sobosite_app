package com.krzysobo.sobositeapp.viewmodel

import com.krzysobo.soboapptpl.service.SoboRouter
import com.krzysobo.sobositeapp.http.LoginResponse
import com.krzysobo.sobositeapp.viewmodel.admin.AdminListUsersPageVM
import com.krzysobo.sobositeapp.viewmodel.admin.AdminEditUserPageVM


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


private object AllViewModels {
    val userStateViewModel = UserStateVM()

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
}

fun isLoggedIn(): Boolean {
    val state = AllViewModels.userStateViewModel.isLoggedIn
    return state
}

fun isLoggedInAdmin(): Boolean {
    val state = (AllViewModels.userStateViewModel.isLoggedIn && AllViewModels.userStateViewModel.isAdmin)
    return state
}

fun getUserToken(): String {
    val token = AllViewModels.userStateViewModel.token
    return token
}

fun getUserData(): UserData {
    return AllViewModels.userStateViewModel.userData
}

fun actionLoginUser(data: LoginResponse) {
    AllViewModels.userStateViewModel.loginUser(data)
}

suspend fun actionLogoutUser() {
    AllViewModels.userStateViewModel.doLogOutUser()
    SoboRouter.navigateToLogin()
}

suspend fun doRefreshAdminUserList() {
    AllViewModels.adminListUsersPageVM.doGetUsers()
}

//fun isCurrentRouteSet(): Boolean {
//    return SoboRouter.isRouteSet()
//}