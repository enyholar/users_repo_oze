package com.gideondev.githubuserrepo.repository
import com.gideondev.githubuserrepo.data.persistence.db.UserDao
import com.gideondev.githubuserrepo.data.remote.GitHubApiService
import com.gideondev.githubuserrepo.model.User
import javax.inject.Inject

class GitHubUserRepository
@Inject
constructor(
    val userDao: UserDao,
    val apiService: GitHubApiService
) {
    fun searchForUser(query: String, page: Int ) = apiService.searchForUser(query, page)

    fun insertUser(user: User) = userDao.insertUser(user)

    fun deleteUser(id: Int) = userDao.deleteUser(id)

    fun deleteAllUsers() =userDao.deleteAllUsers()

    fun getAllUsers() =userDao.getAllUsers()
}