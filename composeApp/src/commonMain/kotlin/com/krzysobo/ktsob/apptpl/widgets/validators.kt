package com.krzysobo.ktsob.apptpl.widgets

import androidx.compose.runtime.MutableState


fun strNotEmpty(value: String): Boolean {

    return value != ""
}

fun validateWithLambda(errField: MutableState<Boolean>, valFunc: () -> Boolean): Boolean {
    val res = valFunc()
//    print("validateWithLambda res: $res")
    errField.value = !res

    return res
}


