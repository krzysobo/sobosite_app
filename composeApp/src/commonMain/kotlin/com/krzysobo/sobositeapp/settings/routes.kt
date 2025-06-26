package com.krzysobo.sobositeapp.settings
import com.krzysobo.soboapptpl.pubres.PubRes
import sobositeapp.composeapp.generated.resources.Res
import sobositeapp.composeapp.generated.resources.dashboard
import sobositeapp.composeapp.generated.resources.profile
import sobositeapp.composeapp.generated.resources.register
import sobositeapp.composeapp.generated.resources.register_confirm
import sobositeapp.composeapp.generated.resources.reset_pass
import sobositeapp.composeapp.generated.resources.reset_pass_confirm
import sobositeapp.composeapp.generated.resources.user_edition
import sobositeapp.composeapp.generated.resources.users
import sobositeapp.composeapp.generated.resources.welcome
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.soboapptpl.service.AppRequest
import com.krzysobo.soboapptpl.service.SoboRoute
import com.krzysobo.soboapptpl.service.USER_ROLE
import com.krzysobo.sobositeapp.view.PageAboutSobosite
import com.krzysobo.sobositeapp.view.PageHelpSobosite
import com.krzysobo.sobositeapp.view.PageSobositeDashboard
import com.krzysobo.sobositeapp.view.PageSobositeLogin
import com.krzysobo.sobositeapp.view.PageSobositeProfile
import com.krzysobo.sobositeapp.view.PageSobositeRegister
import com.krzysobo.sobositeapp.view.PageSobositeRegisterConfirm
import com.krzysobo.sobositeapp.view.PageSobositeResetPassword
import com.krzysobo.sobositeapp.view.PageSobositeResetPasswordConfirm
import com.krzysobo.sobositeapp.view.PageSobositeSettings
import com.krzysobo.sobositeapp.view.PageSobositeAdminEditUser
import com.krzysobo.sobositeapp.view.PageSobositeAdminListUsers
import com.krzysobo.sobositeapp.view.PageSobositeWelcome


class AppRequestEditUser(val userId: String = "") : AppRequest()


enum class SOBOSITE_ROUTE_HANDLE(val value: String) {
    WELCOME("welcome"),
    LOGIN("login"),
    REGISTER("register"),
    REGISTER_CONFIRM("register_confirm"),
    RESET_PASS("reset_pass"),
    CONFIRM_PASS_RESET("confirm_pass_reset"),
    DASHBOARD("dashboard"),
    PROFILE("profile"),
    ADMIN_DASHBOARD("dashboard"),
    ADMIN_USERS("users"),
    ADMIN_EDIT_USER("edit_user"),
    ABOUT("about"),
    HELP("help"),
    SETTINGS("settings"),
}

val sobositeRouteHandlesForDesktopMenu = listOf<String>(
    SOBOSITE_ROUTE_HANDLE.LOGIN.value,
    SOBOSITE_ROUTE_HANDLE.REGISTER_CONFIRM.value,
    SOBOSITE_ROUTE_HANDLE.CONFIRM_PASS_RESET.value,
    SOBOSITE_ROUTE_HANDLE.PROFILE.value,
    SOBOSITE_ROUTE_HANDLE.ADMIN_USERS.value,
    SOBOSITE_ROUTE_HANDLE.DASHBOARD.value,
//    SOBOSITE_ROUTE_HANDLE.ADMIN_DASHBOARD.value,
    SOBOSITE_ROUTE_HANDLE.ABOUT.value,
    SOBOSITE_ROUTE_HANDLE.HELP.value,
    SOBOSITE_ROUTE_HANDLE.SETTINGS.value,
)

val sobositeRoutes = listOf(
    SoboRoute(
        SOBOSITE_ROUTE_HANDLE.WELCOME.value,
        AnyRes(Res.string.welcome),
        { PageSobositeWelcome() }
    ),
    SoboRoute(
        SOBOSITE_ROUTE_HANDLE.PROFILE.value,
        AnyRes(Res.string.profile),
        { PageSobositeProfile() },
        perms = listOf(USER_ROLE.USER, USER_ROLE.ADMIN)
    ),
    SoboRoute(
        SOBOSITE_ROUTE_HANDLE.DASHBOARD.value,
        AnyRes(Res.string.dashboard),
        { PageSobositeDashboard() },
        perms = listOf(USER_ROLE.USER, USER_ROLE.ADMIN)
    ),
    SoboRoute(
        SOBOSITE_ROUTE_HANDLE.ADMIN_USERS.value,
        AnyRes(Res.string.users),
        { PageSobositeAdminListUsers() },
        perms = listOf(USER_ROLE.ADMIN)
    ),

    SoboRoute(
        SOBOSITE_ROUTE_HANDLE.ADMIN_EDIT_USER.value,
        AnyRes(Res.string.user_edition),
        {PageSobositeAdminEditUser() },
        perms = listOf(USER_ROLE.ADMIN),
        funcWithReq = {
            req -> if (req is AppRequestEditUser) {
                PageSobositeAdminEditUser(req.userId)
            }
        }
    ),
    SoboRoute(
        SOBOSITE_ROUTE_HANDLE.LOGIN.value,
        AnyRes(PubRes.string.login),
        { PageSobositeLogin() },
        perms = listOf(USER_ROLE.ANON_ONLY)
    ),
    SoboRoute(
        SOBOSITE_ROUTE_HANDLE.REGISTER.value,
        AnyRes(Res.string.register),
        { PageSobositeRegister() },
        perms = listOf(USER_ROLE.ANON_ONLY)
    ),
    SoboRoute(
        SOBOSITE_ROUTE_HANDLE.REGISTER_CONFIRM.value,
        AnyRes(Res.string.register_confirm),
        { PageSobositeRegisterConfirm() },
        perms = listOf(USER_ROLE.ANON_ONLY)
    ),
    SoboRoute(
        SOBOSITE_ROUTE_HANDLE.RESET_PASS.value,
        AnyRes(Res.string.reset_pass),
        { PageSobositeResetPassword() },
        perms = listOf(USER_ROLE.ANON_ONLY)
    ),
    SoboRoute(
        SOBOSITE_ROUTE_HANDLE.CONFIRM_PASS_RESET.value,
        AnyRes(Res.string.reset_pass_confirm),
        { PageSobositeResetPasswordConfirm() },
        perms = listOf(USER_ROLE.ANON_ONLY)
    ),
    SoboRoute(
        SOBOSITE_ROUTE_HANDLE.ABOUT.value,
        AnyRes(PubRes.string.about),
        { PageAboutSobosite() }),
    SoboRoute(
        SOBOSITE_ROUTE_HANDLE.HELP.value,
        AnyRes(PubRes.string.help),
        { PageHelpSobosite() }),
    SoboRoute(
        SOBOSITE_ROUTE_HANDLE.SETTINGS.value,
        AnyRes(PubRes.string.settings),
        { PageSobositeSettings() }),

)
