package com.krzysobo.sobositeapp

import android.os.Build
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


actual fun getPlatform(): Platform = AndroidPlatform()



class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}


actual fun getHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) {
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
//        config {
//            retryOnConnectionFailure(true)
//            connectTimeout(0, TimeUnit.SECONDS)
//        }
//    }
}

actual fun isNetworkConnectionAvailable(): Boolean {
    return true
}
