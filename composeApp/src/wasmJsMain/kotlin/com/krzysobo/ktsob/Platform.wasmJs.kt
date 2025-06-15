package com.krzysobo.ktsob

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()
//actual fun getOkHttp(): Any {
////    return OkHttpClient()
//    return {}
//}

actual fun getHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Js) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
//    install(Auth) {
//        bearer {
//            sendWithoutRequest { true }
//        }
//    }

}

actual fun isNetworkConnectionAvailable(): Boolean {
    return true
}
