package com.ant.domain.usecases.login

import com.ant.domain.usecases.resultFlow
import com.ant.models.model.Result
import com.ant.models.model.UserData
import com.ant.models.session.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Loads the current user's account profile from the local session store.
 */
class LoadAccountProfileUseCase constructor(
    private val dispatcher: CoroutineDispatcher,
    private val sessionManager: SessionManager,
) {
    /** @return a Flow emitting the locally stored [UserData] (session ID and username). */
    operator fun invoke(parameters: Unit): Flow<Result<UserData>> {
        return resultFlow(dispatcher) {
            val sessionId = sessionManager.getSessionId()
            val username = sessionManager.getUsername()
            UserData(
                username = username,
                sessionId = sessionId,
            )
        }
    }
}
