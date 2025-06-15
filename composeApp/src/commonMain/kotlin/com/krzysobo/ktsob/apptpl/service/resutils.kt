package com.krzysobo.ktsob.apptpl.service

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

class AnyRes(val res: StringResource?, val resArgs: Array<Any> = emptyArray()) {
    constructor(res: StringResource?, argX: String) : this(res, arrayOf(argX))
    constructor(res: StringResource?, argX: Int) : this(res, arrayOf(argX))
    constructor(res: StringResource?, argX: Float) : this(res, arrayOf(argX))
    constructor(res: StringResource?, argX: Double) : this(res, arrayOf(argX))
    constructor(res: StringResource?, argX: Char) : this(res, arrayOf(argX))

//    init {
//        println("AnyRes - main - res: ${res} RESARGS: ${resArgs.size}")
//    }

    private var text: String = ""

    constructor(textIn: String) : this(null, emptyArray()) {
        text = textIn
    }

    @Composable
    fun getText(): String {
        if (res != null) {
            val resOut = if (resArgs.size > 0) {
                stringResource(res, *resArgs)
            } else {
                stringResource(res)
            }

            return resOut
        }

        return text
    }
}


@Composable
fun anyResText(anyRes: AnyRes?): String {
    return anyRes?.getText() ?: ""

}