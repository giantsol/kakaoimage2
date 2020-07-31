package com.ellen.kakaoimages.network.repository

import com.ellen.kakaoimages.data.ImagesResponse
import com.ellen.kakaoimages.network.util.NetworkState

interface ImageRepository {
    suspend fun fetchImages(q: String, page: Int): NetworkState<ImagesResponse>
}
