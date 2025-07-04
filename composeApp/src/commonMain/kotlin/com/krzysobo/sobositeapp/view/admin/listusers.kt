package com.krzysobo.sobositeapp.view

import WaitingSpinner
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.krzysobo.soboapptpl.pubres.PubRes
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.soboapptpl.service.anyResText
import com.krzysobo.soboapptpl.widgets.ErrorMessageBox
import com.krzysobo.soboapptpl.widgets.MessageBox
import com.krzysobo.soboapptpl.widgets.ToggleableDialog
import com.krzysobo.sobositeapp.appres.AppRes
import com.krzysobo.sobositeapp.view.admin.ShowUsersList
import com.krzysobo.sobositeapp.viewmodel.admin.AdminListUsersPageVM
import com.krzysobo.sobositeapp.viewmodel.getAdminListUsersPageVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun deletionDialog(vm: AdminListUsersPageVM, coroutineScope: CoroutineScope) {

    if ((vm.isDeletionOpen.value) && (vm.deletionUserModel.value.id != "")) {
        val userToDelete = vm.deletionUserModel.value

        ToggleableDialog(
            visible = vm.isDeletionOpen.value,
            onDismissRequest = { vm.closeDeletion() },
            onConfirmation = {
                coroutineScope.launch {
                    vm.showUserList.value = false
                    vm.doAdminDeleteUser(userToDelete.id)
                    vm.doGetUsers()
                    vm.closeDeletion()
                    vm.showUserList.value = true
                }
            },
            dialogTitle = anyResText(AnyRes(AppRes.string.user_deletion_s, arrayOf(userToDelete.email))),
            dialogText = anyResText(
                AnyRes(
                    AppRes.string.user_deletion_qs,
                    arrayOf(userToDelete.email, userToDelete.first_name, userToDelete.last_name)
                )
            ),
            confirmButtonText = anyResText(AnyRes(PubRes.string.yes)),
            dismissButtonText = anyResText(AnyRes(PubRes.string.no))
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PageSobositeAdminListUsers() {
    val vm: AdminListUsersPageVM = getAdminListUsersPageVM()
    val coroutineScope = rememberCoroutineScope()

    vm.showUserList = remember {mutableStateOf(true)}

    if (!vm.userListUpdated.value) {
        coroutineScope.launch {
            vm.showUserList.value = false
            vm.doGetUsers()
            vm.showUserList.value = true
        }
    }


    val footerTextStyle = MaterialTheme.typography.h6.copy(
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    deletionDialog(vm, coroutineScope)

    if ((vm.isFormSent.value) && (vm.isDeletionOk.value)) {
        MessageBox(
            "* ${anyResText(AnyRes(AppRes.string.user_deletion_ok))} *",
            anyResText(AnyRes(AppRes.string.user_deletion_ok_desc))
        )
    } else if (vm.isApiError.value) {
        ErrorMessageBox(
            "* ${anyResText(AnyRes(AppRes.string.error_api_error))} *",
            vm.apiErrorDetails.value
        )
    }

    if (vm.showUserList.value) {
        ShowUsersList(vm, footerTextStyle)
    } else {
        WaitingSpinner()
    }
}
