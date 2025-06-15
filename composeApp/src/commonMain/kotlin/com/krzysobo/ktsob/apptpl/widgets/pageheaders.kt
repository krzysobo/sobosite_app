package com.krzysobo.ktsob.apptpl.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import apptpl.composeapp.generated.resources.Res
import apptpl.composeapp.generated.resources.error_occurred
import com.krzysobo.ktsob.apptpl.service.AnyRes
import com.krzysobo.ktsob.apptpl.service.anyResText

@Composable
fun ErrorText(
    errorText: String,
    textStyle: TextStyle = TextStyle(color = Color(255, 0, 0))
) {
    Text(
        if (errorText != "") errorText else anyResText(AnyRes(Res.string.error_occurred)),
        style = textStyle
    )
}

@Composable
fun MessageBox(
    textHeader: String,
    textBody: String,
    borderColor: Color = Color.Blue,
    headerColor: Color = Color.Black
) {
    val annTextBody = buildAnnotatedString {
        append(textBody)
    }

    return MessageBox(textHeader, annTextBody, borderColor, headerColor)
}

@Composable
fun MessageBox(
    textHeader: String,
    textBody: AnnotatedString,
    borderColor: Color = Color.Blue,
    headerColor: Color = Color.Black
) {
    Card(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .border(BorderStroke(width = 1.dp, color = borderColor)),
    ) {
        Column(
            modifier = Modifier.background(
                color = Color(red = 0xEE, green = 0xEE, blue = 0xEE, alpha = 0xAA)
            )
        ) {
            Text(
                textHeader,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = headerColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )

            Text(
                textBody,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(10.dp)
            )
        }
    }
}

@Composable
fun ErrorMessageBox(textHeader: String, textBody: String) {
    MessageBox(textHeader, textBody, borderColor = Color.Red, headerColor = Color.Red)
}

@Composable
fun ErrorMessageBox(textHeader: String, textBody: AnnotatedString) {
    MessageBox(textHeader, textBody, borderColor = Color.Red, headerColor = Color.Red)
}


@Composable
fun PageHeader(textHeader: String) {
    Text(
        textHeader,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 15.dp)
    )
}
