package com.krzysobo.ktsob.sobosite.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
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
import apptpl.composeapp.generated.resources.confirm_password_reset
import apptpl.composeapp.generated.resources.confirm_password_reset_long
import apptpl.composeapp.generated.resources.error_password_reset_confirmation_error
import apptpl.composeapp.generated.resources.error_password_reset_confirmation_error_desc
import apptpl.composeapp.generated.resources.error_passwords_dont_match
import apptpl.composeapp.generated.resources.password_confirmation
import apptpl.composeapp.generated.resources.password_confirmation_required
import apptpl.composeapp.generated.resources.password_reset_ok
import apptpl.composeapp.generated.resources.password_reset_ok_desc
import apptpl.composeapp.generated.resources.token
import apptpl.composeapp.generated.resources.token_required
import apptpl.composeapp.generated.resources.your_password_confirmation
import apptpl.composeapp.generated.resources.your_token
import com.krzysobo.ktsob.apptpl.service.AnyRes
import com.krzysobo.ktsob.apptpl.service.anyResText
import com.krzysobo.ktsob.apptpl.widgets.ErrorMessageBox
import com.krzysobo.ktsob.apptpl.widgets.ErrorText
import com.krzysobo.ktsob.sobosite.widgets.LoginWidget
import com.krzysobo.ktsob.apptpl.widgets.MessageBox
import com.krzysobo.ktsob.apptpl.widgets.PageHeader
import com.krzysobo.ktsob.sobosite.widgets.PasswordWidget
import com.krzysobo.ktsob.apptpl.widgets.TextFieldWithErrorsKeyboardSettings
import com.krzysobo.ktsob.sobosite.viewmodel.getResetPasswordConfirmPageVM
import com.krzysobo.ktsob.sobosite.viewmodel.ResetPasswordConfirmPageVM
import kotlinx.coroutines.launch

@Composable
fun PageSobositeResetPasswordConfirm() {
    val vm: ResetPasswordConfirmPageVM = getResetPasswordConfirmPageVM()
    val coroutineScope = rememberCoroutineScope()

    vm.login = remember { mutableStateOf("") }
    vm.token = remember { mutableStateOf("") }
    vm.pass = remember { mutableStateOf("") }
    vm.passConfirm = remember { mutableStateOf("") }
    vm.isErrorLogin = remember { mutableStateOf(false) }
    vm.isErrorToken = remember { mutableStateOf(false) }
    vm.isErrorPass = remember { mutableStateOf(false) }
    vm.isErrorPassConfirm = remember { mutableStateOf(false) }
    vm.isErrorPassDontMatch = remember { mutableStateOf(false) }
    vm.passVisible = remember { mutableStateOf(false) }
    vm.isFormSent = remember { mutableStateOf(false) }
    vm.isApiError = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(all = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PageHeader(anyResText(AnyRes(Res.string.confirm_password_reset)))

        val focusManager = LocalFocusManager.current

        if (vm.isFormSent.value) {
            MessageBox(
                "* ${anyResText(AnyRes(Res.string.password_reset_ok))} *",
                anyResText(AnyRes(Res.string.password_reset_ok_desc)),
            )
        } else {
            if (vm.isApiError.value) {
                ErrorMessageBox(
                    "* ${anyResText(AnyRes(Res.string.error_password_reset_confirmation_error))} *",
                    anyResText(AnyRes(Res.string.error_password_reset_confirmation_error_desc))
                )
            }

            val leadingIcon = @Composable {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "",
                )
            }

            val leadingIconPass = @Composable {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "",
                )
            }

            val trailingIconPass = @Composable {
                IconButton(
                    onClick = {
                        vm.togglePassVisible()
                    }
                ) {
                    Icon(
                        if (vm.isPassVisible) Icons.Default.KeyboardArrowDown else
                            Icons.Default.KeyboardArrowUp,
                        contentDescription = ""
                    )
                }
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


            /**
             * Password
             */
            PasswordWidget(
                value = vm.pass.value,
                onValueChanges = { data: String ->
                    vm.pass.value = data
                    vm.clearPassError()
                    vm.checkPassMatch()
                },
                isError = vm.isErrorPass.value,
                trailingIconPassOnClick = { vm.togglePassVisible() },
                isPassVisible = vm.isPassVisible,
            )

            PasswordWidget(
                value = vm.passConfirm.value,
                onValueChanges = { data: String ->
                    vm.passConfirm.value = data
                    vm.clearPassConfirmError()
                    vm.checkPassMatch()
                },
                isError = vm.isErrorPassConfirm.value,
                trailingIconPassOnClick = { vm.togglePassVisible() },
                isPassVisible = vm.isPassVisible,
                labelText = anyResText(AnyRes(Res.string.password_confirmation)),
                placeHolderText = anyResText(AnyRes(Res.string.your_password_confirmation)),
                errorText = anyResText(AnyRes(Res.string.password_confirmation_required)),
            )

            if (vm.isErrorPassDontMatch.value) {
                ErrorText(anyResText(AnyRes(Res.string.error_passwords_dont_match)))
            }


            Button(
                onClick = {
                    vm.clearErrors()
                    val res: Boolean = vm.validate()
                    if (res) {
                        coroutineScope.launch {
                            vm.doConfirmPasswordReset()
                        }
                    }
                },
                modifier = Modifier.padding(all = 10.dp)
            ) { Text(anyResText(AnyRes(Res.string.confirm_password_reset_long))) }

        }

    }


}
