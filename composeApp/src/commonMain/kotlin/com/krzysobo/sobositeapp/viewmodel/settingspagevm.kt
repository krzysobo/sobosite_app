package com.krzysobo.sobositeapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.intl.Locale
import com.krzysobo.soboapptpl.viewmodel.LangSettingsVM
import com.krzysobo.soboapptpl.viewmodel.SoboViewModel
import com.krzysobo.soboapptpl.widgets.strNotEmpty
import com.krzysobo.soboapptpl.widgets.validateWithLambda
import com.krzysobo.sobositeapp.settings.AppSettings
import java.lang.module.Configuration
import javax.naming.Context


class SettingsPageVM : SoboViewModel(
) {
    val langSettings = LangSettingsVM()

    var apiHostUrl: MutableState<String> = mutableStateOf("")
    var apiPrefix: MutableState<String> = mutableStateOf("")

    var isSettingsUpToDate: MutableState<Boolean> = mutableStateOf(false)

    var isErrorApiHostUrl: MutableState<Boolean> = mutableStateOf(false)
    var isErrorApiPrefix: MutableState<Boolean> = mutableStateOf(false)


    fun doRefreshSettingsFromAppSettings() {
        langSettings.doRefreshSettingsFromAppSettings()

        apiHostUrl.value = AppSettings.apiHostUrl
        apiPrefix.value = AppSettings.apiPrefix
    }


    fun doUpdateAppSettings(): Boolean {
//        println("\n\n======================= UPDATE SETTINGS ===============\n\n")
        val res = langSettings.doUpdateAppSettings()
        isFormSent.value = langSettings.isFormSent.value
        isApiError.value = langSettings.isApiError.value
        apiErrorDetails.value = langSettings.apiErrorDetails.value

        if ((!res) || (!isFormSent.value) || (isApiError.value)) {
            return false
        }

        try {
            AppSettings.apiHostUrl = apiHostUrl.value
            AppSettings.apiPrefix = apiPrefix.value
            isFormSent.value = true

            return true
        } catch (e: Exception) {
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        }

        return false
    }

    fun validate(): Boolean {
        clearErrors()

        val resLang = langSettings.validate()
        val resApiHostUrl = validateWithLambda(isErrorApiHostUrl, { strNotEmpty(apiHostUrl.value) })
        val resApiPrefix = validateWithLambda(isErrorApiPrefix, { strNotEmpty(apiPrefix.value) })
        var res = resLang && resApiHostUrl && resApiPrefix

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
