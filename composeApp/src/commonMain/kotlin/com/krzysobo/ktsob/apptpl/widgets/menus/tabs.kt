package com.krzysobo.ktsob.apptpl.widgets.menus

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.krzysobo.ktsob.apptpl.service.SoboRoute
import com.krzysobo.ktsob.apptpl.service.SoboRouter
import com.krzysobo.ktsob.apptpl.service.anyResText
import com.krzysobo.ktsob.apptpl.service.filterRoutesByUserStatus
import com.krzysobo.ktsob.apptpl.widgets.routerOutlet
import com.krzysobo.ktsob.sobosite.viewmodel.actionLogoutUser
import com.krzysobo.ktsob.sobosite.viewmodel.isLoggedIn
import com.krzysobo.ktsob.sobosite.viewmodel.isLoggedInAdmin
import com.krzysobo.ktsob.sobosite.viewmodel.isRefreshCompose
import kotlinx.coroutines.launch


@Composable
fun PageTabsWithOutletAndLogin(routesForMenu: List<String>, routes: List<SoboRoute>) {
//    var selectedTabIndex = remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    /**
     * login/logout button widget
     */
    Row {
        Column {
            if (isRefreshCompose()) {
                // this is The Refresher - it may even be an empty string, it must
                // be in reaction to a mutableStateOf changes
//                println("COMPOSE REFRESHED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                Text("")
            }
            if (isLoggedIn()) {
                Button(onClick = {
                    coroutineScope.launch {
                        actionLogoutUser()
                    }
                }) {
                    Text("Log Out")
                }
            } else {
                Button(onClick = { SoboRouter.navigateToRouteHandle("login") }) {
                    Text("Log In")
                }
            }
        }
    }

    /**
     * -- tabs --
     */
    var routesOut = filterRoutesByUserStatus(
        routesForMenu, routes,
        isLoggedIn(),
        isLoggedInAdmin()
    )

    val modSel = Modifier.drawBehind {
        val borderWidth = 3.dp.toPx()
        drawLine(
            color = Color.White,
            start = Offset(0f, size.height - 6),
            end = Offset(size.width, size.height - 6),
            strokeWidth = borderWidth
        )
    }.padding(4.dp)

    /**
     * TOP-BAR TABS
     */
    TabRow(selectedTabIndex = 0, indicator = {}) {
        routesOut.forEachIndexed { index, obj ->
            val isSelected = obj.handle == SoboRouter.getCurrentRoute().handle
            Tab(
                selected = isSelected,
                onClick = {
                    SoboRouter.navigateToRoute(obj)
                },
                modifier = if (isSelected) modSel else Modifier,
                text = {
                    Text(
                        text = if (obj.title != null) anyResText(obj.title) else "",
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
            )
        }
    }

    /**
     * -- selected page CONTENT --
     */
    Column(modifier = Modifier.padding(all = 10.dp)) {
        if (!SoboRouter.isRouteSet()) {
//            println("\n====> Route not sent - navigating to login \n")
            SoboRouter.navigateToLogin()
        }

        routerOutlet()
    }

}
