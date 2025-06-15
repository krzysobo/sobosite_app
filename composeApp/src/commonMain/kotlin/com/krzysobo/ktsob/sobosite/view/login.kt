package com.krzysobo.ktsob.sobosite.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import apptpl.composeapp.generated.resources.Res
import apptpl.composeapp.generated.resources.api_prefix_s
import apptpl.composeapp.generated.resources.api_test_sd
import apptpl.composeapp.generated.resources.error_bad_credentials_long_p1
import apptpl.composeapp.generated.resources.error_bad_credentials_long_p2
import apptpl.composeapp.generated.resources.error_bad_credentials_long_p3
import apptpl.composeapp.generated.resources.error_bad_credentials_long_p4
import apptpl.composeapp.generated.resources.error_bad_credentials_long_p5
import apptpl.composeapp.generated.resources.error_login_error
import apptpl.composeapp.generated.resources.error_unknown_error
import apptpl.composeapp.generated.resources.log_in
import apptpl.composeapp.generated.resources.login_page
import apptpl.composeapp.generated.resources.register
import apptpl.composeapp.generated.resources.reset_pass
import com.krzysobo.ktsob.apptpl.service.AnyRes
import com.krzysobo.ktsob.sobosite.service.HttpService
import com.krzysobo.ktsob.apptpl.service.SoboRouter
import com.krzysobo.ktsob.apptpl.service.anyResText
import com.krzysobo.ktsob.apptpl.widgets.ErrorMessageBox
import com.krzysobo.ktsob.sobosite.widgets.LoginWidget
import com.krzysobo.ktsob.apptpl.widgets.PageHeader
import com.krzysobo.ktsob.sobosite.settings.SOBOSITE_ROUTE_HANDLE
import com.krzysobo.ktsob.sobosite.widgets.PasswordWidget
import com.krzysobo.ktsob.sobosite.viewmodel.getLoginPageVM
import com.krzysobo.ktsob.sobosite.viewmodel.LoginPageVM
import com.krzysobo.ktsob.sobosite.viewmodel.toggleRefreshCompose
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun PageSobositeLogin() {
    val vm: LoginPageVM = getLoginPageVM()
    val coroutineScope = rememberCoroutineScope()

    vm.login = remember { mutableStateOf("") }
    vm.pass = remember { mutableStateOf("") }
    vm.isErrorLogin = remember { mutableStateOf(false) }
    vm.isErrorPass = remember { mutableStateOf(false) }
    vm.passVisible = remember { mutableStateOf(false) }
    vm.isFormSent = remember { mutableStateOf(false) }
    vm.isApiError = remember { mutableStateOf(false) }
    vm.apiErrorDetails = remember { mutableStateOf("") }
    vm.isAuthError = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(all = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PageHeader(anyResText(AnyRes(Res.string.login_page)))
        Text(anyResText(AnyRes(Res.string.api_prefix_s, HttpService().retApiPrefix())))

        val focusManager = LocalFocusManager.current

        if(vm.isFormSent.value) {
            SoboRouter.navigateToHome()
            return
        } else {
            val resLoginError = anyResText(AnyRes(Res.string.error_login_error ))
            if(vm.isAuthError.value) {
                val errorText = buildAnnotatedString {
                    append(anyResText(AnyRes(Res.string.error_bad_credentials_long_p1)))
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(anyResText(AnyRes(Res.string.error_bad_credentials_long_p2)))
                    }
                    append(anyResText(AnyRes(Res.string.error_bad_credentials_long_p3)))
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(anyResText(AnyRes(Res.string.error_bad_credentials_long_p4)))
                    }
                    append(anyResText(AnyRes(Res.string.error_bad_credentials_long_p5)))
                }
                ErrorMessageBox("* $resLoginError *", errorText)
            } else if (vm.isApiError.value) {
                val errorText = if (vm.apiErrorDetails.value != "") vm.apiErrorDetails.value else
                    anyResText(AnyRes(Res.string.error_unknown_error))
                ErrorMessageBox("* $resLoginError *", errorText)
            }


            /**
             * username  https://developer.android.com/develop/ui/compose/text/user-input
             */
            LoginWidget(
                value = vm.login.value,
                onValueChanges = { data: String ->
//                    println("login value change $data")
                    vm.login.value = data
                    vm.clearLoginError()
                },
                isError = vm.isErrorLogin.value,
            )

            /**
             * Password
             */
            PasswordWidget(
                value=vm.pass.value,
                onValueChanges = { data: String ->
//                    println("password value change $data")
                    vm.pass.value = data
                    vm.clearPassError()
                },
                isError = vm.isErrorPass.value,
                trailingIconPassOnClick={ vm.togglePassVisible()},
                isPassVisible=vm.isPassVisible,
            )


            Row {
                Button(
                    onClick = {
                        vm.clearErrors()
                        val res: Boolean = vm.validate()
                        if (res) {
//                            println("FORM IS CORRECT, trying to log in...")
                            coroutineScope.launch {
                                vm.doLogIn()
                            }
                        }
                    },
                    modifier = Modifier.padding(all = 10.dp)
                ) { Text(anyResText(AnyRes(Res.string.log_in))) }

                Button(
                    onClick = {
//                        println("Register clicked!");
                        SoboRouter.navigateToRouteHandle(SOBOSITE_ROUTE_HANDLE.REGISTER.value)
                    },
                    modifier = Modifier.padding(all = 10.dp)
                ) { Text(anyResText(AnyRes(Res.string.register))) }

                Button(
                    onClick = {
//                        println("Reset password clicked!");
                        SoboRouter.navigateToRouteHandle(SOBOSITE_ROUTE_HANDLE.RESET_PASS.value)
                    },
                    modifier = Modifier.padding(all = 10.dp)
                ) { Text(anyResText(AnyRes(Res.string.reset_pass))) }

            }
        }

    }
}
