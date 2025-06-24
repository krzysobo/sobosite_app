package com.krzysobo.sobositeapp.settings
import com.russhwolf.settings.Settings

object AppSettings {
    private val settings: Settings = Settings()

    var apiHostUrl: String
//        get() = settings.getString("api_host_url", "http://192.168.1.177:9000/")
        get() = settings.getString("api_host_url", "http://sobosite.krzysztofsobolewski.info:9000/")
        set(value) = settings.putString("api_host_url", value)

    var apiPrefix: String
        get() = settings.getString("api_prefix", "/api/v1/")
        set(value) = settings.putString("api_prefix", value)

    var useSystemLang: Boolean
        get() = settings.getBoolean("use_system_lang", true)
        set(value) = settings.putBoolean("use_system_lang", value)

    var lang: String
        get() = settings.getString("lang", "en")
        set(value) = settings.putString("lang", value)
}