package com.krzysobo.sobositeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.krzysobo.soboapptpl.service.LocaleManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleManager.storeOriginalLocale()
        super.onCreate(savedInstanceState)

        setContent {
            LocaleManager.useLocaleFromAppSettings()
//            println("TESTXX DEFAULT LOCALE LANGUAGE BEFORE: ${Locale.getDefault().language}")
            Column {
                SobositeAndroidApp()
            }
//            this.recreate()
//            println("TESTXX DEFAULT LOCALE LANGUAGE AFTER: ${Locale.getDefault().language}")
//            println("TESTXX -> MainActivity -> LANGUAGE in LocalConfiguration - not related to Default --: locale: ${LocalConfiguration.current.locale.language} locales: ${LocalConfiguration.current.locales[0].language}")
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    SobositeAndroidApp()
}
