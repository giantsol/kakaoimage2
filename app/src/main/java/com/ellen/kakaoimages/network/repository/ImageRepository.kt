package com.ellen.kakaoimages.network.repository

import androidx.lifecycle.MutableLiveData
import com.ellen.kakaoimages.network.ImageApi
import com.ellen.kakaoimages.network.util.NetworkState
import com.ellen.kakaoimages.data.model.ImagesResponse
import com.ellen.kakaoimages.network.util.NetworkState.Success
import retrofit2.Response

class ImageRepository(
    private val api: ImageApi
)  {
    companion object {
        private const val DEFAULT_SIZE = 50
        private val networkState = MutableLiveData<NetworkState<Int>>()

    }

//     suspend fun fetchUsers(q: String, page: Int): Response<ImagesResponse> {
//        return try {
//
//            val response = api.fetchImages(q,page, DEFAULT_SIZE)
////            if(response.isSuccessful) {
////                val items = response.body()?.documents
////                if(items?.size!! >= 0) {
////                    networkState.postValue(NetworkState.Success())
////                    callback(items)
////                }
////            }
//
////            val response = api.fetchImages("cat", 1, DEFAULT_SIZE)
//            response
//        } catch (e: Exception) {
//            NetworkState.Error(e.message.toString())
//        }
//    }

    suspend fun fetchUsers(q: String, page: Int) = api.fetchImages(q,page, DEFAULT_SIZE)


}