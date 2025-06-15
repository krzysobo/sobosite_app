package com.krzysobo.ktsob

//import apptpl.composeapp.generated.resources.Res
//import apptpl.composeapp.generated.resources.app_name
//import apptpl.composeapp.generated.resources.hello_w

//import org.jetbrains.compose.resources.stringResource


class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}