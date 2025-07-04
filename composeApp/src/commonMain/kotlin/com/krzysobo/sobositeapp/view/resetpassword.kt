package com.krzysobo.sobositeapp.view

import WaitingSpinner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.soboapptpl.service.anyResText
import com.krzysobo.soboapptpl.widgets.LoginWidget
import com.krzysobo.soboapptpl.widgets.MessageBox
import com.krzysobo.soboapptpl.widgets.PageHeader
import com.krzysobo.sobositeapp.appres.AppRes
import com.krzysobo.sobositeapp.viewmodel.ResetPasswordPageVM
import com.krzysobo.sobositeapp.viewmodel.getResetPasswordPageVM
import kotlinx.coroutines.launch

@Composable
fun PageSobositeResetPassword() {
    val vm: ResetPasswordPageVM = getResetPasswordPageVM()
    val coroutineScope = rememberCoroutineScope()

    vm.login = remember { mutableStateOf("") }
    vm.isErrorLogin = remember { mutableStateOf(false) }
    vm.isFormSent = remember { mutableStateOf(false) }
    vm.isApiError = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    var showColumn = remember { mutableStateOf(true) }
    if (showColumn.value) {

        LazyColumn(
            modifier = Modifier.padding(all = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                PageHeader(anyResText(AnyRes(AppRes.string.reset_your_password)))
            }

            val leadingIcon = @Composable {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "",
                )
            }

            item {

                if (vm.isFormSent.value) {
                    MessageBox(
                        "* ${anyResText(AnyRes(AppRes.string.password_reset))} *",
                        anyResText(AnyRes(AppRes.string.password_reset_desc_s, vm.login.value))
                    )
                } else {
                    if (vm.isApiError.value) {
                        MessageBox(
                            "* ${anyResText(AnyRes(AppRes.string.error_network_error))} *",
                            anyResText(AnyRes(AppRes.string.error_network_error_desc))
                        )
                    }
                    /**
                     * username  https://developer.android.com/develop/ui/compose/text/user-input
                     */

                    LoginWidget(
                        value = vm.login.value,
                        onValueChanges = { data: String ->
                            vm.login.value = data
                            vm.clearLoginError()
                        },
                        isError = vm.isErrorLogin.value,
                    )

                    Button(
                        onClick = {
                            vm.clearErrors()
                            val res: Boolean = vm.validate()
                            if (res) {
                                coroutineScope.launch {
                                    showColumn.value = false
                                    vm.doResetPassword()
                                    showColumn.value = true
                                }
                            }
                        },
                        modifier = Modifier.padding(all = 10.dp)
                    ) { Text(anyResText(AnyRes(AppRes.string.reset_your_password))) }
                }
            }
        }
    } else {
        WaitingSpinner()
    }
}
