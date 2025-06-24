package com.krzysobo.sobositeapp.view.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import apptpl.composeapp.generated.resources.Res
import apptpl.composeapp.generated.resources.actions
import apptpl.composeapp.generated.resources.back
import apptpl.composeapp.generated.resources.create_user
import apptpl.composeapp.generated.resources.delete
import apptpl.composeapp.generated.resources.edit
import apptpl.composeapp.generated.resources.email

import com.krzysobo.soboapptpl.service.anyResText


import apptpl.composeapp.generated.resources.first_name
import apptpl.composeapp.generated.resources.forward
import apptpl.composeapp.generated.resources.full_name
import apptpl.composeapp.generated.resources.last_name
import apptpl.composeapp.generated.resources.no
import apptpl.composeapp.generated.resources.user_is_active
import apptpl.composeapp.generated.resources.user_is_staff
import apptpl.composeapp.generated.resources.user_is_staff_desc
import apptpl.composeapp.generated.resources.yes
import com.krzysobo.soboapptpl.service.AnyRes
import com.krzysobo.soboapptpl.service.SoboRouter
import com.krzysobo.sobositeapp.settings.AppRequestEditUser
import com.krzysobo.sobositeapp.settings.SOBOSITE_ROUTE_HANDLE
import com.krzysobo.sobositeapp.viewmodel.admin.AdminListUsersPageVM


@Composable
fun soboTableFooterAndroid(
    columns: List<String> = emptyList(),
    pageNoInit: Int = 0,
    itemsNo: Int = 0,
    totalPages: Int = 0,
    curPageSizeInit: Int = 10,
    options: List<Int> = listOf(5, 10, 15, 20, 50, 100, 1000),
    cbUpdatePageSize: (Int) -> Unit = {},
    cbPagerClickPrevPage: () -> Unit = {},
    cbPagerClickNextPage: () -> Unit = {},
    footerTextStyle: TextStyle = TextStyle(),
    footerTextModifier: Modifier = Modifier.padding(top = 15.dp, start = 15.dp),
    footerRowModifier: Modifier = Modifier.padding(top = 10.dp),
    comboTextModifier: Modifier = Modifier.width(100.dp),
    backButtonModifier: Modifier = Modifier
        .padding(end = 5.dp)
        .size(size = 30.dp),
    forwardButtonModifier: Modifier = Modifier
        .padding(end = 5.dp)
        .size(size = 30.dp),
) {
    var pageNo: Int = pageNoInit;
    var curPageSize: Int = curPageSizeInit;

//    Row(modifier = Modifier.border(BorderStroke(width = 1.dp, color = Color.Gray))) {
    var curOptionIndex = options.indexOf(curPageSize) // init
    if (curOptionIndex == -1) {
        curOptionIndex = 0
//        curPageSize = options[curOptionIndex]
    }

//    val totalItems = anyResText(AnyRes(Res.string.total_items_n, itemsNo))
//    val pageSize = anyResText(AnyRes(Res.string.page_size))

    Row {
        Icon(
            modifier = Modifier.padding(end = 5.dp),
            imageVector = Icons.Default.Person,
            contentDescription = ""
        )
        Text(
            modifier = footerTextModifier
                .padding(end = 10.dp, top=0.dp),
            text = "$itemsNo",
            style = footerTextStyle.copy(fontSize = 14.sp)
        )
        com.krzysobo.soboapptpl.widgets.soboEasyCombo(
            options,
            onClick = { ind ->
                cbUpdatePageSize(options[ind])
            },
            selOptionIndex = curOptionIndex,
            textModifier = comboTextModifier
        )
        IconButton(
            modifier = backButtonModifier,
            onClick = {
                cbPagerClickPrevPage()
            },
            enabled = (pageNo > 1),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                anyResText(AnyRes(Res.string.back))
            )
        }

        val cellSize = with(LocalDensity.current) {
            MaterialTheme.typography.h6.fontSize.toDp() + 3.dp
        }

        val pageIndicatorWidth = cellSize * (
                "$pageNo".length + "$totalPages".length + 3)
        Text(
            modifier = Modifier
                .padding(all = 5.dp)
                .width(width = pageIndicatorWidth)
                .height(height = cellSize + 4.dp)
                .border(BorderStroke(width = 1.dp, color = Color.Gray)),
            style = footerTextStyle,
            text = "$pageNo / $totalPages"
        )
        IconButton(
            modifier = forwardButtonModifier,
            onClick = {
                cbPagerClickNextPage()
            },
            enabled = (pageNo < totalPages),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                anyResText(AnyRes(Res.string.forward))
            )
        }
    }

//    Row(modifier = footerRowModifier) {
//    }
//    }

}


@Composable
actual fun ShowUsersList(vm: AdminListUsersPageVM, footerTextStyle: TextStyle) {

    val footerTs = footerTextStyle.copy(
        fontSize = 13.sp
    )
    val columns = listOf(
        anyResText(AnyRes(Res.string.email)),
        anyResText(AnyRes(Res.string.first_name)),
        anyResText(AnyRes(Res.string.last_name)),
        anyResText(AnyRes(Res.string.user_is_active)),
        anyResText(AnyRes(Res.string.user_is_staff)),
        anyResText(AnyRes(Res.string.actions)),
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // ---- FOOTER --> HEADER ----
        stickyHeader {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp)
                    .background(color = Color(red = 0xEE, green = 0xEE, blue = 0xEE, alpha = 0xFF))
            ) {
                soboTableFooterAndroid(
                    columns = columns,
                    options = listOf(2, 3, 5, 10, 20, 50, 100, 1000),

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
                    footerTextStyle = footerTs
                )
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

        }
        // ---- /FOOTER --> HEADER ----

        // ---- CONTENT ----
        if (vm.userList.value.isNotEmpty()) {
            for (itemI in vm.itemOffset.value..vm.userList.value.indices.max()) {
                if (itemI >= vm.itemOffset.value + vm.pageSize.value) {
                    break
                }

                val user = vm.userList.value[itemI]

                item {
                    val isActiveStr =
                        if (user.is_active) anyResText(AnyRes(Res.string.yes)) else anyResText(
                            AnyRes(Res.string.no)
                        )
                    val isStaffStr =
                        if (user.is_staff) anyResText(AnyRes(Res.string.yes)) else anyResText(
                            AnyRes(Res.string.no)
                        )

                    Card(
                        modifier = Modifier
                            .padding(all = 10.dp)
                            .fillMaxWidth(),
                        border = BorderStroke(width = 1.dp, color = Color.Blue)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 7.dp),
                                text = "${anyResText(AnyRes(Res.string.email))}: ${user.email}"
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 7.dp),
                                text = "${anyResText(AnyRes(Res.string.full_name))}: " +
                                        "${user.first_name} ${user.last_name}"
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 7.dp),
                                text = "${anyResText(AnyRes(Res.string.user_is_active))} $isActiveStr  " +
                                        "${anyResText(AnyRes(Res.string.user_is_staff_desc))}? $isStaffStr"
                            )

                            Row(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth()
                            ) {
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
                                        anyResText(AnyRes(Res.string.edit))
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
        }
        // ---- /CONTENT ----

    }
}
