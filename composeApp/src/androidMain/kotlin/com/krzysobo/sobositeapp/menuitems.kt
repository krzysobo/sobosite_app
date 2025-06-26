package com.krzysobo.sobositeapp

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VpnKey
import com.krzysobo.soboapptpl.pubres.PubRes
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.sobositeapp.settings.SOBOSITE_ROUTE_HANDLE
import com.krzysobo.sobositeapp.viewmodel.actionLogoutUser
import kotlinx.coroutines.launch
import sobositeapp.composeapp.generated.resources.Res
import sobositeapp.composeapp.generated.resources.profile
import sobositeapp.composeapp.generated.resources.register
import sobositeapp.composeapp.generated.resources.register_confirm
import sobositeapp.composeapp.generated.resources.reset_pass_confirm
import sobositeapp.composeapp.generated.resources.users

val aboutItem = com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem(
    "about",
    AnyRes(PubRes.string.about),
    Icons.Default.Info,
    routeHandle = SOBOSITE_ROUTE_HANDLE.ABOUT.value
)

val helpItem = com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem(
    "help",
    AnyRes(PubRes.string.help),
    Icons.AutoMirrored.Filled.Help,
    routeHandle = SOBOSITE_ROUTE_HANDLE.HELP.value
)

val settingsItem = com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem(
    "settings",
    AnyRes(PubRes.string.settings),
    Icons.Default.Settings,
    routeHandle = SOBOSITE_ROUTE_HANDLE.SETTINGS.value
)

val loginItem = com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem(
    "login",
    AnyRes(PubRes.string.login),
    Icons.AutoMirrored.Filled.Login,
    routeHandle = SOBOSITE_ROUTE_HANDLE.LOGIN.value
)

val registerItem = com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem(
    "register",
    AnyRes(Res.string.register),
    Icons.Default.AppRegistration,
    routeHandle = SOBOSITE_ROUTE_HANDLE.REGISTER.value
)

val homeItem = com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem(
    "home",
    AnyRes(PubRes.string.home),
    Icons.Default.Home,
    routeHandle = SOBOSITE_ROUTE_HANDLE.DASHBOARD.value
)

val profileItem = com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem(
    "profile",
    AnyRes(Res.string.profile),
    Icons.Default.Person,
    routeHandle = SOBOSITE_ROUTE_HANDLE.PROFILE.value
)

val logoutItem = com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem(
    "logout",
    AnyRes(PubRes.string.logout),
    Icons.AutoMirrored.Filled.Logout,
    actionHandle = "do_logout",
    actionFunc = { scope ->
        scope.launch {
            actionLogoutUser()
        }
    }
)

val confirmRegItem = com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem(
    "confirm_reg",
    AnyRes(Res.string.register_confirm),
    Icons.Default.VerifiedUser,
    routeHandle = SOBOSITE_ROUTE_HANDLE.REGISTER_CONFIRM.value
)

val confirmPassResetItem = com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem(
    "confirm_pass_reset",
    AnyRes(Res.string.reset_pass_confirm),
    Icons.Default.VpnKey,
    routeHandle = SOBOSITE_ROUTE_HANDLE.CONFIRM_PASS_RESET.value
)

val adminUsersItem = com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem(
    "admin_users",
    AnyRes(Res.string.users),
    Icons.AutoMirrored.Filled.List,
    SOBOSITE_ROUTE_HANDLE.ADMIN_USERS.value
)


val sobositeMenuItemsForLoggedOut: List<com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem> =
    listOf(
        loginItem,
        registerItem,
        com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem("div1", null, null),

        aboutItem,
        helpItem,
        settingsItem,
        com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem("div2", null, null),

        confirmRegItem,
        confirmPassResetItem
    )

val sobositeMenuItemsForLoggedIn: List<com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem> = listOf(
    homeItem,
    profileItem,
    logoutItem,
    com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem("div1", null, null),

    aboutItem,
    helpItem,
    settingsItem
)


@SuppressLint("CoroutineCreationDuringComposition")
val sobositeMenuItemsForLoggedInAdmin: List<com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem> =
    listOf(
        homeItem,
        profileItem,
        adminUsersItem,
        logoutItem,
        com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem("div1", null, null),

        aboutItem,
        helpItem,
        settingsItem
    )
