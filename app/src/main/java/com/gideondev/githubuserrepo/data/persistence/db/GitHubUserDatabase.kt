package com.gideondev.githubuserrepo.data.persistence.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gideondev.githubuserrepo.model.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class GitHubUserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}