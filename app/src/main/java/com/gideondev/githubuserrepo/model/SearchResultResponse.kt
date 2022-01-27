package com.gideondev.githubuserrepo.model

import com.google.gson.annotations.SerializedName

data class SearchResultResponse(

	@field:SerializedName("total_count")
	val totalCount: Int? = null,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean? = null,

	@field:SerializedName("items")
	val items: List<User?>? = null
)

