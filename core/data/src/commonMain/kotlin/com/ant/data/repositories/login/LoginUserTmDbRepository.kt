package com.ant.data.repositories.login

import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import com.ant.network.api.TmdbAuthApi
import com.ant.network.mappers.login.LoginSessionMapper


/**
 * Authenticates a user against TMDb using the three-step token flow:
 * request token -> validate with credentials -> create session.
 */
class LoginUserTmDbRepository constructor(
    private val authApi: TmdbAuthApi,
    private val loginSessionMapper: LoginSessionMapper,
) {
    /**
     * Performs the full TMDb login flow and returns the authenticated user's data.
     *
     * @return [UserData] containing the session ID and username.
     */
    suspend fun performRequest(params: RequestType.LoginSessionRequest.WithCredentials): UserData {
        // First we need to request a token.
        val tokenResponse = authApi.createRequestToken()

        // Next we need to validate the token, username and password.
        val validate = authApi.validateTokenWithLogin(
            username = params.username,
            password = params.password ?: error("Password is required for login"),
            requestToken = tokenResponse.requestToken ?: error("Request token was null"),
        )

        // If successful we fetch the session.
        val accountSession = authApi.createSession(
            requestToken = validate.requestToken ?: error("Validated token was null"),
        )

        // If successful we return the session.
        val session = loginSessionMapper.map(accountSession)
        return session.copy(username = params.username)
    }
}
