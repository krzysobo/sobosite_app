package com.krzysobo.ktsob.sobosite

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
import apptpl.composeapp.generated.resources.Res
import apptpl.composeapp.generated.resources.about
import apptpl.composeapp.generated.resources.help
import apptpl.composeapp.generated.resources.home
import apptpl.composeapp.generated.resources.login
import apptpl.composeapp.generated.resources.logout
import apptpl.composeapp.generated.resources.profile
import apptpl.composeapp.generated.resources.register
import apptpl.composeapp.generated.resources.register_confirm
import apptpl.composeapp.generated.resources.reset_pass_confirm
import apptpl.composeapp.generated.resources.settings
import apptpl.composeapp.generated.resources.users
import com.krzysobo.ktsob.apptpl.service.AnyRes
import com.krzysobo.ktsob.apptpl.widgets.menus.SoboMenuItem
import com.krzysobo.ktsob.sobosite.settings.SOBOSITE_ROUTE_HANDLE

val sobositeMenuItemsForLoggedOut: List<SoboMenuItem> = listOf(
    SoboMenuItem(
        "login",
        AnyRes(Res.string.login),
        Icons.AutoMirrored.Filled.Login,
        routeHandle = SOBOSITE_ROUTE_HANDLE.LOGIN.value
    ),
    SoboMenuItem(
        "register",
        AnyRes(Res.string.register),
        Icons.Default.AppRegistration,
        routeHandle = SOBOSITE_ROUTE_HANDLE.REGISTER.value
    ),

    SoboMenuItem("div1", null, null),

    SoboMenuItem(
        "about",
        AnyRes(Res.string.about),
        Icons.Default.Info,
        routeHandle = SOBOSITE_ROUTE_HANDLE.ABOUT.value
    ),
    SoboMenuItem(
        "help",
        AnyRes(Res.string.help),
        Icons.AutoMirrored.Filled.Help,
        routeHandle = SOBOSITE_ROUTE_HANDLE.HELP.value
    ),
    SoboMenuItem(
        "settings",
        AnyRes(Res.string.settings),
        Icons.Default.Settings,
        routeHandle = SOBOSITE_ROUTE_HANDLE.SETTINGS.value
    ),

    SoboMenuItem("div2", null, null),

    SoboMenuItem(
        "confirm_reg",
        AnyRes(Res.string.register_confirm),
        Icons.Default.VerifiedUser,
        routeHandle = SOBOSITE_ROUTE_HANDLE.REGISTER_CONFIRM.value
    ),
    SoboMenuItem(
        "confirm_pass_reset",
        AnyRes(Res.string.reset_pass_confirm),
        Icons.Default.VpnKey,
        routeHandle = SOBOSITE_ROUTE_HANDLE.CONFIRM_PASS_RESET.value
    ),
)

val sobositeMenuItemsForLoggedIn: List<SoboMenuItem> = listOf(
    SoboMenuItem(
        "home",
        AnyRes(Res.string.home),
        Icons.Default.Home,
        routeHandle = SOBOSITE_ROUTE_HANDLE.DASHBOARD.value
    ),
    SoboMenuItem(
        "profile",
        AnyRes(Res.string.profile),
        Icons.Default.Person,
        routeHandle = SOBOSITE_ROUTE_HANDLE.PROFILE.value
    ),
    SoboMenuItem(
        "logout",
        AnyRes(Res.string.logout),
        Icons.AutoMirrored.Filled.Logout,
        actionHandle = "do_logout"
    ),

    SoboMenuItem("div1", null, null),

    SoboMenuItem(
        "about",
        AnyRes(Res.string.about),
        Icons.Default.Info,
        routeHandle = SOBOSITE_ROUTE_HANDLE.ABOUT.value
    ),
    SoboMenuItem(
        "help",
        AnyRes(Res.string.help),
        Icons.AutoMirrored.Filled.Help,
        routeHandle = SOBOSITE_ROUTE_HANDLE.HELP.value
    ),
    SoboMenuItem(
        "settings",
        AnyRes(Res.string.settings),
        Icons.Default.Settings,
        routeHandle = SOBOSITE_ROUTE_HANDLE.SETTINGS.value
    ),
)

val sobositeMenuItemsForLoggedInAdmin: List<SoboMenuItem> = listOf(
    SoboMenuItem(
        "home",
        AnyRes(Res.string.home),
        Icons.Default.Home,
        routeHandle = SOBOSITE_ROUTE_HANDLE.DASHBOARD.value
    ),
    SoboMenuItem(
        "profile",
        AnyRes(Res.string.profile),
        Icons.Default.Person,
        routeHandle = SOBOSITE_ROUTE_HANDLE.PROFILE.value
    ),
    SoboMenuItem(
        "admin_users",
        AnyRes(Res.string.users),
        Icons.AutoMirrored.Filled.List,
        SOBOSITE_ROUTE_HANDLE.ADMIN_USERS.value
    ),
    SoboMenuItem(
        "logout",
        AnyRes(Res.string.logout),
        Icons.AutoMirrored.Filled.Logout,
        actionHandle = "do_logout"
    ),

    SoboMenuItem("div1", null, null),

    SoboMenuItem(
        "about",
        AnyRes(Res.string.about),
        Icons.Default.Info,
        routeHandle = SOBOSITE_ROUTE_HANDLE.ABOUT.value
    ),
    SoboMenuItem(
        "help",
        AnyRes(Res.string.help),
        Icons.AutoMirrored.Filled.Help,
        routeHandle = SOBOSITE_ROUTE_HANDLE.HELP.value
    ),
    SoboMenuItem(
        "settings",
        AnyRes(Res.string.settings),
        Icons.Default.Settings,
        routeHandle = SOBOSITE_ROUTE_HANDLE.SETTINGS.value
    ),
)
