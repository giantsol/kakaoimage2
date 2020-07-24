package com.ellen.kakaoimages.data.model

import com.ellen.kakaoimages.data.model.ImagesMetaData


data class ImagesResponse(
    val documents: List<ImagesDocuments>,
    val meta: ImagesMetaData
)