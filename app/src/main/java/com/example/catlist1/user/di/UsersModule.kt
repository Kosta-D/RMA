package com.example.catlist1.user.di


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import com.example.catlist1.user.domain.UserAccount
import com.example.catlist1.user.account.UserAccountSerializer
import com.example.catlist1.user.account.UserAccountStore
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UsersModule {

    @Provides
    @Singleton
    fun provideUserAccountDataStore(
        @ApplicationContext context: Context
    ): DataStore<UserAccount?> {
        return DataStoreFactory.create(
            storage = OkioStorage(
                fileSystem = FileSystem.SYSTEM,
                serializer = UserAccountSerializer(),
                producePath = {
                    (context.filesDir.resolve("user_account.json")).toOkioPath()
                }
            )
        )
    }

    @Provides
    @Singleton
    fun provideUserAccountStore(dataStore: DataStore<UserAccount?>): UserAccountStore {
        return UserAccountStore(dataStore)
    }

}