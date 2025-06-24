package com.krzysobo.sobositeapp


import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import com.krzysobo.soboapptpl.widgets.DialogVisibility

@Composable
fun FrameWindowScope.MainMenu(dialogStates: DialogVisibility) {
    MenuBar {
        Menu("Help", mnemonic = 'H') {
            Item(
                "About Sobosite",
                mnemonic = 'A',
                onClick = {
                    dialogStates.helpAboutOpen.value = true;
//                        dv.helpAbout = true;
                }
            )
            Item(
                "Contact Me",
                mnemonic = 'C',
                onClick = { dialogStates.helpAboutOpen.value = true; }
            )
        }
    }

}
