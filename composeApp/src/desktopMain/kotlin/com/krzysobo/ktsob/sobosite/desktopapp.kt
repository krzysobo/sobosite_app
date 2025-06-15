package com.krzysobo.ktsob.sobosite


import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import apptpl.composeapp.generated.resources.Res
import apptpl.composeapp.generated.resources.app_name_sobosite_desktop
import com.krzysobo.ktsob.SoboTheme
import com.krzysobo.ktsob.apptpl.service.SoboRouter
import com.krzysobo.ktsob.apptpl.widgets.menus.PageTabsWithOutletAndLogin
import com.krzysobo.ktsob.sobosite.settings.SOBOSITE_ROUTE_HANDLE
import com.krzysobo.ktsob.sobosite.settings.sobositeRouteHandlesForMenu
import com.krzysobo.ktsob.sobosite.settings.sobositeRoutes
import com.krzysobo.ktsob.sobosite.viewmodel.isLoggedIn
import com.krzysobo.ktsob.sobosite.viewmodel.isLoggedInAdmin
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun SobositeDesktopApp() {
    SoboRouter.initRouter(
        routes = sobositeRoutes,
        routeHandleLoggedInUserHome = SOBOSITE_ROUTE_HANDLE.DASHBOARD.value,
        routeHandleLogin = SOBOSITE_ROUTE_HANDLE.LOGIN.value,
        lmbIsLoggedIn = { isLoggedIn() },
        lmbIsLoggedInAdmin = { isLoggedInAdmin() }
    )

    SoboTheme {
        Column {
            Text(stringResource(Res.string.app_name_sobosite_desktop))
            PageTabsWithOutletAndLogin(sobositeRouteHandlesForMenu, sobositeRoutes)
        }
    }
}