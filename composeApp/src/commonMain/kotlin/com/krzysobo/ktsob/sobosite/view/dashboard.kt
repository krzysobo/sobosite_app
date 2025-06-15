package com.krzysobo.ktsob.sobosite.view

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import apptpl.composeapp.generated.resources.Res
import apptpl.composeapp.generated.resources.dashboard_long_s
import apptpl.composeapp.generated.resources.desc_sobosite_dashboard
import com.krzysobo.ktsob.apptpl.service.AnyRes
import com.krzysobo.ktsob.apptpl.service.anyResText
import com.krzysobo.ktsob.apptpl.widgets.PageHeader


@Composable
fun PageSobositeDashboard() {
    PageHeader(anyResText(AnyRes(Res.string.dashboard_long_s, arrayOf("Sobosite"))))
    Text(anyResText(AnyRes(Res.string.desc_sobosite_dashboard)))
}

