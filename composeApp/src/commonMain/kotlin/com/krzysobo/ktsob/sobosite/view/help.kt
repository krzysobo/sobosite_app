package com.krzysobo.ktsob.sobosite.view

//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import apptpl.composeapp.generated.resources.Res
import apptpl.composeapp.generated.resources.help_s
import apptpl.composeapp.generated.resources.more_at_url_s
import com.krzysobo.ktsob.appVersion
import com.krzysobo.ktsob.apptpl.service.AnyRes
import com.krzysobo.ktsob.apptpl.service.anyResText
import com.krzysobo.ktsob.apptpl.widgets.PageHeader


@Composable
fun PageHelpSobosite() {

    Column(
        Modifier
            .fillMaxSize()
            .padding(all = 32.dp)
    ) {
        PageHeader(anyResText(AnyRes(Res.string.help_s, arrayOf("Sobosite"))))
        Text(anyResText(AnyRes(Res.string.help_s, arrayOf("Sobosite v. $appVersion"))))
        Text(anyResText(AnyRes(Res.string.more_at_url_s,
            arrayOf("https://github.com/krzysobo/sobosite_app/blob/main/README.md")
        )))
    }
}


