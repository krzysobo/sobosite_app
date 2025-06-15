package com.krzysobo.ktsob.apptpl.widgets

import androidx.compose.runtime.Composable
import com.krzysobo.ktsob.apptpl.service.SoboRouter
import androidx.compose.material.Text

@Composable
fun routerOutlet() {
//    Text("Router Outlet")
    var curRoute = SoboRouter.getCurrentRoute()
    var curRequest = SoboRouter.getCurrentRequest()

//    println("\n====> routerOutlet() - curRoute: ${curRoute.handle}\n")
    SoboRouter.applyPermsToRoute(curRoute)

    if ((curRoute.funcWithReq != null) && (curRequest != null)) {
//        println("\n======> routerOutlet() - invoking funcWithReq for route ${curRoute.handle}\n")
        curRoute.funcWithReq?.invoke(curRequest)
    } else {
//        println("\n======> routerOutlet() - invoking NORMAL func for route ${curRoute.handle}\n")
        curRoute.func.invoke()

    }

}