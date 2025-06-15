package com.krzysobo.ktsob.sobosite.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.krzysobo.ktsob.sobosite.settings.AppSettings
import com.krzysobo.ktsob.apptpl.widgets.strNotEmpty
import com.krzysobo.ktsob.apptpl.widgets.validateWithLambda


class SettingsPageVM : SoboViewModel(
) {
    var apiHostUrl: MutableState<String> = mutableStateOf("")
    var apiPrefix: MutableState<String> = mutableStateOf("")

    var isSettingsUpToDate: MutableState<Boolean> = mutableStateOf(false)

    var isErrorApiHostUrl: MutableState<Boolean> = mutableStateOf(false)
    var isErrorApiPrefix: MutableState<Boolean> = mutableStateOf(false)


    fun doRefreshSettingsFromAppSettings() {
        apiHostUrl.value = AppSettings.apiHostUrl
        apiPrefix.value = AppSettings.apiPrefix
    }

    fun doUpdateAppSettings(): Boolean {
//        println("\n\n======================= UPDATE SETTINGS ===============\n\n")

        try {
            AppSettings.apiHostUrl = apiHostUrl.value
            AppSettings.apiPrefix = apiPrefix.value
            isFormSent.value = true

            return true
        } catch(e: Exception) {
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        }

        return false
    }

    fun validate(): Boolean {
        clearErrors()

        val resApiHostUrl = validateWithLambda(isErrorApiHostUrl, { strNotEmpty(apiHostUrl.value) })
        val resApiPrefix = validateWithLambda(isErrorApiPrefix, { strNotEmpty(apiPrefix.value) })
        var res = resApiHostUrl && resApiPrefix

        return res
    }

    fun clearApiHostUrlError() {
        clearErrorFlag(isErrorApiHostUrl)
    }

    fun clearApiPrefixError() {
        clearErrorFlag(isErrorApiPrefix)
    }

    fun clearErrors() {
        clearApiHostUrlError()
        clearApiPrefixError()

        clearApiError()
        clearFormSent()
    }
}
