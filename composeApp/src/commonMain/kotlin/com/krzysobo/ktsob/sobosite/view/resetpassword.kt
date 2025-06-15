package com.krzysobo.ktsob.sobosite.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import apptpl.composeapp.generated.resources.Res
import apptpl.composeapp.generated.resources.error_network_error
import apptpl.composeapp.generated.resources.error_network_error_desc
import apptpl.composeapp.generated.resources.error_network_error_s
import apptpl.composeapp.generated.resources.password_reset
import apptpl.composeapp.generated.resources.registration_confirmed_ok
import apptpl.composeapp.generated.resources.reset_your_password
import com.krzysobo.ktsob.apptpl.service.AnyRes
import com.krzysobo.ktsob.apptpl.service.anyResText
import com.krzysobo.ktsob.sobosite.widgets.LoginWidget
import com.krzysobo.ktsob.apptpl.widgets.MessageBox
import com.krzysobo.ktsob.apptpl.widgets.PageHeader
import com.krzysobo.ktsob.sobosite.viewmodel.getResetPasswordPageVM
import com.krzysobo.ktsob.sobosite.viewmodel.ResetPasswordPageVM
import kotlinx.coroutines.launch

@Composable
fun PageSobositeResetPassword() {
    val vm: ResetPasswordPageVM = getResetPasswordPageVM()
    val coroutineScope = rememberCoroutineScope()

    vm.login = remember { mutableStateOf("") }
    vm.isErrorLogin = remember { mutableStateOf(false) }
    vm.isFormSent = remember { mutableStateOf(false) }
    vm.isApiError = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(all = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PageHeader(anyResText(AnyRes(Res.string.reset_your_password)))
        val focusManager = LocalFocusManager.current

        val leadingIcon = @Composable {
            Icon(
                Icons.Default.Person,
                contentDescription = "",
            )
        }

        if (vm.isFormSent.value) {
            MessageBox(
                "* ${anyResText(AnyRes(Res.string.password_reset))} *",
                anyResText(AnyRes(Res.string.password_reset, vm.login.value))
            )
        } else {
            if (vm.isApiError.value) {
                MessageBox(
            "* ${anyResText(AnyRes(Res.string.error_network_error))} *",
                    anyResText(AnyRes(Res.string.error_network_error_desc))
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
                            vm.doResetPassword()
                        }
                    }
                },
                modifier = Modifier.padding(all = 10.dp)
            ) { Text(anyResText(AnyRes(Res.string.reset_your_password))) }
        }
    }
}
