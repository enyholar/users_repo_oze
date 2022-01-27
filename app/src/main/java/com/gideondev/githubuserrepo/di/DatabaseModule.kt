package com.gideondev.githubuserrepo.di

import android.content.Context
import androidx.room.Room
import com.gideondev.githubuserrepo.data.persistence.db.GitHubUserDatabase
import com.gideondev.githubuserrepo.utils.Constants.API.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providePokemonDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        GitHubUserDatabase::class.java,
        DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .allowMainThreadQueries()
        .build()

    @Provides
    @Singleton
    fun provideUserDao(db: GitHubUserDatabase) = db.userDao()
}