package com.krzysobo.sobositeapp

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun getHttpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient

expect fun isNetworkConnectionAvailable(): Boolean
