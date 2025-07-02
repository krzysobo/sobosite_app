package com.krzysobo.sobositeapp


//import com.krzysobo.sobositeapp.SoboTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.FrameWindowScope
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.soboapptpl.service.LocaleManager
import com.krzysobo.soboapptpl.service.SoboRouter
import com.krzysobo.soboapptpl.service.anyResText
import com.krzysobo.soboapptpl.viewmodel.isRefreshCompose
import com.krzysobo.soboapptpl.widgets.menus.PageTabsWithOutletAndLogin
import com.krzysobo.sobositeapp.appres.AppRes
import com.krzysobo.sobositeapp.settings.SOBOSITE_ROUTE_HANDLE
import com.krzysobo.sobositeapp.settings.sobositeRouteHandlesForDesktopMenu
import com.krzysobo.sobositeapp.settings.sobositeRoutes
import com.krzysobo.sobositeapp.viewmodel.actionLogoutUser
import com.krzysobo.sobositeapp.viewmodel.isLoggedIn
import com.krzysobo.sobositeapp.viewmodel.isLoggedInAdmin
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun SobositeDesktopApp() {
    LocaleManager.useLocaleFromAppSettings()

    SoboRouter.initRouter(
        routes = sobositeRoutes,
        routeHandleLoggedInUserHome = SOBOSITE_ROUTE_HANDLE.DASHBOARD.value,
        routeHandleWelcome = SOBOSITE_ROUTE_HANDLE.WELCOME.value,

        routeHandleLogin = SOBOSITE_ROUTE_HANDLE.LOGIN.value,
        lmbIsLoggedIn = { isLoggedIn() },
        lmbIsLoggedInAdmin = { isLoggedInAdmin() }
    )
    val coroutineScope = rememberCoroutineScope()

    SoboTheme {
        Column {
            Text(anyResText(AnyRes(AppRes.string.app_name_sobosite_desktop)))
            PageTabsWithOutletAndLogin(
                sobositeRouteHandlesForDesktopMenu,
                sobositeRoutes,
                { isRefreshCompose() },
                { coroutineScope.launch { actionLogoutUser() } },
                { isLoggedIn() },
                { isLoggedInAdmin() }
            )
        }
    }
}


@Composable
fun FrameWindowScope.DesktopApp() {

    SoboTheme {
        Column {
            SobositeDesktopApp()
        }
    }
}