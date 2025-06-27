package com.krzysobo.sobositeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.krzysobo.soboapptpl.service.LocaleManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        LocaleManager.storeOriginalLocale()
        super.onCreate(savedInstanceState)

        setContent {
//            this.recreate()
            SobositeAndroidApp()
            println("TESTXX -> MainActivity -> LANGUAGE: locale: ${LocalConfiguration.current.locale.language} locales: ${LocalConfiguration.current.locales[0].language}")
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    SobositeAndroidApp()
}
