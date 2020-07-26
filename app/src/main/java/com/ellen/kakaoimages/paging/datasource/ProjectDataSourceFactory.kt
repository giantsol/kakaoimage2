package com.ellen.kakaoimages.paging.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import com.ellen.kakaoimages.data.model.ImagesDocuments
import com.ellen.kakaoimages.network.repository.ImageRepository
import kotlinx.coroutines.CoroutineScope

class ProjectDataSourceFactory(private val repository: ImageRepository,
                               private val scope: CoroutineScope,
                               private var query: String = ""
) : DataSource.Factory<Int, ImagesDocuments>()
{
    val dataSource = MutableLiveData<ProjectDataSource>()

    override fun create() : DataSource<Int, ImagesDocuments> {

        val dataSource = ProjectDataSource(repository,query , scope)
        this.dataSource.postValue(dataSource)
        return dataSource
    }


    fun updateQuery(query: String) {
        this.query = query
        dataSource.value?.refresh()
    }
}