package com.ellen.kakaoimages.data

import com.ellen.kakaoimages.data.model.ImagesDocuments
import com.ellen.kakaoimages.data.model.ImagesMetaData


data class ImagesResponse(
    val documents: List<ImagesDocuments>,
    val meta: ImagesMetaData
)