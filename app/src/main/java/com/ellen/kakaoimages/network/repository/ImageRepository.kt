package com.ellen.kakaoimages.network.repository

import com.ellen.kakaoimages.data.model.ImagesResponse
import com.ellen.kakaoimages.network.util.NetworkState

interface ImageRepository {
    suspend fun fetchImages(q: String, page: Int): NetworkState<ImagesResponse>
}
