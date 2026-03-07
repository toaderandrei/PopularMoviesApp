package com.ant.network.mappers.login

import com.ant.models.model.UserData
import com.ant.network.dto.SessionDto
import com.ant.network.mappers.Mapper

/** Maps [SessionDto] to [UserData], extracting the session ID. */
class LoginSessionMapper : Mapper<SessionDto, UserData> {
    override suspend fun map(from: SessionDto): UserData {
        return UserData(
            sessionId = from.sessionId,
        )
    }
}
