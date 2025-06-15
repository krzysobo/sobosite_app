package com.krzysobo.ktsob.apptpl.service

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

open class AppRequest

enum class USER_ROLE(val roleId: Int) {
    ANY_USER(1),
    ANON_ONLY(2),
    USER(10),
    ADMIN(20)
}

data class SoboRoute(
    val handle: String,
//    val title: String,
    val title: AnyRes?,
    val func: @Composable () -> Unit,
    val perms: List<USER_ROLE> = listOf(USER_ROLE.ANY_USER),
    val funcWithReq: (@Composable (AppRequest) -> Unit)? = null
)


fun filterRoutesByUserStatus(
    routesForMenu: List<String>,
    allRoutes: List<SoboRoute>,
    isLoggedIn: Boolean, isAdmin: Boolean
): List<SoboRoute> {
    var routesOut = allRoutes.filter {
        if (routesForMenu.contains(it.handle)) {
            if ((isLoggedIn) && (isAdmin)) {
                (it.perms.contains(USER_ROLE.ADMIN) || it.perms.contains(USER_ROLE.USER) || it.perms.contains(
                    USER_ROLE.ANY_USER
                ))
            } else if (isLoggedIn) {
                (it.perms.contains(USER_ROLE.USER) || it.perms.contains(USER_ROLE.ANY_USER))
            } else {
                (it.perms.contains(USER_ROLE.ANON_ONLY) || it.perms.contains(USER_ROLE.ANY_USER))
            }
        } else {
            false
        }
    }

    return routesOut
}


object SoboRouter {
    var routes: List<SoboRoute> = emptyList()
    var routeHandleLoggedInUserHome: String = ""
    var routeHandleAnonymousUserHome: String = ""
    var routeHandleLogin: String = ""
    var lmbIsLoggedIn: () -> Boolean = { false }
    var lmbIsLoggedInAdmin: () -> Boolean = { false }
    var backStack: List<Any> = emptyList()


    fun initRouter(
        routes: List<SoboRoute> = emptyList(),
        routeHandleAnonymousUserHome: String = "",
        routeHandleLoggedInUserHome: String = "",
        routeHandleLogin: String = "",
        lmbIsLoggedIn: () -> Boolean = { false },
        lmbIsLoggedInAdmin: () -> Boolean = { false },
    ) {

        this.routes = routes
        this.routeHandleLoggedInUserHome = routeHandleLoggedInUserHome
        this.routeHandleAnonymousUserHome = routeHandleAnonymousUserHome
        this.routeHandleLogin = routeHandleLogin
        this.lmbIsLoggedIn = lmbIsLoggedIn
        this.lmbIsLoggedInAdmin = lmbIsLoggedInAdmin
    }

    private fun getRouteByHandle(handle: String): SoboRoute {
//        println("\n======== getRouteByHandle - handle: $handle\n")
        val route: SoboRoute? = routes.find { it.handle == handle }
        if (route == null) {
//            print("\n\n======= !!!!!!!!!!!!!!!!!!!!!!!route $handle does not exist !!!!!!!!!!!!!!!!!!!!!!")
//            println("\n-----> routes length: ${routes.size}")
//            for (rr in routes) {
//                println("\n ----> QQQ ROUTE HANDLE: ${rr.handle}\n")
//
//            }
//            return SoboRoute("", "", {})
            throw NoSuchElementException("the route $handle does not exist")
        }

        return route
    }

    fun navigateToHome() {
        if (lmbIsLoggedIn()) {
//            println("navigateToHome -- REDIRECTING TO $routeHandleLoggedInUserHome")
            // redirect to our own home (USER or ADMIN)
            val targetRoute = getRouteByHandle(routeHandleLoggedInUserHome)
            setCurrentRoute(targetRoute, null)
        }
    }

    fun navigateToLogin() {
        if (!lmbIsLoggedIn()) {   // navigateToLogin -- REDIRECTING TO $routeHandleLogin
            // redirect to LOGIN page
            val targetRoute = getRouteByHandle(routeHandleLogin)
            setCurrentRoute(targetRoute, null)
        }
    }

    fun navigateToRouteHandle(routeHandle: String, req: AppRequest? = null) {
        val route: SoboRoute = getRouteByHandle(routeHandle)
        setCurrentRoute(route, req)
    }

    fun navigateToRoute(route: SoboRoute, req: AppRequest? = null) {
        setCurrentRoute(route, req)
    }

    fun applyPermsToRoute(route: SoboRoute) {
        var loggedIn: Boolean = lmbIsLoggedIn()
        var isAdmin: Boolean = lmbIsLoggedInAdmin()

//        println("====== applyPermsToRoute - LOGGED IN: $loggedIn isAdmin: $isAdmin")

        with(route.perms) {
            when {
                contains(USER_ROLE.ANY_USER) -> {  //  "ANY USER - OK!!!"
                    // always proceed
                    return
                }

                contains(USER_ROLE.ANON_ONLY) -> {
                    // TODO check if user logged in. If yes, redirect to DEFAULT_ROUTE (home)
                    if (loggedIn) { // ANON ONLY - LOGGED IN - NOT OK!!! REDIRECTING TO $routeHandleLoggedInUserHome
                        // redirect to our own home (USER or ADMIN)

                        val targetRoute = getRouteByHandle(routeHandleLoggedInUserHome)
                        setCurrentRoute(targetRoute, null)

                        return
                    } else {     // ANON ONLY - NOT LOGGED IN - OK!!!
                        return
                    }
                }

                contains(USER_ROLE.USER) -> {
                    if (loggedIn) { // ROLE USER - LOGGED IN - OK!!!
                        // if logged in, let's go in - USER or ADMIN
                        return
                    } else {   // ROLE USER - NOT LOGGED IN - NOT OK!!! REDIRECTING TO $routeHandleLogin
                        // redirect to login page
                        val targetRoute = getRouteByHandle(routeHandleLogin)
                        setCurrentRoute(targetRoute)
                        return
                    }
                }

                contains(USER_ROLE.ADMIN) -> {
                    if (isAdmin) {   // ROLE ADMIN - LOGGED IN AND ADMIN - OK!!!

                        // if logged in AND admin, let's go in - ADMIN ONLY
                    } else if (loggedIn) { // ROLE ADMIN - LOGGED IN AND NOT ADMIN - NOT OK!!! REDIRECTING TO $routeHandleLoggedInUserHome
                        val targetRoute = getRouteByHandle(routeHandleLoggedInUserHome)
                        setCurrentRoute(targetRoute)
                        return
                    } else {    // ROLE ADMIN - NOT LOGGED IN - NOT OK! REDIRECTING TO $routeHandleLogin
                        val targetRoute = getRouteByHandle(routeHandleLogin)
                        setCurrentRoute(targetRoute)
                        return
                    }
                }

            }
        }
    }

    var currentRouteHandle: MutableState<String> = mutableStateOf("")
    var currentRoute: MutableState<SoboRoute> =
        mutableStateOf(SoboRoute("", null, {}, emptyList(), null))
    var currentRequest: MutableState<AppRequest?> = mutableStateOf(null)

    private fun setCurrentRoute(route: SoboRoute, req: AppRequest? = null) {
        currentRoute.value = route
        currentRouteHandle.value = route.handle
        currentRequest.value = req
    }

    fun getCurrentRoute(): SoboRoute {
        return currentRoute.value
    }

    fun getCurrentRequest(): AppRequest? {
        return currentRequest.value
    }

    fun isRouteSet(): Boolean {
        return (currentRoute.value.handle != "")
    }

}
