package com.krzysobo.ktsob.sobosite

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.krzysobo.ktsob.SoboTheme
import com.krzysobo.ktsob.apptpl.widgets.menus.PageTabsWithOutletAndLogin
import com.krzysobo.ktsob.sobosite.settings.sobositeRouteHandlesForMenu
import com.krzysobo.ktsob.sobosite.settings.sobositeRoutes
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun SobositeWasmApp() {
    SoboTheme {
        Column {
            Text("Sobosite Wasm App")
            PageTabsWithOutletAndLogin(sobositeRouteHandlesForMenu, sobositeRoutes)
        }
    }
}