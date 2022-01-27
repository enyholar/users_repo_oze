package com.gideondev.githubuserrepo.data.remote

import com.gideondev.githubuserrepo.model.SearchResultResponse
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApiService {

    @GET("search/users")
    fun searchForUser(
        @Query("q") query: String,
        @Query("page") page: Int,
    ): Single<SearchResultResponse>

}