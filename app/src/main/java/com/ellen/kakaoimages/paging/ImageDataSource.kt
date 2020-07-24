package com.ellen.kakaoimages.paging

import androidx.paging.PageKeyedDataSource
import com.ellen.kakaoimages.data.model.ImagesDocuments
import com.ellen.kakaoimages.network.repository.ImageRepository
import com.ellen.kakaoimages.network.util.AppResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ImageDataSource(
    private val repository: ImageRepository,
    private val query: String,
    private val scope: CoroutineScope
) :
    PageKeyedDataSource<Int, ImagesDocuments>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ImagesDocuments>
    ) {
        requestLaunch(1, callback, null)
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ImagesDocuments>
    ) {
        requestLaunch(params.key + 1, null, callback)
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ImagesDocuments>
    ) {
    }


    private fun requestLaunch(
        page: Int,
        loadInitialCallback: LoadInitialCallback<Int, ImagesDocuments>?,
        callback: LoadCallback<Int, ImagesDocuments>?
    ) {

        scope.launch {
//            val recipes = repository.searchRecipesWithPagination(query, page)
            val result = repository.fetchUsers(query, page)
            when (result) {
                is AppResult.Success -> {
                    val data = result.data.documents
//                            var filter =HashSet<String>()
//                            for(item in data){
//                                filter.add(item.collection)
//                            }
                    callback?.onResult(data, page)
                    loadInitialCallback?.onResult(data, null, page)
                }
                is AppResult.Error -> "error"
            }
        }

    }
}