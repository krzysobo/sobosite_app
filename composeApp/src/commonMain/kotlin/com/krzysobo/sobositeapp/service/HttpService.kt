package com.krzysobo.sobositeapp.service

import com.krzysobo.sobositeapp.getHttpClient
import com.krzysobo.sobositeapp.http.AdminUserCreateRequest
import com.krzysobo.sobositeapp.http.AdminUserListResponse
import com.krzysobo.sobositeapp.http.AdminUserModel
import com.krzysobo.sobositeapp.http.AdminUserUpdateRequest
import com.krzysobo.sobositeapp.http.DataResponse
import com.krzysobo.sobositeapp.http.LoginRequest
import com.krzysobo.sobositeapp.http.LoginResponse
import com.krzysobo.sobositeapp.http.ProfileOwnGetResponse
import com.krzysobo.sobositeapp.http.ProfileOwnUpdateResponse
import com.krzysobo.sobositeapp.http.RegisterCreateResponse
import com.krzysobo.sobositeapp.http.RegisterRequest
import com.krzysobo.sobositeapp.http.ResetPasswordConfirmRequest
import com.krzysobo.sobositeapp.http.ResetPasswordRequest
import com.krzysobo.sobositeapp.http.UserProfileOwnWithPasswordUpdateRequest
import com.krzysobo.sobositeapp.isNetworkConnectionAvailable
import com.krzysobo.sobositeapp.settings.AppSettings
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonObject

open class HttpException(message: String) : Exception(message)
open class ApiServerUnavailableException(message: String) : HttpException(message)
open class NetworkConnectionUnavailableException(message: String) : HttpException(message)

open class LoginException(message: String) : HttpException(message)

class BadCredentialsException(message: String) : LoginException(message)
class RegisterConfirmationException(message: String) : LoginException(message)
class AuthException(message: String) : HttpException(message)
class EmailAlreadyExistsException(message: String) : HttpException(message)
class InvalidEmailException(message: String) : HttpException(message)
class UnauthorizedException(message: String) : HttpException(message)


class HttpService() {
    private var apiPrefix = ""

    init {
        apiPrefix = "${AppSettings.apiHostUrl.trimEnd('/')}/${AppSettings.apiPrefix.trim('/')}/"
    }

    constructor(apiPrefix: String = "") : this() {
        this.apiPrefix = apiPrefix
    }

    private val urlServerInfo = "${apiPrefix.trimEnd('/')}/info/"
    private val urlUserLogin = "${apiPrefix.trimEnd('/')}/user_forms/user/login/"
    private val urlUserLogout = "${apiPrefix.trimEnd('/')}/user_forms/user/logout/"
    private val urlUserProfileOwnGet = "${apiPrefix.trimEnd('/')}/user_forms/user/profile/own/"
    private val urlUserProfileOwnUpdate =
        "${apiPrefix.trimEnd('/')}/user_forms/user/profile/own/update/"
    private val urlUserProfileOwnChangePassword =
        "${apiPrefix.trimEnd('/')}/user_forms/user/profile/own/change-password/"

    private val urlUserRegisterCreate = "${apiPrefix.trimEnd('/')}/user_forms/user/register/create/"
    private val urlUserRegisterConfirm =
        "${apiPrefix.trimEnd('/')}/user_forms/user/register/confirm/[email]/[token]/"
    private val urlUserResetPasswordSendRequest =
        "${apiPrefix.trimEnd('/')}/user_forms/user/reset-password/send-request/"
    private val urlUserResetPasswordConfirm =
        "${apiPrefix.trimEnd('/')}/user_forms/user/reset-password/confirm/[email]/[token]/"

    val urlAdminUserList = "${apiPrefix.trimEnd('/')}/user_forms/admin/user/"
    val urlAdminUserGet = "${apiPrefix.trimEnd('/')}/user_forms/admin/user/id/[id]/"
    val urlAdminUserCreate = "${apiPrefix.trimEnd('/')}/user_forms/admin/user/create/"
    val urlAdminUserUpdate = "${apiPrefix.trimEnd('/')}/user_forms/admin/user/id/[id]/"
    val urlAdminUserDelete = "${apiPrefix.trimEnd('/')}/user_forms/admin/user/id/[id]/"

    private suspend fun isAPIServerAvailable(): Boolean {
        val client = getHttpClient()

        val response: HttpResponse = client.get(urlServerInfo) {
            contentType(ContentType.Application.Json)
        }

        if (response.status == HttpStatusCode.OK) {
            return true;
        }

        return false
    }

    private suspend fun guardServerAvailable() {
        if (!isNetworkConnectionAvailable()) {
            throw NetworkConnectionUnavailableException(
                "Network is unavailable - " +
                        "check your connection."
            )
        }

        if (!isAPIServerAvailable()) {
            throw ApiServerUnavailableException(
                "no API server found at $apiPrefix - check " +
                        "your network connection and client configuration."
            )
        }
    }

