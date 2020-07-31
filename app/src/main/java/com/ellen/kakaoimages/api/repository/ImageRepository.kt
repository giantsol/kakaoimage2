package com.ellen.kakaoimages.api.repository

import com.ellen.kakaoimages.data.ImagesResponse
import com.ellen.kakaoimages.api.util.NetworkState

interface ImageRepository {
    suspend fun fetchImages(q: String, page: Int): NetworkState<ImagesResponse>
}
