package com.ellen.kakaoimages.paging.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PageKeyedDataSource
import com.ellen.kakaoimages.data.model.ImagesDocuments
import com.ellen.kakaoimages.network.repository.ImageRepository
import com.ellen.kakaoimages.network.util.NetworkState
import com.ellen.kakaoimages.viewmodel.ImageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProjectDataSource(
    private val repository: ImageRepository,
    private val query: String,
    private val scope: CoroutineScope
) : PageKeyedDataSource<Int, ImagesDocuments>() {
    // FOR DATA ---
    private val networkState = MutableLiveData<NetworkState<Int>>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ImagesDocuments>
    ) {
        executeQuery(1, params.requestedLoadSize) {
            callback.onResult(it, null, 2)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ImagesDocuments>) {
        executeQuery(params.key, params.requestedLoadSize) {
            callback.onResult(it, params.key + 1)
        }
    }

    // UTILS ---
    private fun executeQuery(page: Int, perPage: Int, callback: (List<ImagesDocuments>) -> Unit) {
        networkState.postValue(NetworkState.Loading())
        scope.launch {
            try {
                val response = repository.fetchUsers(query, page)
                if (response.isSuccessful) {
                    val items = response.body()?.documents
                    if (items?.size!! >= 0) {
                        networkState.postValue(NetworkState.Success())
                        callback(items)
                    }
                } else networkState.postValue(NetworkState.Error())
            } catch (exception: HttpException) {
                networkState.postValue(NetworkState.Error())
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ImagesDocuments>) {
    }

    // PUBLIC API ---
    fun getNetworkState(): LiveData<NetworkState<Int>> = networkState
    fun refresh() =
        this.invalidate()
}