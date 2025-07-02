package com.krzysobo.sobositeapp.view

import WaitingSpinner
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
import com.krzysobo.soboapptpl.pubres.PubRes
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.soboapptpl.service.anyResText
import com.krzysobo.soboapptpl.widgets.ErrorMessageBox
import com.krzysobo.soboapptpl.widgets.ErrorText
import com.krzysobo.soboapptpl.widgets.LoginWidget
import com.krzysobo.soboapptpl.widgets.MessageBox
import com.krzysobo.soboapptpl.widgets.PageHeader
import com.krzysobo.soboapptpl.widgets.PasswordWidget
import com.krzysobo.soboapptpl.widgets.TextFieldWithErrorsKeyboardSettings
import com.krzysobo.sobositeapp.appres.AppRes
import com.krzysobo.sobositeapp.viewmodel.RegisterPageVM
import com.krzysobo.sobositeapp.viewmodel.getRegisterPageVM
import kotlinx.coroutines.launch

@Composable
fun PageSobositeRegister() {
    val vm: RegisterPageVM = getRegisterPageVM()
    val coroutineScope = rememberCoroutineScope()

    vm.login = remember { mutableStateOf("") }
    vm.pass = remember { mutableStateOf("") }
    vm.passConfirm = remember { mutableStateOf("") }
    vm.firstName = remember { mutableStateOf("") }
    vm.lastName = remember { mutableStateOf("") }

    vm.isErrorLogin = remember { mutableStateOf(false) }
    vm.isErrorPass = remember { mutableStateOf(false) }
    vm.isErrorPassConfirm = remember { mutableStateOf(false) }
    vm.isErrorPassDontMatch = remember { mutableStateOf(false) }
    vm.isErrorFirstName = remember { mutableStateOf(false) }
    vm.isErrorLastName = remember { mutableStateOf(false) }
    vm.passVisible = remember { mutableStateOf(false) }

    // refreshing element
    vm.isFormSent = remember { mutableStateOf(false) }

    var showColumn = remember { mutableStateOf(true) }
    if (showColumn.value) {

        LazyColumn(
            modifier = Modifier.padding(all = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                PageHeader(anyResText(AnyRes(AppRes.string.user_registration)))
            }

            item {

                if (vm.isFormSent.value) {
                    MessageBox(
                        "* ${anyResText(AnyRes(AppRes.string.registration_ok))} *",
                        anyResText(AnyRes(AppRes.string.registration_ok_desc))
                    )
                } else {
                    if (vm.isApiError.value) {
                        ErrorMessageBox(
                            "* ${anyResText(AnyRes(AppRes.string.error_registration_failure))} *",
                            vm.apiErrorDetails.value
                        )
                    }

                    val focusManager = LocalFocusManager.current

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
                     * first name
                     */
                    TextFieldWithErrorsKeyboardSettings(
                        value = vm.firstName.value,
                        onValueChanges = { data: String ->
                            vm.firstName.value = data
                            vm.clearFirstNameError()
                        },
                        modifier = Modifier.padding(all = 10.dp).fillMaxWidth(),
                        labelText = anyResText(AnyRes(PubRes.string.first_name)),
                        placeHolderText = anyResText(AnyRes(PubRes.string.your_first_name)),
                        leadingIcon = leadingIcon,
                        isError = vm.isErrorFirstName.value,
                        errorText = anyResText(AnyRes(PubRes.string.first_name_required)),
                        focusManager = focusManager
                    )

                    /**
                     * last name
                     */
                    TextFieldWithErrorsKeyboardSettings(
                        value = vm.lastName.value,
                        onValueChanges = { data: String ->
                            vm.lastName.value = data
                            vm.clearLastNameError()
                        },
                        modifier = Modifier.padding(all = 10.dp).fillMaxWidth(),
                        labelText = anyResText(AnyRes(PubRes.string.last_name)),
                        placeHolderText = anyResText(AnyRes(PubRes.string.your_last_name)),
                        leadingIcon = leadingIcon,
                        isError = vm.isErrorLastName.value,
                        errorText = anyResText(AnyRes(PubRes.string.last_name_required)),
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
                        labelText = anyResText(AnyRes(PubRes.string.password_confirmation)),
                        placeHolderText = anyResText(AnyRes(PubRes.string.your_password_confirmation)),
                        errorText = anyResText(AnyRes(PubRes.string.password_confirmation_required)),
                    )

                    if (vm.isErrorPassDontMatch.value) {
                        ErrorText(anyResText(AnyRes(PubRes.string.error_passwords_dont_match)))
                    }

                    Button(
                        onClick = {
                            vm.clearErrors()
                            val resForm: Boolean = vm.validate()
                            if (resForm) {
                                coroutineScope.launch {
                                    showColumn.value = false
                                    vm.doRegister()
                                    showColumn.value = true
                                }
                            }
                        },
                        modifier = Modifier.padding(all = 10.dp)
                    ) { Text(anyResText(AnyRes(AppRes.string.register))) }
                }
            }


        }
    } else {
        WaitingSpinner()
    }

}

