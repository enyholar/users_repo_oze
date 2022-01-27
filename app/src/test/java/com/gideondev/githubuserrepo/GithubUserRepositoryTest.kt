package com.gideondev.githubuserrepo

import com.gideondev.githubuserrepo.data.persistence.db.UserDao
import com.gideondev.githubuserrepo.data.remote.GitHubApiService
import com.gideondev.githubuserrepo.model.User
import com.gideondev.githubuserrepo.repository.GitHubUserRepository
import com.gideondev.githubuserrepo.test_common.MockTestUtil
import com.gideondev.githubuserrepo.test_common.TestCompletable
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import org.mockito.Mock


class GithubUserRepositoryTest {

    lateinit var githubUserRepository: GitHubUserRepository

    @Mock
    lateinit var gitHubApiService: GitHubApiService

    @Mock
    lateinit var userDao: UserDao
    private val revokeCompletable = TestCompletable()
    @Before
    fun setup() {

        MockitoAnnotations.initMocks(this)
        gitHubApiService =  mockk<GitHubApiService>()
        userDao =  mockk<UserDao>()
    }

    @Test
    fun test_searchForUsers_gives_list_of_users() {
        val givenSearchedUserResponse = MockTestUtil.createSearchResponse(30)
        githubUserRepository = GitHubUserRepository(userDao,gitHubApiService)
        val searchUserResponseSingle = Single.just(givenSearchedUserResponse)

        coEvery { gitHubApiService.searchForUser(any(),any()) }
            .returns(searchUserResponseSingle)

        val apiResponseFlow = githubUserRepository.searchForUser("lagos",1)
        val value = apiResponseFlow.blockingGet()

        // Then
        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(value.items?.size, CoreMatchers.`is`(
            givenSearchedUserResponse.items?.size))
        MatcherAssert.assertThat(value.incompleteResults, CoreMatchers.`is`(
            givenSearchedUserResponse.incompleteResults))
    }

    @Test
    fun `test searchForUsers() gives empty list of user`(){
        val givenSearchedUserResponse = MockTestUtil.createSearchResponse(0)
        githubUserRepository = GitHubUserRepository(userDao,gitHubApiService)
        val searchUserResponseSingle = Single.just(givenSearchedUserResponse)

        coEvery { gitHubApiService.searchForUser(any(),any()) }
            .returns(searchUserResponseSingle)

        val apiResponseFlow = githubUserRepository.searchForUser("lagos",1)
        val value = apiResponseFlow.blockingGet()

        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(value.items?.size, CoreMatchers.`is`(
            givenSearchedUserResponse.items?.size))

        coVerify(exactly = 1) { gitHubApiService.searchForUser(any(), any())  }
        confirmVerified(gitHubApiService)
    }

    @Test
    fun `test save user to db`(){
        val givenSearchedUserResponse = MockTestUtil.createSearchResponse(30)
        githubUserRepository = GitHubUserRepository(userDao,gitHubApiService)

        coEvery { userDao.insertUser (any()) }
            .returns(revokeCompletable)

        val apiResponseFlow = givenSearchedUserResponse.items?.first()
            ?.let { githubUserRepository.insertUser(it) }

        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())
        coVerify(exactly = 1) { githubUserRepository.insertUser(any())  }
        confirmVerified(userDao)
    }

    @Test
    fun `test delete a  user in db`(){
        githubUserRepository = GitHubUserRepository(userDao,gitHubApiService)

        coEvery { userDao.deleteUser (any()) }
            .returns(revokeCompletable)

        val apiResponseFlow = githubUserRepository.deleteUser(374648)

        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())
        coVerify(exactly = 1) { githubUserRepository.deleteUser(any())  }
        confirmVerified(userDao)
    }

    @Test
    fun `test delete all user`(){
        githubUserRepository = GitHubUserRepository(userDao,gitHubApiService)

        coEvery { userDao.deleteAllUsers () }
            .returns(revokeCompletable)

        val apiResponseFlow = githubUserRepository.deleteUser(374648)

        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())
        coVerify(exactly = 1) { githubUserRepository.deleteAllUsers()  }
        confirmVerified(userDao)
    }

    @Test
    fun `test get all user`(){
        val givenSearchedUserResponse = MockTestUtil.createSearchResponse(30)
        val userList = givenSearchedUserResponse.items
        var tes: MutableList<User> = ArrayList()
        if (userList != null) {
            userList.forEach{
                tes.add(it!!)
            }
        }
        val flowableUserList:  Flowable<List<User>> = Flowable.just(tes)
        githubUserRepository = GitHubUserRepository(userDao,gitHubApiService)
        coEvery { userDao.getAllUsers () }
            .returns(flowableUserList)

        val apiResponseFlow = githubUserRepository.getAllUsers()
        val value = apiResponseFlow.blockingFirst()

        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(value.size, CoreMatchers.`is`(
            givenSearchedUserResponse.items?.size))
        coVerify(exactly = 1) { githubUserRepository.getAllUsers()  }
        confirmVerified(userDao)
    }

}

