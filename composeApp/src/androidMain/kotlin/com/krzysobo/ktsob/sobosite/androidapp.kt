package com.krzysobo.ktsob.sobosite

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.krzysobo.ktsob.SoboTheme
import com.krzysobo.ktsob.appVersion
import com.krzysobo.ktsob.apptpl.service.SoboRouter
import com.krzysobo.ktsob.sobosite.settings.SOBOSITE_ROUTE_HANDLE
import com.krzysobo.ktsob.sobosite.viewmodel.getUserStateVM
import com.krzysobo.ktsob.sobosite.viewmodel.isDarkMode
import com.krzysobo.ktsob.apptpl.widgets.menus.AppLayoutWithDrawerMenu
import com.krzysobo.ktsob.sobosite.settings.sobositeRoutes
import com.krzysobo.ktsob.sobosite.viewmodel.isLoggedIn
import com.krzysobo.ktsob.sobosite.viewmodel.isLoggedInAdmin
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun SobositeAndroidApp() {
    SoboRouter.initRouter(
        routes = sobositeRoutes,
        routeHandleLoggedInUserHome = SOBOSITE_ROUTE_HANDLE.DASHBOARD.value,
        routeHandleLogin = SOBOSITE_ROUTE_HANDLE.LOGIN.value,
        lmbIsLoggedIn = { isLoggedIn() },
        lmbIsLoggedInAdmin = { isLoggedInAdmin() }
    )

    SoboTheme(useDarkTheme = isDarkMode()) {
        val userStateVM = getUserStateVM()
        Column {
            userStateVM.isAdminRmb = remember { mutableStateOf(false) }
            userStateVM.isLoggedInRmb = remember { mutableStateOf(false) }

            val menuItems =
                if ((userStateVM.isLoggedInRmb.value) && (userStateVM.isAdminRmb.value)) {
                    sobositeMenuItemsForLoggedInAdmin
                } else if (userStateVM.isLoggedInRmb.value) {
                    sobositeMenuItemsForLoggedIn
                } else {
                    sobositeMenuItemsForLoggedOut
                }

            AppLayoutWithDrawerMenu(
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
