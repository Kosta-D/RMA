package com.example.catlist1.user.account

import androidx.datastore.core.DataStore
import com.example.catlist1.user.domain.UserAccount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class UserAccountStore @Inject constructor(
    private val persistence: DataStore<UserAccount?>
){
    private val scope = CoroutineScope(Dispatchers.IO)

    val userAccount = persistence.data
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = runBlocking { persistence.data.first() }
        )

    suspend fun replaceUserAccount(userAccount: UserAccount) {
        persistence.updateData { userAccount }
    }

    suspend fun clearUserAccount() {
        persistence.updateData { UserAccount.empty() } //proba
    }







}