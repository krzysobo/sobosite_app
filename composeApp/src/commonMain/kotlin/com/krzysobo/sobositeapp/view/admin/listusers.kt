package com.krzysobo.sobositeapp.view

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import apptpl.composeapp.generated.resources.Res
import apptpl.composeapp.generated.resources.error_api_error
import apptpl.composeapp.generated.resources.no
import apptpl.composeapp.generated.resources.user_deletion_ok
import apptpl.composeapp.generated.resources.user_deletion_ok_desc
import apptpl.composeapp.generated.resources.user_deletion_qs
import apptpl.composeapp.generated.resources.user_deletion_s
import apptpl.composeapp.generated.resources.yes
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.soboapptpl.service.anyResText
import com.krzysobo.soboapptpl.widgets.ErrorMessageBox
import com.krzysobo.soboapptpl.widgets.MessageBox
import com.krzysobo.sobositeapp.viewmodel.getAdminListUsersPageVM
import com.krzysobo.sobositeapp.viewmodel.admin.AdminListUsersPageVM
import com.krzysobo.soboapptpl.widgets.ToggleableDialog
import com.krzysobo.sobositeapp.view.admin.ShowUsersList
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
                    vm.doAdminDeleteUser(userToDelete.id)
                    vm.doGetUsers()
                    vm.closeDeletion()
                }
            },
            dialogTitle = anyResText(AnyRes(Res.string.user_deletion_s, arrayOf(userToDelete.email))),
            dialogText = anyResText(
                AnyRes(
                    Res.string.user_deletion_qs,
                    arrayOf(userToDelete.email, userToDelete.first_name, userToDelete.last_name)
                )
            ),
            confirmButtonText = anyResText(AnyRes(Res.string.yes)),
            dismissButtonText = anyResText(AnyRes(Res.string.no))
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PageSobositeAdminListUsers() {
    val vm: AdminListUsersPageVM = getAdminListUsersPageVM()
    val coroutineScope = rememberCoroutineScope()

    if (!vm.userListUpdated.value) {
        coroutineScope.launch {
            vm.doGetUsers()
        }
    }


    val footerTextStyle = MaterialTheme.typography.h6.copy(
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    deletionDialog(vm, coroutineScope)

    if ((vm.isFormSent.value) && (vm.isDeletionOk.value)) {
        MessageBox(
            "* ${anyResText(AnyRes(Res.string.user_deletion_ok))} *",
            anyResText(AnyRes(Res.string.user_deletion_ok_desc))
        )
    } else if (vm.isApiError.value) {
        ErrorMessageBox(
            "* ${anyResText(AnyRes(Res.string.error_api_error))} *",
            vm.apiErrorDetails.value
        )
    }

    ShowUsersList(vm, footerTextStyle)
}
