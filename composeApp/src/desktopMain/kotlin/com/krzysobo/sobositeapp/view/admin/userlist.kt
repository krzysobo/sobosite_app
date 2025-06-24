package com.krzysobo.sobositeapp.view.admin

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import apptpl.composeapp.generated.resources.Res
import apptpl.composeapp.generated.resources.actions
import apptpl.composeapp.generated.resources.create_user
import apptpl.composeapp.generated.resources.delete
import apptpl.composeapp.generated.resources.edit
import apptpl.composeapp.generated.resources.email
import apptpl.composeapp.generated.resources.first_name
import apptpl.composeapp.generated.resources.last_name
import apptpl.composeapp.generated.resources.user_is_active
import apptpl.composeapp.generated.resources.user_is_staff
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.soboapptpl.service.SoboRouter
import com.krzysobo.soboapptpl.service.anyResText
import com.krzysobo.soboapptpl.widgets.soboTableDataRow
import com.krzysobo.soboapptpl.widgets.soboTableFooter
import com.krzysobo.soboapptpl.widgets.soboTableHeader
import com.krzysobo.sobositeapp.settings.AppRequestEditUser
import com.krzysobo.sobositeapp.settings.SOBOSITE_ROUTE_HANDLE
import com.krzysobo.sobositeapp.viewmodel.admin.AdminListUsersPageVM


@Composable
actual fun ShowUsersList(vm: AdminListUsersPageVM, footerTextStyle: TextStyle) {
    val columns = listOf(
        anyResText(AnyRes(Res.string.email)),
        anyResText(AnyRes(Res.string.first_name)),
        anyResText(AnyRes(Res.string.last_name)),
        anyResText(AnyRes(Res.string.user_is_active)),
        anyResText(AnyRes(Res.string.user_is_staff)),
        anyResText(AnyRes(Res.string.actions)),
    )

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .width(680.dp),
        columns = GridCells.Fixed(columns.size),
    ) {
        item(span = { GridItemSpan(6) }) {
            Row {
                Button(onClick = {
                    SoboRouter.navigateToRouteHandle(SOBOSITE_ROUTE_HANDLE.ADMIN_EDIT_USER.value)
                }) {
                    Icon(
                        modifier = Modifier
                            .padding(end = 10.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = anyResText(AnyRes(Res.string.create_user))
                    )
                    Text(anyResText(AnyRes(Res.string.create_user)))
                }
            }
        }

        // ---- HEADER ----
        soboTableHeader(columns)
        // ---- /HEADER ----

        // ---- CONTENT ----
        if (vm.userList.value.isNotEmpty()) {
            for (itemI in vm.itemOffset.value..vm.userList.value.indices.max()) {
                if (itemI >= vm.itemOffset.value + vm.pageSize.value) {
                    break
                }

                val user = vm.userList.value[itemI]
                soboTableDataRow(user.fieldsForList()) {
                    item {
                        Row {
                            IconButton(
                                modifier = Modifier
                                    .padding(end = 5.dp),
                                onClick = {
                                    SoboRouter.navigateToRouteHandle(
                                        SOBOSITE_ROUTE_HANDLE.ADMIN_EDIT_USER.value,
                                        AppRequestEditUser(userId = user.id)
                                    )
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = anyResText(AnyRes(Res.string.edit))
                                )
                            }

                            IconButton(
                                modifier = Modifier,
                                onClick = { vm.openDeletionForUser(user) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = anyResText(AnyRes(Res.string.delete))
                                )
                            }
                        }
                    }
                }
            }
        }
        // ---- /CONTENT ----

        // ---- FOOTER ----
        item(span = { GridItemSpan(columns.size) }) {
            Row {
                soboTableFooter(
                    columns = columns,
                    pageNoInit = vm.pageNo.value,
                    itemsNo = vm.itemsNo.value,
                    totalPages = vm.totalPages(),
                    curPageSizeInit = vm.pageSize.value,
                    cbUpdatePageSize = { ps ->
                        vm.pageSize.value = ps
                        vm.pageNo.value = 1
                        vm.updateItemOffsetByPage()
                    },
                    cbPagerClickPrevPage = {
                        vm.pageNo.value -= 1
                        vm.updateItemOffsetByPage()
                    },
                    cbPagerClickNextPage = {
                        vm.pageNo.value += 1
                        vm.updateItemOffsetByPage()
                    },
                    footerTextStyle = footerTextStyle
                )
            }
        }
        // ---- /FOOTER ----

    }

}
