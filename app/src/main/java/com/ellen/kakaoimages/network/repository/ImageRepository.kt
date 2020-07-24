package com.ellen.kakaoimages.network.repository

import com.ellen.kakaoimages.data.model.ImagesResponse
import com.ellen.kakaoimages.network.util.AppResult

interface ImageRepository {
    suspend fun getAllUsers(): AppResult<ImagesResponse>
    suspend fun fetchUsers(q: String, page: Int): AppResult<ImagesResponse>
}
