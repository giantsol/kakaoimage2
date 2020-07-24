package com.ellen.kakaoimages.data.model

data class ImagesDocuments(
    val collection: String,
    val thumbnail_url: String,
    val image_url: String,
    val display_sitename: String,
    val doc_url: String,
    val datetime: String
)