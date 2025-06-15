package com.krzysobo.ktsob

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.FrameWindowScope
import com.krzysobo.ktsob.sobosite.SobositeDesktopApp
import com.krzysobo.ktsob.apptpl.widgets.DialogVisibility
import com.krzysobo.ktsob.sobosite.MainMenu

@Composable
fun FrameWindowScope.DesktopApp() {
    val isDialogHelpAboutOpen = remember { mutableStateOf(false) }
    val isDialogHelpContactOpen = remember { mutableStateOf(false) }
    val dialogStates = DialogVisibility(isDialogHelpAboutOpen, isDialogHelpContactOpen)

    MainMenu(dialogStates)

    SoboTheme {
        Column {
            Column {
                SobositeDesktopApp()
            }
        }
    }
}
