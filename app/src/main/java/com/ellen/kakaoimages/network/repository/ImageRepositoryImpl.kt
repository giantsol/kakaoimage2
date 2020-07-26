package com.ellen.kakaoimages.network.repository

import com.ellen.kakaoimages.network.ImageApi
import com.ellen.kakaoimages.network.util.AppResult
import com.ellen.kakaoimages.network.util.Utils.handleApiError
import com.ellen.kakaoimages.network.util.Utils.handleSuccess
import android.content.Context
import com.ellen.kakaoimages.data.model.ImagesResponse

class ImageRepositoryImpl(
    private val api: ImageApi
) :
    ImageRepository {
    companion object {
        private const val DEFAULT_SIZE = 50
    }

    override suspend fun fetchUsers(q: String, page: Int): AppResult<ImagesResponse> {
        return try {
            val response = api.fetchImages(q, page, DEFAULT_SIZE)
            if (response.isSuccessful) {
                handleSuccess(response)
            } else {
                handleApiError(response)
            }
        } catch (e: Exception) {
            AppResult.Error(e.message)
        }
    }

}