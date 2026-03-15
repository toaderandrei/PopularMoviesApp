package com.ant.data.repositories.login

import com.ant.common.exceptions.NetworkError
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import com.ant.network.dto.CreateSessionBody
import com.ant.network.dto.RequestTokenDto
import com.ant.network.dto.SessionDto
import com.ant.network.dto.ValidateTokenBody
import com.ant.network.ktx.safeResourceGet
import com.ant.network.ktx.safeResourcePost
import com.ant.network.mappers.login.LoginSessionMapper
import com.ant.network.resources.AuthResources
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Authenticates a user against TMDb using the three-step token flow:
 * request token -> validate with credentials -> create session.
 */
class LoginUserTmDbRepository(
    private val client: HttpClient,
    private val loginSessionMapper: LoginSessionMapper,
) {
    /**
     * Performs the full TMDb login flow and returns the authenticated user's data.
     *
     * @return [UserData] containing the session ID and username.
     */
    suspend fun performRequest(params: RequestType.LoginSessionRequest.WithCredentials): UserData {
        // Step 1: Request token
        val tokenResponse: RequestTokenDto = client.safeResourceGet(
            resource = AuthResources.CreateToken(),
            maxAttempts = 1,
        )

        val requestToken = tokenResponse.requestToken
            ?: throw NetworkError.Unknown(message = "Request token was null")

        // Step 2: Validate with credentials — 401 = wrong password
        val validate: RequestTokenDto = client.safeResourcePost(
            resource = AuthResources.ValidateToken(),
            maxAttempts = 1,
        ) {
            contentType(ContentType.Application.Json)
            setBody(
                ValidateTokenBody(
                    username = params.username,
                    password = params.password
                        ?: throw NetworkError.Unknown(message = "Password is required for login"),
                    requestToken = requestToken,
                )
            )
        }

        val validatedToken = validate.requestToken
            ?: throw NetworkError.Unknown(message = "Validated token was null")

        // Step 3: Create session
        val accountSession: SessionDto = client.safeResourcePost(
            resource = AuthResources.CreateSession(),
            maxAttempts = 1,
        ) {
            contentType(ContentType.Application.Json)
            setBody(CreateSessionBody(validatedToken))
        }

        val session = loginSessionMapper.map(accountSession)
        return session.copy(username = params.username)
    }
}
