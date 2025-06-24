package com.krzysobo.sobositeapp

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}


actual fun getPlatform(): Platform = JVMPlatform()

//actual fun getHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
actual fun getHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(CIO) {
    config(this)
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

//    engine {
////        config {
////            retryOnConnectionFailure(true)
////            connectTimeout(0, TimeUnit.SECONDS)
////        }
//    }
}

actual fun isNetworkConnectionAvailable(): Boolean {
    return true
}
