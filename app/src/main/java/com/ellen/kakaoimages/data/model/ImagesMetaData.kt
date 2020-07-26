package com.ellen.kakaoimages.data.model

import com.google.gson.annotations.SerializedName

data class ImagesMetaData(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("is_end")
    val isEnd: Boolean
)