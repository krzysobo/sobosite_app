package com.krzysobo.sobositeapp

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.krzysobo.sobositeapp.viewmodel.getUserStateVM
import com.krzysobo.soboapptpl.service.SoboRouter
import com.krzysobo.sobositeapp.settings.SOBOSITE_ROUTE_HANDLE
import com.krzysobo.sobositeapp.settings.sobositeRoutes


import com.krzysobo.sobositeapp.viewmodel.isLoggedIn
import com.krzysobo.sobositeapp.viewmodel.isLoggedInAdmin
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun getMenuItemsByUserStatus(
    menuItemsForLoggedInAdmin: List<com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem>,
    menuItemsForLoggedIn: List<com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem>,
    menuItemsForLoggedOut: List<com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem>,
): List<com.krzysobo.soboapptpl.widgets.menus.SoboMenuItem> {
    val userStateVM = getUserStateVM()
    userStateVM.isAdminRmb = remember { mutableStateOf(false) }
    userStateVM.isLoggedInRmb = remember { mutableStateOf(false) }

    val menuItems =
        if ((userStateVM.isLoggedInRmb.value) && (userStateVM.isAdminRmb.value)) {
            menuItemsForLoggedInAdmin
        } else if (userStateVM.isLoggedInRmb.value) {
            menuItemsForLoggedIn
        } else {
            menuItemsForLoggedOut
        }

    return menuItems
}


@Composable
@Preview
fun SobositeAndroidApp() {
    SoboRouter.initRouter(
        routes = sobositeRoutes,
        routeHandleLoggedInUserHome = SOBOSITE_ROUTE_HANDLE.DASHBOARD.value,
        routeHandleWelcome = SOBOSITE_ROUTE_HANDLE.WELCOME.value,
        routeHandleLogin = SOBOSITE_ROUTE_HANDLE.LOGIN.value,
        lmbIsLoggedIn = { isLoggedIn() },
        lmbIsLoggedInAdmin = { isLoggedInAdmin() },
        authUsed = true
    )
    SoboRouter.navigateToWelcomeIfBackStackEmpty()

    SoboTheme(useDarkTheme = com.krzysobo.soboapptpl.viewmodel.isDarkMode()) {
        val routesDebug = false
        Column {
            // ---- routes debug ----
            if (routesDebug) {
                Text("BS SIZE: ${SoboRouter.backStack.size}")
                Text("Current route: ${SoboRouter.getCurrentRoute().handle}")
                Text("Previous Route: ${SoboRouter.getPreviousBackStackItemIfAvailable()?.route?.handle ?: "-nope-"}")
            }
            // ---- /routes debug ----


            val activity = (LocalContext.current as? Activity)
            BackHandler(enabled = true) {  // handling the Smartphone's "BACK" button
                com.krzysobo.soboapptpl.service.handleAndroidBackButton(activity)
            }

            val menuItems = getMenuItemsByUserStatus(
                menuItemsForLoggedInAdmin = sobositeMenuItemsForLoggedInAdmin,
                menuItemsForLoggedIn = sobositeMenuItemsForLoggedIn,
                menuItemsForLoggedOut = sobositeMenuItemsForLoggedOut,
            )

            com.krzysobo.soboapptpl.widgets.menus.AppLayoutWithDrawerMenu(
                menuItems,
                {
                    if (it.routeHandle != "") {
                        SoboRouter.navigateToRouteHandle(it.routeHandle)
                    }
                },
                topAppBarTitle = "Sobosite App v. $appVersion",
                drawerAppTitle = "Sobosite App",
            )
        }
    }
}
