package com.krzysobo.sobositeapp.view

import WaitingSpinner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Web
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonSkippableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.krzysobo.soboapptpl.pubres.PubRes
import sobositeapp.composeapp.generated.resources.Res
import sobositeapp.composeapp.generated.resources.api_host_url
import sobositeapp.composeapp.generated.resources.api_host_url_desc
import sobositeapp.composeapp.generated.resources.api_host_url_required
import sobositeapp.composeapp.generated.resources.api_prefix
import sobositeapp.composeapp.generated.resources.api_prefix_desc
import sobositeapp.composeapp.generated.resources.api_prefix_required
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.soboapptpl.service.SoboRouter
import com.krzysobo.soboapptpl.service.anyResText
import com.krzysobo.soboapptpl.viewmodel.AppViewModelVM
import com.krzysobo.soboapptpl.widgets.ErrorMessageBox
import com.krzysobo.soboapptpl.widgets.MessageBox
import com.krzysobo.soboapptpl.widgets.SettingSelectLanguage
import com.krzysobo.soboapptpl.widgets.SettingUseSystemLang
import com.krzysobo.soboapptpl.widgets.TextFieldWithErrorsKeyboardSettings
import com.krzysobo.soboapptpl.widgets.menus.DrawerData
import com.krzysobo.sobositeapp.settings.langListSoboSite
import com.krzysobo.sobositeapp.viewmodel.SettingsPageVM
import com.krzysobo.sobositeapp.viewmodel.getSettingsPageVM
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

//@NonSkippableComposable
@Composable
fun PageSobositeSettings() {
    val vm: SettingsPageVM = getSettingsPageVM()
    val coroutineScope = rememberCoroutineScope()


    vm.langSettings.lang = remember { mutableStateOf("") }
    vm.langSettings.useSystemLang = remember { mutableStateOf(false) }
    vm.langSettings.isErrorLang = remember { mutableStateOf(false) }

    vm.apiHostUrl = remember { mutableStateOf("") }
    vm.apiPrefix = remember { mutableStateOf("") }

    vm.isErrorApiHostUrl = remember { mutableStateOf(false) }
    vm.isErrorApiPrefix = remember { mutableStateOf(false) }

    vm.isFormSent = remember { mutableStateOf(false) }
    vm.isSettingsUpToDate = remember { mutableStateOf(false) }

    if (!vm.isSettingsUpToDate.value) {
        vm.doRefreshSettingsFromAppSettings()
        vm.isSettingsUpToDate.value = true
    }

    var showColumn = remember { mutableStateOf(true) }
    if (showColumn.value) {

        val focusManager = LocalFocusManager.current

        LazyColumn(
            modifier = Modifier.padding(all = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (vm.isFormSent.value) {
                item {
                    MessageBox(
                        "* ${anyResText(AnyRes(PubRes.string.settings_updated_ok))} *",
                        anyResText(AnyRes(PubRes.string.settings_updated_ok_desc))
                    )
                }
            } else if (vm.isApiError.value) {
                item {
                    ErrorMessageBox(
                        "* ${anyResText(AnyRes(PubRes.string.settings_updating_error))} *",
                        vm.apiErrorDetails.value
                    )
                }
            }

            val leadingIcon = @Composable {
                Icon(
                    Icons.Default.Web,
                    contentDescription = "",
                )
            }

            item {

                /**
                 * apiHostUrl
                 */
                TextFieldWithErrorsKeyboardSettings(
                    value = vm.apiHostUrl.value,
                    onValueChanges = { data: String ->
                        vm.apiHostUrl.value = data
                        vm.clearApiHostUrlError()
                    },
                    modifier = Modifier.padding(all = 10.dp).fillMaxWidth(),
                    labelText = anyResText(AnyRes(Res.string.api_host_url)),
                    placeHolderText = anyResText(AnyRes(Res.string.api_host_url_desc)),
                    leadingIcon = leadingIcon,
                    isError = vm.isErrorApiHostUrl.value,
                    errorText = anyResText(AnyRes(Res.string.api_host_url_required)),
                    focusManager = focusManager
                )
            }

            item {

                /**
                 * apiPrefix
                 */
                TextFieldWithErrorsKeyboardSettings(
                    value = vm.apiPrefix.value,
                    onValueChanges = { data: String ->
                        vm.apiPrefix.value = data
                        vm.clearApiHostUrlError()
                    },
                    modifier = Modifier.padding(all = 10.dp).fillMaxWidth(),
                    labelText = anyResText(AnyRes(Res.string.api_prefix)),
                    placeHolderText = anyResText(AnyRes(Res.string.api_prefix_desc)),
                    leadingIcon = leadingIcon,
                    isError = vm.isErrorApiPrefix.value,
                    errorText = anyResText(AnyRes(Res.string.api_prefix_required)),
                    focusManager = focusManager
                )
            }

            item {

                SettingUseSystemLang(vm.langSettings.useSystemLang)
                if (!vm.langSettings.useSystemLang.value) {
                    SettingSelectLanguage(langListSoboSite, vm.langSettings.lang)
                }

                Button(
                    onClick = {
                        vm.clearErrors()
                        val resForm: Boolean = vm.validate()
                        if (resForm) {
                            coroutineScope.launch {
                                val res = vm.doUpdateAppSettings()
                                if (res) {
                                    vm.doRefreshSettingsFromAppSettings()
                                    coroutineScope.launch {
                                        showColumn.value = false
//                                        DrawerData.selectedItemId.value = "about"
                                        AppViewModelVM.hideMenu()
                                        delay(700)
                                        showColumn.value = true
                                        delay(100)
                                        AppViewModelVM.showMenu()
//                                    SoboRouter.navigateToRouteHandle("settings")
                                    }

                                }
                            }
                        }
                    },
                    modifier = Modifier.padding(all = 10.dp)
                ) { Text(anyResText(AnyRes(PubRes.string.update_settings))) }
            }


        }
    } else {
        WaitingSpinner()
    }
}
