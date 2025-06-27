package com.krzysobo.sobositeapp.view

import WaitingSpinner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
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
import sobositeapp.composeapp.generated.resources.Res
import sobositeapp.composeapp.generated.resources.close_password_edition
import sobositeapp.composeapp.generated.resources.open_password_edition
import sobositeapp.composeapp.generated.resources.save_user_data
import sobositeapp.composeapp.generated.resources.user_is_active_desc
import sobositeapp.composeapp.generated.resources.user_is_staff_desc
import sobositeapp.composeapp.generated.resources.user_saved_ok
import sobositeapp.composeapp.generated.resources.user_saved_ok_desc
import sobositeapp.composeapp.generated.resources.user_saving_error
import com.krzysobo.soboapptpl.pubres.PubRes
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.soboapptpl.service.anyResText
import com.krzysobo.soboapptpl.widgets.ErrorMessageBox
import com.krzysobo.soboapptpl.widgets.ErrorText
import com.krzysobo.soboapptpl.widgets.LoginWidget
import com.krzysobo.soboapptpl.widgets.MessageBox
import com.krzysobo.soboapptpl.widgets.PasswordWidget
import com.krzysobo.soboapptpl.widgets.TextFieldWithErrorsKeyboardSettings
import com.krzysobo.sobositeapp.viewmodel.admin.AdminEditUserPageVM
import com.krzysobo.sobositeapp.viewmodel.getAdminEditUserPageVM
import kotlinx.coroutines.launch

@Composable
fun PageSobositeAdminEditUser(editedUserId: String = "") {
    val vm: AdminEditUserPageVM = getAdminEditUserPageVM()
    val coroutineScope = rememberCoroutineScope()

    vm.userId = remember { mutableStateOf(editedUserId) }
    vm.login = remember { mutableStateOf("") }
    vm.pass = remember { mutableStateOf("") }
    vm.lastName = remember { mutableStateOf("") }

    vm.isStaff = remember { mutableStateOf(false) }
    vm.isActive = remember { mutableStateOf(false) }

    vm.passConfirm = remember { mutableStateOf("") }
    vm.firstName = remember { mutableStateOf("") }

    vm.isErrorLogin = remember { mutableStateOf(false) }
    vm.isErrorPass = remember { mutableStateOf(false) }
    vm.isErrorPassConfirm = remember { mutableStateOf(false) }
    vm.isErrorPassDontMatch = remember { mutableStateOf(false) }
    vm.isErrorFirstName = remember { mutableStateOf(false) }
    vm.isErrorLastName = remember { mutableStateOf(false) }
    vm.passVisible = remember { mutableStateOf(false) }
    vm.isPassChangeOpen = remember { mutableStateOf(false) }

    // refreshing element
    vm.isFormSent = remember { mutableStateOf(false) }

    vm.isEditedUserUpToDate = remember { mutableStateOf(false) }


    if ((editedUserId != "") && (!vm.isEditedUserUpToDate.value)) {
        coroutineScope.launch {
//            println("\n\n===== edited user with id $editedUserId IS NOT UP TO DATE - " +
//                    "refreshing... ${vm.isEditedUserUpToDate.value} ")
            vm.doRefreshUserFromApi()
            vm.isEditedUserUpToDate.value = true
        }
    }
    val focusManager = LocalFocusManager.current

    var showColumn = remember { mutableStateOf(true) }
    if (showColumn.value) {

        LazyColumn(
            modifier = Modifier.padding(all = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
//        Text("EDITED USER ID: $editedUserId IS NEW USER: ${vm.isNewUser()}")

            item {
                if (vm.isFormSent.value) {
                    MessageBox(
                        "* ${anyResText(AnyRes(Res.string.user_saved_ok))} *",
                        anyResText(AnyRes(Res.string.user_saved_ok_desc))
                    )
                } else if (vm.isApiError.value) {
                    ErrorMessageBox(
                        "* ${anyResText(AnyRes(Res.string.user_saving_error))} *",
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
                    placeHolderText = anyResText(AnyRes(PubRes.string.first_name)),
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
                    placeHolderText = anyResText(AnyRes(PubRes.string.last_name)),
                    leadingIcon = leadingIcon,
                    isError = vm.isErrorLastName.value,
                    errorText = anyResText(AnyRes(PubRes.string.last_name_required)),
                    focusManager = focusManager
                )
                Row {
                    Checkbox(
                        checked = vm.isActive.value,
                        onCheckedChange = {
                            vm.isActive.value = !vm.isActive.value
                        }
                    )

                    val isActiveStr = if (vm.isActive.value) {
                        anyResText(AnyRes(PubRes.string.yes))
                    } else {
                        anyResText(AnyRes(PubRes.string.no))
                    }
                    Text("${anyResText(AnyRes(Res.string.user_is_active_desc))} $isActiveStr")
                }
            }

            item {
                Row {
                    Checkbox(
                        checked = vm.isStaff.value,
                        onCheckedChange = {
                            vm.isStaff.value = !vm.isStaff.value
                        }
                    )
                    val isStaffStr = if (vm.isStaff.value) {
                        anyResText(AnyRes(PubRes.string.yes))
                    } else {
                        anyResText(AnyRes(PubRes.string.no))
                    }
                    Text("${anyResText(AnyRes(Res.string.user_is_staff_desc))} $isStaffStr")
                }


                // ============ passwords block =============
                if ((vm.isPassChangeOpen.value) || (vm.isNewUser())) {
                    if (!vm.isNewUser()) {
                        Button(
                            onClick = {
                                vm.isPassChangeOpen.value = false
                            },
                            modifier = Modifier.padding(all = 10.dp)
                        ) { Text(anyResText(AnyRes(Res.string.close_password_edition))) }
                    }

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
                        labelText = anyResText(AnyRes(PubRes.string.password)),
                        placeHolderText = anyResText(AnyRes(PubRes.string.password)),
                        errorText = anyResText(AnyRes(PubRes.string.password_required)),

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
                        placeHolderText = anyResText(AnyRes(PubRes.string.password_confirmation)),
                        errorText = anyResText(AnyRes(PubRes.string.password_confirmation_required)),
                    )

                    if (vm.isErrorPassDontMatch.value) {
                        ErrorText(anyResText(AnyRes(PubRes.string.error_passwords_dont_match)))
                    }

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
                                showColumn.value = false
                                vm.doAdminSaveUser()
                                showColumn.value = true
                            }
                        }
                    },
                    modifier = Modifier.padding(all = 10.dp)
                ) { Text(anyResText(AnyRes(Res.string.save_user_data))) }
            }
        }

    } else {
        WaitingSpinner()
    }
}

