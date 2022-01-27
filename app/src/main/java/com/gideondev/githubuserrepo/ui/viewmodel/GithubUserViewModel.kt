package com.gideondev.githubuserrepo.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.gideondev.githubuserrepo.model.SearchResultResponse
import com.gideondev.githubuserrepo.model.User
import com.gideondev.githubuserrepo.repository.GitHubUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class GithubUserViewModel
@Inject
constructor(
    private val userRepository: GitHubUserRepository,
) : BaseViewModel() {
    val searchResponseResult = MutableLiveData<SearchResultResponse>()
    val isLoading = MutableLiveData<Boolean>()
    val isNetworkUnavailable = MutableLiveData<Boolean>()
    val userAddedToFavoriteDb = MutableLiveData<Boolean>()
    val userRemoveFromFavoriteDb = MutableLiveData<Boolean>()
    val removeAllUserFromFavoriteDb = MutableLiveData<Boolean>()
    val favouriteUserList = MutableLiveData<List<User>>()

     fun searchForUser(q : String, page: Int) {
        isLoading.value = true
            disposable.add(
                userRepository.searchForUser(q,page)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        searchResponseResult.value = it
                        isLoading.value = false
                        isNetworkUnavailable.value = false
                    }, { t ->

                    })


            )
        }

     fun saveUserToFavoriteList(user: User) {
        disposable.add(
            userRepository.insertUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    userAddedToFavoriteDb.value = true
                }, { t ->
                    t.printStackTrace()
                })
        )
    }

     fun getAllFavoriteUsers() {
        disposable.add(
            userRepository.getAllUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ users ->
                    favouriteUserList.value = users
                }, { throwable ->
                    throwable.printStackTrace()
                })
        )
    }

    fun removeUserFromFavorite(id: Int) {
        disposable.add(
            userRepository.deleteUser(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    userRemoveFromFavoriteDb.value = true
                }, { throwable ->
                    throwable.printStackTrace()
                })
        )
    }

    fun removeAllUserFromFavorite() {
        disposable.add(
            userRepository.deleteAllUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    removeAllUserFromFavoriteDb.value = true
                }, { throwable ->
                    throwable.printStackTrace()
                })
        )
    }


}