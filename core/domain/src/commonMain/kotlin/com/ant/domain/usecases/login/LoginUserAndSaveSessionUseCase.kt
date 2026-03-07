package com.ant.domain.usecases.login

import com.ant.shared.logger.Logger
import com.ant.domain.usecases.resultFlow
import com.ant.models.model.Result
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import com.ant.models.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Authenticates the user with TMDb and persists the session locally.
 *
 * If an active session already exists, it returns immediately without re-authenticating.
 * Otherwise, it delegates to [LoginUserToTmDbUseCase] and saves the resulting session ID
 * and username via [SessionManager].
 */
class LoginUserAndSaveSessionUseCase constructor(
    private val logger: Logger,
    private val loginUserToTmDbUseCase: LoginUserToTmDbUseCase,
    private val sessionManager: SessionManager,
    private val dispatcher: CoroutineDispatcher,
) {
    /** @return a Flow emitting [UserData] with the authenticated session information. */
    operator fun invoke(parameters: RequestType.LoginSessionRequest.WithCredentials): Flow<Result<UserData>> {
        return resultFlow(dispatcher) {
            if (!sessionManager.getSessionId().isNullOrEmpty()) {
                UserData(
                    username = parameters.username,
                )
            } else {
                loginUserToTmDbUseCase.invoke(parameters)
                    .filterNot { it is Result.Loading }
                    .map { result ->
                        if (result is Result.Success) {
                            val userData = result.data
                            logger.d("Login to TmDb APi successful. SessionId: ${result.data.sessionId}")
                            userData.apply {
                                sessionId?.let { sessionId ->
                                    sessionManager.saveSessionId(sessionId = sessionId)
                                    sessionManager.saveUsername(username = userData.username)
                                }
                            }
                        } else {
                            throw (result as Result.Error).throwable
                        }
                    }.first()
            }
        }
    }
}
