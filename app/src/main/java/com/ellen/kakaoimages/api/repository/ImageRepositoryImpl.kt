package com.ellen.kakaoimages.api.repository

import com.ellen.kakaoimages.api.ImageApi
import com.ellen.kakaoimages.data.ImagesResponse
import com.ellen.kakaoimages.api.util.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class ImageRepositoryImpl(
    private val api: ImageApi
) :
    ImageRepository {
    companion object {
        private const val DEFAULT_SIZE = 50
    }

    override suspend fun fetchImages(q: String, page: Int): NetworkState<ImagesResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.fetchImages(q, page, DEFAULT_SIZE)
                if (response.isSuccessful) {
                    response.body()?.let {
                         NetworkState.Success(it)
                    } ?: NetworkState.Failure("Response Error")

                } else {
                    NetworkState.Failure(response.errorBody()?.string() ?: "API Error")
                }
            } catch (e: Exception) {
                NetworkState.Failure(e.message ?: "Internet Error")
            }
        }
    }
}