package com.krzysobo.ktsob.apptpl.widgets.menus

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import apptpl.composeapp.generated.resources.Res
import apptpl.composeapp.generated.resources.color_scheme_dark
import apptpl.composeapp.generated.resources.color_scheme_light
import apptpl.composeapp.generated.resources.open_menu
import com.krzysobo.ktsob.apptpl.service.AnyRes
import com.krzysobo.ktsob.apptpl.service.SoboRoute
import com.krzysobo.ktsob.apptpl.service.SoboRouter
import com.krzysobo.ktsob.apptpl.service.anyResText
import com.krzysobo.ktsob.apptpl.widgets.routerOutlet
import com.krzysobo.ktsob.sobosite.viewmodel.actionLogoutUser
import com.krzysobo.ktsob.sobosite.viewmodel.isDarkMode
import com.krzysobo.ktsob.sobosite.viewmodel.toggleDarkMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


object DrawerData {
    val selectedItemId: MutableState<String> = mutableStateOf("")
}

data class SoboMenuItem(
    val id: String = "",
    val title: AnyRes? = null,
    val icon: ImageVector? = null,
    val routeHandle: String = "",
    val actionHandle: String = "",
    val route: SoboRoute? = null,
    val desc: AnyRes? = null,
)

@Composable
fun DrawerMenu(
    menuItems: List<SoboMenuItem> = emptyList(),
    itemTextStyle: TextStyle = TextStyle(fontSize = 20.sp),
    onClickMenuItem: (SoboMenuItem) -> Unit = {},
    drawerAppTitle: String = "",
    useIcons: Boolean = true,
    drawerState: DrawerState,
    scope: CoroutineScope,
) {
    Column {
        DrawerMenuHeader(drawerAppTitle)
        DrawerMenuBody(
            menuItems = menuItems,
            itemTextStyle = itemTextStyle,
            onClickMenuItem = onClickMenuItem,
            useIcons = useIcons,
            drawerState = drawerState,
            scope = scope
        )
    }
}


@Composable
fun DrawerMenuHeader(appTitle: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .height(height = 100.dp)
//            .border(width = 2.dp, color= Color.Red)
            .background(color = MaterialTheme.colors.secondary)
    ) {
        Text(
            appTitle,
            modifier = Modifier.padding(start = 50.dp, top = 30.dp, bottom = 30.dp, end = 10.dp),
            style = TextStyle(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold, fontSize = 35.sp
            )
        )
    }
}


@Composable
fun DrawerMenuBody(
    menuItems: List<SoboMenuItem> = emptyList(),
    itemTextStyle: TextStyle = TextStyle(fontSize = 20.sp),
    onClickMenuItem: (SoboMenuItem) -> Unit = {},
    useIcons: Boolean = true,
    drawerState: DrawerState,
    scope: CoroutineScope,
) {

    val currentRoute = SoboRouter.getCurrentRoute()
    val defaultIcon = Icons.Default.Circle

    LazyColumn {
        items(items = menuItems, key = { it.id }) {
//            Text("Title null? ${it.title == null}")
            if ((it.title == null) && (it.icon == null)) { // divider
                Divider(
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp)
                        .padding(start = 30.dp)
                        .clickable {
                            onClickMenuItem(it)
                            DrawerData.selectedItemId.value = it.id
                            if (it.routeHandle != "") {
                                scope.launch {
                                    drawerState.close()
                                }
                            }

                            if (it.actionHandle == "do_logout") {
                                scope.launch {
                                    actionLogoutUser()
                                }

                            }


                        }
                ) {
                    if (useIcons) {
                        Icon(
                            imageVector = it.icon ?: defaultIcon,
                            contentDescription = if (it.desc != null) anyResText(it.desc) else ""
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))

                    if ((it.routeHandle != "") && (currentRoute.handle == it.routeHandle)) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = if (it.title != null) anyResText(it.title) else "",
                            style = itemTextStyle.copy(fontWeight = FontWeight.Bold)
                        )
                    } else {
                        Text(
                            text = if (it.title != null ) anyResText(it.title) else "",
                            style = itemTextStyle,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 30.dp)
                    .padding(start = 30.dp)
                    .clickable {}
            ) {

                val colorSchemeText = anyResText(
                    AnyRes(
                        if (isDarkMode()) Res.string.color_scheme_dark
                        else Res.string.color_scheme_light
                    )
                )
                val colorSchemeIcon: ImageVector =
                    if (isDarkMode()) Icons.Default.DarkMode else Icons.Default.LightMode
                Icon(imageVector = colorSchemeIcon, contentDescription = "")
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    colorSchemeText,
                    style = itemTextStyle.copy(fontWeight = FontWeight.Normal),
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = isDarkMode(),
                    onCheckedChange = {
                        toggleDarkMode()

                    })
            }
        }
    }
}

@Composable
fun AppLayoutWithDrawerMenu(
    menuItems: List<SoboMenuItem> = emptyList(),
    onClickMenuItem: (SoboMenuItem) -> Unit = {},
    topAppBarTitle: String = "",
    drawerAppTitle: String = "",
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = { // drawer menu content
            DrawerMenu(
                menuItems = menuItems,
                onClickMenuItem = onClickMenuItem,
                drawerAppTitle = drawerAppTitle,
                drawerState = drawerState,
                scope = scope
            )
        },
        content = { // screen content
            Scaffold(
                // we can only open the menu with top button, to avoid accidental
                // "drawer dragging" while navigating on the screen with the drawer closed.
//                drawerGesturesEnabled = drawerState.isOpen,
                topBar = {
                    TopAppBar(
                        title = { Text(topAppBarTitle) },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) {
                                        drawerState.open()
                                    } else {
                                        drawerState.close()
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = anyResText(AnyRes(Res.string.open_menu))
                                )
                            }
                        },
                        backgroundColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary,
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(20.dp))
                    if (SoboRouter.isRouteSet()) {
                        routerOutlet()
                    }
                }
            }
        }
    )

}
