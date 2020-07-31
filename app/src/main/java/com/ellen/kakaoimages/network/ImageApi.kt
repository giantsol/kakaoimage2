package com.ellen.kakaoimages.network

import com.ellen.kakaoimages.data.ImagesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {
    @GET("/v2/search/image")
    suspend fun fetchImages(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<ImagesResponse>

}