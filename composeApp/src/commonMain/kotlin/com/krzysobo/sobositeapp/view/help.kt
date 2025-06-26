package com.krzysobo.sobositeapp.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.krzysobo.soboapptpl.pubres.PubRes
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.soboapptpl.service.anyResText
import com.krzysobo.soboapptpl.widgets.PageHeader
import com.krzysobo.sobositeapp.appVersion


@Composable
fun PageHelpSobosite() {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(all = 32.dp)
    ) {
        item {
            PageHeader(anyResText(AnyRes(PubRes.string.help_s, arrayOf("Sobosite"))))
        }

        item {
            Text(anyResText(AnyRes(PubRes.string.help_s, arrayOf("Sobosite v. $appVersion"))))
            Text(
                anyResText(
                    AnyRes(
                        PubRes.string.more_at_url_s,
                        arrayOf("https://github.com/krzysobo/sobosite_app/blob/main/README.md")
                    )
                )
            )
        }
    }
}


