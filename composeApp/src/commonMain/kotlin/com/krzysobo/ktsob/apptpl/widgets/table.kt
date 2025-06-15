package com.krzysobo.ktsob.apptpl.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import apptpl.composeapp.generated.resources.Res
import apptpl.composeapp.generated.resources.back
import apptpl.composeapp.generated.resources.forward
import apptpl.composeapp.generated.resources.no
import apptpl.composeapp.generated.resources.page_size
import apptpl.composeapp.generated.resources.total_items_n
import apptpl.composeapp.generated.resources.yes
import com.krzysobo.ktsob.apptpl.service.AnyRes
import com.krzysobo.ktsob.apptpl.service.anyResText


// ---------------------------------- SoboTable ---------------------------
//@Composable
fun LazyGridScope.soboTableHeader(
    columns: List<String>,
    headerBoxModifier: Modifier = Modifier
        .border(BorderStroke(width = 1.dp, color = Color.Gray)),
    textModifier: Modifier = Modifier
        .padding(all = 5.dp),
    textStyle: TextStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
) {
    items(columns) { column ->
        Box(
            modifier = headerBoxModifier
        ) {
            Text(
                modifier = textModifier,
                text = column,
                style = textStyle,
            )
        }
    }
}


@Composable
fun soboTableFooter(
    columns: List<String> = emptyList(),
    pageNoInit: Int = 0,
    itemsNo: Int = 0,
    totalPages: Int = 0,
    curPageSizeInit: Int = 10,
    options: List<Int> = listOf(5, 10, 15, 20, 50, 100, 1000),
    cbUpdatePageSize: (Int) -> Unit = {},
    cbPagerClickPrevPage: () -> Unit = {},
    cbPagerClickNextPage: () -> Unit = {},
    footerTextStyle: TextStyle = TextStyle(),
    footerTextModifier: Modifier = Modifier.padding(top = 15.dp, start = 15.dp),
    footerRowModifier: Modifier = Modifier.padding(top = 10.dp),
    comboTextModifier: Modifier = Modifier.width(100.dp),
    backButtonModifier: Modifier = Modifier.padding(end = 5.dp).size(size = 30.dp),
    forwardButtonModifier: Modifier = Modifier.padding(end = 5.dp).size(size = 30.dp),
) {
    var pageNo: Int = pageNoInit;
    var curPageSize: Int = curPageSizeInit;

//    Row(modifier = Modifier.border(BorderStroke(width = 1.dp, color = Color.Gray))) {
    var curOptionIndex = options.indexOf(curPageSize) // init
    if (curOptionIndex == -1) {
        curOptionIndex = 0
//        curPageSize = options[curOptionIndex]
    }

    val totalItems = anyResText(AnyRes(Res.string.total_items_n, itemsNo))
    val pageSize = anyResText(AnyRes(Res.string.page_size))

    Text(
        modifier = footerTextModifier,
        text = "$totalItems   $pageSize ",
        style = footerTextStyle
    )

    soboEasyCombo(
        options,
        onClick = { ind ->
            cbUpdatePageSize(options[ind])
        },
        selOptionIndex = curOptionIndex,
        textModifier = comboTextModifier
    )

    Row(modifier = footerRowModifier) {
        IconButton(
            modifier = backButtonModifier,
            onClick = {
                cbPagerClickPrevPage()
            },
            enabled = (pageNo > 1),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                anyResText(AnyRes(Res.string.back))
            )
        }

        val cellSize = with(LocalDensity.current) {
            MaterialTheme.typography.h6.fontSize.toDp() + 3.dp
        }

        val pageIndicatorWidth = cellSize * (
                "$pageNo".length + "$totalPages".length + 3)
        Text(
            modifier = Modifier
                .padding(all = 5.dp)
                .width(width = pageIndicatorWidth)
                .height(height = cellSize + 4.dp)
                .border(BorderStroke(width = 1.dp, color = Color.Gray)),
            style = footerTextStyle,
            text = "$pageNo / $totalPages"
        )
        IconButton(
            modifier = forwardButtonModifier,
            onClick = {
                cbPagerClickNextPage()
            },
            enabled = (pageNo < totalPages),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                anyResText(AnyRes(Res.string.forward))
            )
        }
    }
//    }

}

fun LazyGridScope.soboTableDataRow(
    dataRow: List<Any>,
    extraContent: (LazyGridScope.() -> Unit)?
) {
    for (field in dataRow) {
        item {
            Box(modifier = Modifier.padding(bottom = 10.dp)) {
                if (field is Boolean) {
                    Text(
                        text = if (field) anyResText(AnyRes(Res.string.yes)) else
                            anyResText(AnyRes(Res.string.no))
                    )
                } else {
                    Text(field.toString())
                }
            }
        }
    }

    if (extraContent != null) {
        extraContent()
    }
}
