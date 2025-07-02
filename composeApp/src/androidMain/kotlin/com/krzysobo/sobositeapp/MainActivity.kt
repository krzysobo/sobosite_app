package com.krzysobo.sobositeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.krzysobo.soboapptpl.service.LocaleManager
import androidx.compose.material.Text
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        LocaleManager.storeOriginalLocale()
        super.onCreate(savedInstanceState)

//        var xxx: Int = 0

        setContent {
//            LocalConfiguration.current.setLocale(Locale.forLanguageTag("fr"))
//            LocalConfiguration.current.locales[0].language = "fr"
//            println("TESTXX::: XXX: $xxx")
            LocaleManager.useLocaleFromAppSettings()
//            xxx += 1
//
//            val time = Calendar.getInstance().time
//            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.forLanguageTag("pl"))
//            val current = formatter.format(time)
//            println("TESTXX CURRENT DATE AND TIME: $current")
            println("TESTXX DEFAULT LOCALE LANGUAGE BEFORE: ${Locale.getDefault().language}")
            Column {
//                Text("ABC $current")
                SobositeAndroidApp()
            }
//            this.recreate()
            println("TESTXX DEFAULT LOCALE LANGUAGE AFTER: ${Locale.getDefault().language}")
            println("TESTXX -> MainActivity -> LANGUAGE in LocalConfiguration - not related to Default --: locale: ${LocalConfiguration.current.locale.language} locales: ${LocalConfiguration.current.locales[0].language}")
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    SobositeAndroidApp()
}
