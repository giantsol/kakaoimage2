package com.ellen.kakaoimages.data.model


data class ImagesResponse(
    val documents: List<ImagesDocuments>,
    val meta: ImagesMetaData
)