    fun retApiPrefix(): String {
        return apiPrefix
    }

    suspend fun loginInt(username: String, password: String): Int {
        guardServerAvailable()

        val client = getHttpClient()
        val response: HttpResponse = client.post(urlUserLogin) {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(username, password));
        }

        return response.status.value
    }

    suspend fun login(username: String, password: String): DataResponse<LoginResponse> {
        guardServerAvailable()

        val client = getHttpClient()
        val response: HttpResponse = client.post(urlUserLogin) {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(username, password));
        }

        if (response.status != HttpStatusCode.OK) {
            when (response.status) {
                HttpStatusCode.Forbidden -> throw BadCredentialsException("Incorrect login or password.")
                HttpStatusCode.BadRequest -> {
                    val resBody: JsonObject = response.body()
                    throw HttpException("API Error. Please check your network connection and settings.")
                }
            }


        }

        val dataResponse: DataResponse<LoginResponse> = response.body()
        return dataResponse
    }

    suspend fun logout(token: String): HttpResponse {
        guardServerAvailable()

        val client = getHttpClient()
        val response: HttpResponse = client.post(urlUserLogout) {
            header(HttpHeaders.Authorization, "Token $token")
            contentType(ContentType.Application.Json)
        }

        return response;
    }

    suspend fun registerCreate(
        email: String,
        firstName: String,
        lastName: String,
        password: String
    ): DataResponse<RegisterCreateResponse> {
        guardServerAvailable()

        val client = getHttpClient()
        val response: HttpResponse = client.post(urlUserRegisterCreate) {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(email, firstName, lastName, password));
        }

        if (response.status != HttpStatusCode.OK) {
            findAndThrowUserEditionExceptions(response, email)
        }

        val registerCreateResponse: DataResponse<RegisterCreateResponse> = response.body()

        return registerCreateResponse
    }

    suspend fun registerConfirm(
        email: String,
        token: String,
    ): HttpResponse {
        guardServerAvailable()

        val client = getHttpClient()
        val url = urlUserRegisterConfirm
            .replace("[email]", email)
            .replace("[token]", token)

        val response: HttpResponse = client.get(url) {
            contentType(ContentType.Application.Json)
        }

        if (response.status != HttpStatusCode.OK) {
            throw RegisterConfirmationException("Could not confirm registration of $email.")
        }

        return response;
    }

    suspend fun userProfileOwnUpdate(
        token: String,
        email: String,
        firstName: String,
        lastName: String,
        oldPassword: String = "",
        newPassword: String = "",
    ): DataResponse<ProfileOwnUpdateResponse> {
        guardServerAvailable()

        val client = getHttpClient()
        val response: HttpResponse = client.put(urlUserProfileOwnUpdate) {
            header(HttpHeaders.Authorization, "Token $token")
            contentType(ContentType.Application.Json)
            setBody(
                UserProfileOwnWithPasswordUpdateRequest(
                    email, firstName, lastName, oldPassword, newPassword
                )
            );
        }

        if (response.status != HttpStatusCode.OK) {
            findAndThrowUserEditionExceptions(response, email)
        }

        val profileOwnResponse: DataResponse<ProfileOwnUpdateResponse> = response.body()
        return profileOwnResponse
    }

    suspend fun userProfileOwnGet(token: String): DataResponse<ProfileOwnGetResponse> {
        guardServerAvailable()

        val client = getHttpClient()
        val url = urlUserProfileOwnGet
        val response: HttpResponse = client.get(url) {
            header(HttpHeaders.Authorization, "Token $token")
            contentType(ContentType.Application.Json)
        }

        if (response.status != HttpStatusCode.OK) {
            when (response.status) {
                HttpStatusCode.Unauthorized -> throw UnauthorizedException("Not authorized.")
                else -> {
                    throw HttpException("API Error.")
                }
            }
        }

        val dataResponse: DataResponse<ProfileOwnGetResponse> = response.body()
        return dataResponse;
    }


    suspend fun resetPasswordSendRequest(
        email: String,
    ): HttpResponse {
        guardServerAvailable()

        val client = getHttpClient()
        val response: HttpResponse = client.post(urlUserResetPasswordSendRequest) {
            contentType(ContentType.Application.Json)
            setBody(ResetPasswordRequest(email));
        }

        if (response.status != HttpStatusCode.OK) {
            throw HttpException("Connection failed.")
        }

        return response;
    }

    suspend fun resetPasswordConfirm(
        email: String,
        token: String,
        newPassword: String,
    ): HttpResponse {
        guardServerAvailable()

        val client = getHttpClient()
        val url = urlUserResetPasswordConfirm
            .replace("[email]", email)
            .replace("[token]", token)

        val response: HttpResponse = client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(ResetPasswordConfirmRequest(newPassword));
        }

        if (response.status != HttpStatusCode.OK) {
            throw HttpException("Connection failed.")
        }

        return response;
    }

    // ----- admin methods -----
    suspend fun adminGetUserList(
        token: String,
        pageNo: Int = 0,
        pageSize: Int = 30
    ): DataResponse<AdminUserListResponse> {
        guardServerAvailable()

        val client = getHttpClient()
        val response: HttpResponse = client.get(urlAdminUserList) {
            header(HttpHeaders.Authorization, "Token $token")
            contentType(ContentType.Application.Json)
        }

        val userListResponse: DataResponse<AdminUserListResponse> = response.body()
        return userListResponse;
    }

    suspend fun adminUserGet(token: String, userId: String): DataResponse<AdminUserModel> {
        guardServerAvailable()

        val client = getHttpClient()
        val url = urlAdminUserGet.replace("[id]", userId)

        val response: HttpResponse = client.get(url) {
            header(HttpHeaders.Authorization, "Token $token")
            contentType(ContentType.Application.Json)
        }

        val userResponse: DataResponse<AdminUserModel> = response.body()
        return userResponse
    }

    suspend fun adminUserDelete(token: String, userId: String): HttpResponse {
        guardServerAvailable()

        val client = getHttpClient()
        val url = urlAdminUserDelete
            .replace("[id]", userId)

        val response: HttpResponse = client.delete(url) {
            header(HttpHeaders.Authorization, "Token $token")
            contentType(ContentType.Application.Json)
        }

        return response;
    }

    suspend fun adminUserUpdate(
        token: String,
        userId: String,
        email: String,
        firstName: String,
        lastName: String,
        isStaff: Boolean,
        isActive: Boolean,
        noFailedLogins: Int,
        failedIsBlocked: Boolean,
        failedIsBlockedThru: String,
        password: String = "",
    ): DataResponse<AdminUserModel> {
        guardServerAvailable()

        val client = getHttpClient()
        val url = urlAdminUserUpdate.replace("[id]", userId)
        val response: HttpResponse = client.put(url) {
            header(HttpHeaders.Authorization, "Token $token")
            contentType(ContentType.Application.Json)
            setBody(
                AdminUserUpdateRequest(
                    email,
                    firstName,
                    lastName,
                    isStaff,
                    isActive,
                    password,
                    noFailedLogins,
                    failedIsBlocked,
                    failedIsBlockedThru
                )
            )
        }

        if (response.status != HttpStatusCode.OK) {
            findAndThrowUserEditionExceptions(response, email)
        }

        val userResponse: DataResponse<AdminUserModel> = response.body()
        return userResponse
    }

    suspend fun adminUserCreate(
        token: String,
        email: String,
        firstName: String,
        lastName: String,
        isStaff: Boolean,
        isActive: Boolean,
        password: String = "",
    ): DataResponse<AdminUserModel> {
        guardServerAvailable()

        val client = getHttpClient()
        val response: HttpResponse = client.post(urlAdminUserCreate) {
            header(HttpHeaders.Authorization, "Token $token")
            contentType(ContentType.Application.Json)
            setBody(
                AdminUserCreateRequest(
                    email,
                    firstName,
                    lastName,
                    isStaff,
                    isActive,
                    password = password
                )
            )
        }

        if (response.status != HttpStatusCode.OK) {
            findAndThrowUserEditionExceptions(response, email)
        }

        val userResponse: DataResponse<AdminUserModel> = response.body()
        return userResponse
    }
}

