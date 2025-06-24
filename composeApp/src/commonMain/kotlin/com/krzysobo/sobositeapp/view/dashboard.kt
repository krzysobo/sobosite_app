package com.krzysobo.sobositeapp.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import apptpl.composeapp.generated.resources.Res
import apptpl.composeapp.generated.resources.dashboard_long_s
import apptpl.composeapp.generated.resources.desc_sobosite_dashboard
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.soboapptpl.service.anyResText
import com.krzysobo.soboapptpl.widgets.PageHeader


@Composable
fun PageSobositeDashboard() {
    LazyColumn {
        item {
            PageHeader(anyResText(AnyRes(Res.string.dashboard_long_s, arrayOf("Sobosite"))))
        }

        item {
            Text(anyResText(AnyRes(Res.string.desc_sobosite_dashboard)))
        }
    }
}

