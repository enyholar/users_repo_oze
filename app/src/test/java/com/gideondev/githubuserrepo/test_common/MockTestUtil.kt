package com.gideondev.githubuserrepo.test_common

import com.gideondev.githubuserrepo.model.SearchResultResponse
import com.gideondev.githubuserrepo.model.User

class MockTestUtil {
    companion object {
        fun createSearchResponse(count : Int): SearchResultResponse {
            return SearchResultResponse(
                incompleteResults = false,
                totalCount = 300,
                items = createUser(count)
                )
        }

        private fun createUser(count: Int): List<User> {
            return (0 until count).map {
                User(
                    gistsUrl = "",
                    login = "enyholar",
                    avatarUrl = "url",
                    id = 374648
                )
            }
        }

    }
}