suspend fun findAndThrowUserEditionExceptions(response: HttpResponse, email: String) {
    when (response.status) {
        HttpStatusCode.Conflict -> throw EmailAlreadyExistsException(
            "account with email $email already exists."
        )

        HttpStatusCode.Forbidden -> throw InvalidEmailException(
            "email $email is invalid."
        )

        HttpStatusCode.BadRequest -> {
            val resBody: JsonObject = response.body()
            throw HttpException("API Error.")
        }
    }

}

/**
 * a workaround regarding the problems with KTOR's exceptions. It sometimes
 *  throws java.net.* and java.io.* exceptions, which are unavailable in commonMain code, but
 *  still they break program operation. Therefore, I catch the broad Exception and determine them
 *  by the class name, using e::class.qualifiedName, where e an instance of Exception or its heirs
 */
fun findNetworkExceptions(e: Exception): String {
    var res = ""
    val qn = "${e::class.qualifiedName}"
    // java.net.* exceptions - java.net.ConnectException, java.net.SocketException, etc.
    // java.io.* exceptions - java.io.IOException "not permitted by network security policy" etc

    if ((qn.startsWith("java.net.")) || (qn.startsWith("java.io."))) {
        // IT IS JAVA.NET exception (${e::class.qualifiedName})
        res += "Network error. Please check your network connection " +
                "or contact your site administrator - msg: ${e.message}"
    } else {     // IT IS NOT THE JAVA.NET EXCEPTION (java.net.*)
        res += "${e.message}"
    }

    return res
}

