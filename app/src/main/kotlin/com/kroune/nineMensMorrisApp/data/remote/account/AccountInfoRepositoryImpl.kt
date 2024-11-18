package com.kroune.nineMensMorrisApp.data.remote.account

import android.util.Log
import com.kroune.nineMensMorrisApp.StorageManager
import com.kroune.nineMensMorrisApp.common.SERVER_ADDRESS
import com.kroune.nineMensMorrisApp.common.USER_API
import com.kroune.nineMensMorrisApp.data.remote.Common.network
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json

/**
 * remote repository implementation
 */
class AccountInfoRepositoryImpl : AccountInfoRepositoryI {
    override val accountIdState = MutableStateFlow(StorageManager.getLong("accountId"))

    override fun updateAccountIdState(value: Long?) {
        Log.d("AccountId", "account id has changed to $value")
        StorageManager.putLong("accountId", value)
        accountIdState.value = value
    }

    override val jwtTokenState = MutableStateFlow(StorageManager.getString("jwtToken"))

    override fun updateJwtTokenState(value: String?) {
        Log.d("JwtToken", "jwt token has changed to $value")
        if (jwtTokenState.value == value) {
            // nothing changed, no need to update anything
            return
        }
        jwtTokenState.value = value
        StorageManager.putString("jwtToken", value)
        if (value == null) {
            updateAccountIdState(null)
            return
        }
    }

    override suspend fun getAccountRatingById(id: Long, jwtToken: String): Result<Long?> {
        var response: Long? = null
        return runCatching {
            val request = network.get("http${SERVER_ADDRESS}${USER_API}/get-rating-by-id") {
                method = HttpMethod.Get
                url {
                    parameters["id"] = id.toString()
                    parameters["jwtToken"] = jwtToken
                }
            }.bodyAsText()
            response = Json.decodeFromString<Long?>(request)
            response
        }.onFailure {
            println("error getting account rating $id; response - $response")
            it.printStackTrace()
        }
    }

    override suspend fun getAccountDateById(
        id: Long,
        jwtToken: String
    ): Result<Triple<Int, Int, Int>?> {
        var response: Triple<Int, Int, Int>? = null
        return runCatching {
            val request = network.get("http${SERVER_ADDRESS}${USER_API}/get-creation-date-by-id") {
                method = HttpMethod.Get
                url {
                    parameters["id"] = id.toString()
                    parameters["jwtToken"] = jwtToken
                }
            }.bodyAsText()
            response = Json.decodeFromString<Triple<Int, Int, Int>?>(request)
            response
        }.onFailure {
            println("error getting account creation date $id; response - $response")
            it.printStackTrace()
        }
    }

    override suspend fun getAccountNameById(id: Long, jwtToken: String): Result<String?> {
        var response: String? = null
        return runCatching {
            val request = network.get("http${SERVER_ADDRESS}${USER_API}/get-login-by-id") {
                method = HttpMethod.Get
                url {
                    parameters["id"] = id.toString()
                    parameters["jwtToken"] = jwtToken
                }
            }.bodyAsText()
            response = Json.decodeFromString<String?>(request)
            response
        }.onFailure {
            println("error getting account name $id; response - $response")
            it.printStackTrace()
        }
    }

    override suspend fun getIdByJwtToken(jwtToken: String): Result<Long> {
        var response: Long? = null
        return runCatching {
            val request = network.get("http${SERVER_ADDRESS}${USER_API}/get-id-by-jwt-token") {
                method = HttpMethod.Get
                url {
                    parameters["jwtToken"] = jwtToken
                }
            }.bodyAsText()
            response = Json.decodeFromString<Long>(request)
            response!!
        }.onFailure {
            println("error getting account id $jwtToken; response - $response")
            it.printStackTrace()
        }
    }

    override suspend fun getLeaderBoard(): Result<List<Long>> {
        var response: String? = null
        return runCatching {
            val httpResponse: HttpResponse =
                network.get("http${SERVER_ADDRESS}${USER_API}/leaderboard") {
                    method = HttpMethod.Get
                }
            response = httpResponse.bodyAsText()
            Json.decodeFromString<List<Long>>(response!!)
        }.onFailure {
            println("error getting leaderboard, response - $response")
            it.printStackTrace()
        }
    }

    override fun logout() {
        updateAccountIdState(null)
        updateJwtTokenState(null)
        Log.d("ACCOUNT", "Logged out")
    }

    override suspend fun getAccountPictureById(id: Long, jwtToken: String): Result<ByteArray> {
        var response: String? = null
        return runCatching {
            val httpResponse: HttpResponse =
                network.get("http${SERVER_ADDRESS}${USER_API}/get-picture-by-id") {
                    method = HttpMethod.Get
                    url {
                        parameters["id"] = id.toString()
                        parameters["jwtToken"] = jwtToken
                    }
                }
            response = httpResponse.bodyAsText()
            Json.decodeFromString<ByteArray>(response!!)
        }.onFailure {
            println("error getting account picture id - $id, response - $response")
            it.printStackTrace()
        }
    }
}
