package com.example.kakaosearchapi

import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("meta")
    var meta: Meta,
    @SerializedName("documents")
    var documents: List<Items>
)

data class Meta(
    @SerializedName("total_count")
    var totalCount: Int = 0,
    @SerializedName("pageable_count")
    var pageableCount: Int = 0
)

data class Items(
    @SerializedName("datetime")
    var datetime: String = "",
    @SerializedName("display_sitename")
    var sitename: String = "",
    @SerializedName("doc_url")
    var docUrl: String = "",
    @SerializedName("image_url")
    var imageUrl: String = "",
    @SerializedName("thumbnail_url")
    var thumbnailUrl: String = ""
)