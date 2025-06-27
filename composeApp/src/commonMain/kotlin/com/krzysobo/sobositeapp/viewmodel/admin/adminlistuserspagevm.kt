package com.krzysobo.sobositeapp.viewmodel.admin

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.krzysobo.soboapptpl.viewmodel.SoboViewModel
import com.krzysobo.soboapptpl.viewmodel.toggleRefreshCompose
import com.krzysobo.sobositeapp.http.AdminUserListResponse
import com.krzysobo.sobositeapp.http.AdminUserModel
import com.krzysobo.sobositeapp.http.DataResponse
import com.krzysobo.sobositeapp.service.ApiServerUnavailableException
import com.krzysobo.sobositeapp.service.HttpException
import com.krzysobo.sobositeapp.service.HttpService
import com.krzysobo.sobositeapp.service.NetworkConnectionUnavailableException
import com.krzysobo.sobositeapp.service.findNetworkExceptions
import com.krzysobo.sobositeapp.viewmodel.getUserToken
import io.ktor.client.call.NoTransformationFoundException
import kotlin.math.ceil


class AdminListUsersPageVM : SoboViewModel(
) {

    var userList: MutableState<List<AdminUserModel>> = mutableStateOf(emptyList())
    var userList2: MutableState<AdminUserListResponse> = mutableStateOf(AdminUserListResponse())
    var itemsNo: MutableState<Int> = mutableStateOf(0)

    var itemOffset: MutableState<Int> = mutableStateOf(0)
    var pageNo: MutableState<Int> = mutableStateOf(1)
    var pageSize: MutableState<Int> = mutableStateOf(20)
    var userListUpdated: MutableState<Boolean> = mutableStateOf(false)
    var showUserList: MutableState<Boolean> = mutableStateOf(true)

    var isDeletionOpen: MutableState<Boolean> = mutableStateOf(false)
    var isDeletionOk: MutableState<Boolean> = mutableStateOf(false)

    var deletionUserModel: MutableState<AdminUserModel> = mutableStateOf(AdminUserModel())

    fun openDeletionForUser(user: AdminUserModel) {
        isDeletionOpen.value = true
        deletionUserModel.value = user
    }

    fun closeDeletion() {
        isDeletionOpen.value = false
        deletionUserModel.value = AdminUserModel()
    }


    fun totalPages(): Int {
        return ceil(itemsNo.value.toDouble() / pageSize.value.toDouble()).toInt()
    }

    fun updateItemOffsetByPage() {
        itemOffset.value = pageNo.value * pageSize.value - pageSize.value
    }

    suspend fun doGetUsers() {
        try {
            val resp: DataResponse<AdminUserListResponse> = HttpService().adminGetUserList(
                getUserToken()
            )
            if (resp.data != null) {
                userList.value = resp.data.items
                userList2.value = resp.data
                itemsNo.value = resp.data.count
            } else {
                userList.value = emptyList()
                userList2.value = AdminUserListResponse()
                itemsNo.value = 0
            }
        } catch (e: NetworkConnectionUnavailableException) {
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: ApiServerUnavailableException) {
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: NoTransformationFoundException) {
            isApiError.value = true
            apiErrorDetails.value = "Server error. Please contact the administrator (${e.message}"
        } catch (e: Exception) {
            isApiError.value = true
            apiErrorDetails.value = "ERROR: ${e.message}"
        }
    }

    suspend fun doAdminDeleteUser(userId: String): Boolean {
        if (userId == "") {
            return false
        }

        try {
            val resp = HttpService().adminUserDelete(getUserToken(), userId)

            isFormSent.value = true
            isDeletionOk.value = true
            isApiError.value = false
            apiErrorDetails.value = ""

            toggleRefreshCompose()

            return true
        } catch (e: HttpException) {
            apiErrorDetails.value = "A network error has occurred. " +
                    "Please check your internet connection."
        } catch (e: NetworkConnectionUnavailableException) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: ApiServerUnavailableException) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "${e.message}"
        } catch (e: NoTransformationFoundException) {
            isFormSent.value = false
            isApiError.value = true
            apiErrorDetails.value = "Server error. Please contact the administrator"
        } catch (e: Exception) {
            apiErrorDetails.value = findNetworkExceptions(e)
            isFormSent.value = false
            isApiError.value = true
        }

        isFormSent.value = false
        isApiError.value = true
        toggleRefreshCompose()
        return false
    }

}

