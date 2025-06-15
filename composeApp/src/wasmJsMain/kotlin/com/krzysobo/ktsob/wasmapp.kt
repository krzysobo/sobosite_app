package com.krzysobo.ktsob

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.krzysobo.ktsob.sobosite.SobositeWasmApp
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun WasmApp() {
    SoboTheme {
        Column {
             SobositeWasmApp()
        }
    }
}