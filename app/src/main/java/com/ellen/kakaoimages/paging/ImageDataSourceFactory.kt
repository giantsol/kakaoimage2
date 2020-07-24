package com.ellen.kakaoimages.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ellen.kakaoimages.data.model.ImagesDocuments
import com.ellen.kakaoimages.network.repository.ImageRepository
import kotlinx.coroutines.CoroutineScope


class RecipeDataSourceFactory(
    private val repository: ImageRepository,
    private var query: String = "",
    private val scope: CoroutineScope
) : DataSource.Factory<Int, ImagesDocuments>() {

    private val source = MutableLiveData<ImageDataSource>()

    override fun create(): DataSource<Int, ImagesDocuments> {
        val source = ImageDataSource(repository, query, scope)
        this.source.postValue(source)
        return source
    }

    fun getSource() = source.value

}