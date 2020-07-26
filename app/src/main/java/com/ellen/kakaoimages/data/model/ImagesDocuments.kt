package com.ellen.kakaoimages.data.model

import com.google.gson.annotations.SerializedName

data class ImagesDocuments(
    val collection: String,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("display_sitename")
    val displaySitename: String,
    @SerializedName("doc_url")
    val docUrl: String,
    val datetime: String
)