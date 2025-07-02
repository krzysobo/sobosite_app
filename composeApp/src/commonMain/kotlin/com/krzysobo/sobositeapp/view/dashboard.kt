package com.krzysobo.sobositeapp.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.soboapptpl.service.anyResText
import com.krzysobo.soboapptpl.widgets.PageHeader
import com.krzysobo.sobositeapp.appres.AppRes


@Composable
fun PageSobositeDashboard() {
    LazyColumn {
        item {
            PageHeader(anyResText(AnyRes(AppRes.string.dashboard_long_s, arrayOf("Sobosite"))))
        }

        item {
            Text(anyResText(AnyRes(AppRes.string.desc_sobosite_dashboard)))
        }
    }
}

