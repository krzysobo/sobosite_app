package com.krzysobo.ktsob.sobosite.view.admin

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.krzysobo.ktsob.sobosite.viewmodel.admin.AdminListUsersPageVM

@Composable
expect fun ShowUsersList(vm: AdminListUsersPageVM, footerTextStyle: TextStyle)
