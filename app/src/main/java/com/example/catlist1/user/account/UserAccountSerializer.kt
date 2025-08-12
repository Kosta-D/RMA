package com.example.catlist1.user.account

import androidx.datastore.core.okio.OkioSerializer
import com.example.catlist1.user.domain.UserAccount
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource

class UserAccountSerializer: OkioSerializer<UserAccount?> {
    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override val defaultValue: UserAccount?
        get() = null

    override suspend fun readFrom(source: BufferedSource): UserAccount? {
        return runCatching {
            jsonSerializer.decodeFromString<UserAccount?>(source.readUtf8())
        }.getOrNull()
    }

    override suspend fun writeTo(
        t: UserAccount?,
        sink: BufferedSink
    ) {
        sink.use {
            if (t != null) {
                it.writeUtf8(jsonSerializer.encodeToString(UserAccount.serializer(), t))
            } else {
                it.writeUtf8("")
            }
        }
    }
}