package com.krzysobo.sobositeapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import sobositeapp.composeapp.generated.resources.Res
import sobositeapp.composeapp.generated.resources.close_password_edition
import sobositeapp.composeapp.generated.resources.error_profile_updating_error
import sobositeapp.composeapp.generated.resources.open_password_edition
import sobositeapp.composeapp.generated.resources.profile_updated
import sobositeapp.composeapp.generated.resources.profile_updated_ok
import sobositeapp.composeapp.generated.resources.update_profile
import com.krzysobo.soboapptpl.pubres.PubRes
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.soboapptpl.service.anyResText
import com.krzysobo.soboapptpl.widgets.ErrorMessageBox
import com.krzysobo.soboapptpl.widgets.ErrorText
import com.krzysobo.soboapptpl.widgets.LoginWidget
import com.krzysobo.soboapptpl.widgets.MessageBox
import com.krzysobo.soboapptpl.widgets.PasswordWidget
import com.krzysobo.soboapptpl.widgets.TextFieldWithErrorsKeyboardSettings
import com.krzysobo.sobositeapp.viewmodel.ProfilePageVM
import com.krzysobo.sobositeapp.viewmodel.getProfilePageVM
import com.krzysobo.sobositeapp.viewmodel.getUserStateVM
import kotlinx.coroutines.launch

@Composable
fun PageSobositeProfile() {
    val vm: ProfilePageVM = getProfilePageVM()
    val coroutineScope = rememberCoroutineScope()

    vm.login = remember { mutableStateOf("") }
    vm.newPassword = remember { mutableStateOf("") }
    vm.lastName = remember { mutableStateOf("") }

    vm.newPasswordConfirm = remember { mutableStateOf("") }
    vm.oldPassword = remember { mutableStateOf("") }
    vm.firstName = remember { mutableStateOf("") }

    vm.isErrorLogin = remember { mutableStateOf(false) }
    vm.isErrorNewPassword = remember { mutableStateOf(false) }
    vm.isErrorNewPasswordConfirm = remember { mutableStateOf(false) }
    vm.isErrorOldPassword = remember { mutableStateOf(false) }
    vm.isErrorNewPasswordsDontMatch = remember { mutableStateOf(false) }
    vm.isErrorFirstName = remember { mutableStateOf(false) }
    vm.isErrorLastName = remember { mutableStateOf(false) }
    vm.passVisible = remember { mutableStateOf(false) }
    vm.isPassChangeOpen = remember { mutableStateOf(false) }

    // refreshing element
    vm.isFormSent = remember { mutableStateOf(false) }
    vm.isProfileUpToDate = remember { mutableStateOf(false) }

    if (!vm.isProfileUpToDate.value) {
        vm.doRefreshProfileFromUserState()
        vm.isProfileUpToDate.value = true
    }

    val focusManager = LocalFocusManager.current

    LazyColumn(
        modifier = Modifier.padding(all = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            if (vm.isFormSent.value) {
                MessageBox(
                    "* ${anyResText(AnyRes(Res.string.profile_updated))} *",
                    anyResText(AnyRes(Res.string.profile_updated_ok))
                )
            } else if (vm.isApiError.value) {
                ErrorMessageBox(
                    "* ${anyResText(AnyRes(Res.string.error_profile_updating_error))} *",
                    vm.apiErrorDetails.value
                )
            }
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

        item {
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

            // ============ passwords block =============
            if (vm.isPassChangeOpen.value) {
                Button(
                    onClick = {
                        vm.isPassChangeOpen.value = false
                    },
                    modifier = Modifier.padding(all = 10.dp)
                ) { Text(anyResText(AnyRes(Res.string.close_password_edition))) }

                /**
                 * Password
                 */
                PasswordWidget(
                    value = vm.newPassword.value,
                    onValueChanges = { data: String ->
                        vm.newPassword.value = data
                        vm.clearNewPasswordError()
                        vm.checkPassMatch()
                    },
                    isError = vm.isErrorNewPassword.value,
                    trailingIconPassOnClick = { vm.togglePassVisible() },
                    isPassVisible = vm.isPassVisible,
                    labelText = anyResText(AnyRes(PubRes.string.new_password)),
                    placeHolderText = anyResText(AnyRes(PubRes.string.your_new_password)),
                    errorText = anyResText(AnyRes(PubRes.string.new_password_required)),

                    )

                PasswordWidget(
                    value = vm.newPasswordConfirm.value,
                    onValueChanges = { data: String ->
                        vm.newPasswordConfirm.value = data
                        vm.clearNewPasswordConfirmError()
                        vm.checkPassMatch()
                    },
                    isError = vm.isErrorNewPasswordConfirm.value,
                    trailingIconPassOnClick = { vm.togglePassVisible() },
                    isPassVisible = vm.isPassVisible,
                    labelText = anyResText(AnyRes(PubRes.string.new_password_confirmation)),
                    placeHolderText = anyResText(AnyRes(PubRes.string.your_new_password_confirmation)),
                    errorText = anyResText(AnyRes(PubRes.string.new_password_confirmation_required)),
                )

                if (vm.isErrorNewPasswordsDontMatch.value) {
                    ErrorText(anyResText(AnyRes(PubRes.string.error_passwords_dont_match)))
                }

                PasswordWidget(
                    value = vm.oldPassword.value,
                    onValueChanges = { data: String ->
                        vm.oldPassword.value = data
                        vm.clearNewPasswordConfirmError()
                        vm.checkPassMatch()
                    },
                    isError = vm.isErrorOldPassword.value,
                    trailingIconPassOnClick = { vm.togglePassVisible() },
                    isPassVisible = vm.isPassVisible,
                    labelText = anyResText(AnyRes(PubRes.string.current_password)),
                    placeHolderText = anyResText(AnyRes(PubRes.string.your_current_password)),
                    errorText = anyResText(AnyRes(PubRes.string.current_password_required)),
                )

            } else {
                Button(
                    onClick = {
                        vm.isPassChangeOpen.value = true
                    },
                    modifier = Modifier.padding(all = 10.dp)
                ) { Text(anyResText(AnyRes(Res.string.open_password_edition))) }

            }
            // =============================================================

            Button(
                onClick = {
                    vm.clearErrors()
                    val resForm: Boolean = vm.validate()
                    if (resForm) {
                        coroutineScope.launch {
                            val res = vm.doUpdateProfile()
                            if (res) {
                                getUserStateVM().doRefreshProfileFromApi()
                                vm.doRefreshProfileFromUserState()
                            }
                        }
                    }
                },
                modifier = Modifier.padding(all = 10.dp)
            ) { Text(anyResText(AnyRes(Res.string.update_profile))) }
        }
    }
}
