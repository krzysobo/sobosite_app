package com.krzysobo.sobositeapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
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
import apptpl.composeapp.generated.resources.Res
import apptpl.composeapp.generated.resources.confirm_your_registration
import apptpl.composeapp.generated.resources.registration_confirmation
import apptpl.composeapp.generated.resources.registration_confirmation_failure
import apptpl.composeapp.generated.resources.registration_confirmation_failure_desc
import apptpl.composeapp.generated.resources.registration_confirmed_ok
import apptpl.composeapp.generated.resources.registration_confirmed_ok_desc
import apptpl.composeapp.generated.resources.token
import apptpl.composeapp.generated.resources.token_required
import apptpl.composeapp.generated.resources.your_token
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.soboapptpl.service.anyResText
import com.krzysobo.soboapptpl.widgets.ErrorMessageBox
import com.krzysobo.soboapptpl.widgets.LoginWidget
import com.krzysobo.soboapptpl.widgets.MessageBox
import com.krzysobo.soboapptpl.widgets.PageHeader
import com.krzysobo.soboapptpl.widgets.TextFieldWithErrorsKeyboardSettings
import com.krzysobo.sobositeapp.viewmodel.RegisterConfirmPageVM
import com.krzysobo.sobositeapp.viewmodel.getRegisterConfirmPageVM
import kotlinx.coroutines.launch


@Composable
fun PageSobositeRegisterConfirm() {
    val vm: RegisterConfirmPageVM = getRegisterConfirmPageVM()
    val coroutineScope = rememberCoroutineScope()

    vm.login = remember { mutableStateOf("") }
    vm.token = remember { mutableStateOf("") }
    vm.isErrorLogin = remember { mutableStateOf(false) }
    vm.isErrorToken = remember { mutableStateOf(false) }
    vm.isApiError = remember { mutableStateOf(false) }
    vm.isConfirmationError = remember { mutableStateOf(false) }
    vm.isFormSent = remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    LazyColumn(
        modifier = Modifier.padding(all = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            PageHeader(anyResText(AnyRes(Res.string.registration_confirmation)))
        }

        item {

            if (vm.isFormSent.value) {
                MessageBox(
                    "* ${anyResText(AnyRes(Res.string.registration_confirmed_ok))} *",
                    anyResText(AnyRes(Res.string.registration_confirmed_ok_desc))
                )
            } else {
                if (vm.isApiError.value) {
                    ErrorMessageBox(
                        "* ${anyResText(AnyRes(Res.string.registration_confirmation_failure))} *",
                        anyResText(AnyRes(Res.string.registration_confirmation_failure_desc))
                    )
                }

                val leadingIcon = @Composable {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "",
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

                /**
                 * token
                 */
                TextFieldWithErrorsKeyboardSettings(
                    value = vm.token.value,
                    onValueChanges = { data: String ->
                        vm.token.value = data
                        vm.clearTokenError()
                    },
                    modifier = Modifier.padding(all = 10.dp).fillMaxWidth(),
                    labelText = anyResText(AnyRes(Res.string.token)),
                    placeHolderText = anyResText(AnyRes(Res.string.your_token)),
                    leadingIcon = leadingIcon,
                    isError = vm.isErrorToken.value,
                    errorText = anyResText(AnyRes(Res.string.token_required)),
                    focusManager = focusManager
                )

                Button(
                    onClick = {
                        vm.clearErrors()
                        val res: Boolean = vm.validate()
                        if (res) {
                            coroutineScope.launch {
                                vm.doRegisterConfirm()
                            }
                        }
                    },
                    modifier = Modifier.padding(all = 10.dp)
                ) { Text(anyResText(AnyRes(Res.string.confirm_your_registration))) }
            }
        }
    }
}
