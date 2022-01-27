package com.gideondev.githubuserrepo.data.persistence.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gideondev.githubuserrepo.model.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User) : Completable

    @Query("delete from User where id =:id")
    fun deleteUser(id: Int) : Completable

    @Query("delete  from user")
    fun deleteAllUsers() : Completable

    @Query("select * from user")
    fun getAllUsers(): Flowable<List<User>>
}