package com.krzysobo.sobositeapp.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import apptpl.composeapp.generated.resources.Res
import apptpl.composeapp.generated.resources.more_at_url_s
import apptpl.composeapp.generated.resources.welcome_s
import apptpl.composeapp.generated.resources.welcome_story_sobosite
import com.krzysobo.sobositeapp.appVersion
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.soboapptpl.service.anyResText
import com.krzysobo.soboapptpl.widgets.PageHeader


@Composable
fun PageSobositeWelcome() {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(all = 32.dp)
    ) {
        item {
            PageHeader(anyResText(AnyRes(Res.string.welcome_s, arrayOf("Sobosite"))))
        }

        item {
            Text(anyResText(AnyRes(Res.string.welcome_story_sobosite)))
        }

        item {
            Text("Sobosite App v. $appVersion")
            Text("Copyright (c) Krzysztof Sobolewski <krzysztof.sobolewski@gmail.com>")
            Text("Apache 2.0 License")

            Text(
                anyResText(
                    AnyRes(
                        Res.string.more_at_url_s,
                        arrayOf("https://github.com/krzysobo/sobosite_app/blob/main/README.md")
                    )
                )
            )
        }
    }
}
