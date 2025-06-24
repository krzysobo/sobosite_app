package com.krzysobo.sobositeapp.view.admin

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.krzysobo.sobositeapp.viewmodel.admin.AdminListUsersPageVM

@Composable
expect fun ShowUsersList(vm: AdminListUsersPageVM, footerTextStyle: TextStyle)
