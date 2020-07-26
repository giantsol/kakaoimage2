package com.ellen.kakaoimages.viewmodel

import androidx.lifecycle.LiveData
import com.ellen.kakaoimages.network.repository.ImageRepository
import com.ellen.kakaoimages.network.util.NetworkState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ellen.kakaoimages.paging.datasource.ProjectDataSourceFactory
import com.ellen.kakaoimages.data.model.ImagesDocuments
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ImageViewModel(private val repository: ImageRepository) : ViewModel() {

    val showLoading = MutableLiveData<Boolean>()
    var showError = MutableLiveData<String>()
    private val _filter = MutableLiveData<String>()
    val filter: LiveData<String>
        get() = _filter
    private val networkState = MutableLiveData<NetworkState<Int>>()


    val searchQuery = MutableLiveData<String>()
    private val _selected = MutableLiveData<ImagesDocuments>()
    fun select(item: ImagesDocuments) {
        _filter.postValue(item.collection)
        _selected.postValue(item)
    }

    private val _isSearchFrgShowing = MutableLiveData<Boolean>()
    val isSearchFrgShowing: LiveData<Boolean>
        get() = _isSearchFrgShowing

    fun showDetailFragment() {
        _isSearchFrgShowing.postValue(false)
    }


    val config = PagedList.Config.Builder()
        .setInitialLoadSizeHint(20)
        .setPageSize(50)
        .setPrefetchDistance(5)
        .setEnablePlaceholders(true)
        .build()

    val userList = MutableLiveData<List<ImagesDocuments>>()

    /**
     * PAGING STARt
     */
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val projectsDataSource = ProjectDataSourceFactory(repository,ioScope)
    val projects = LivePagedListBuilder(projectsDataSource, config).build()

    fun fetchImages(){
        projectsDataSource.updateQuery(searchQuery.value.toString())
    }


    /**
     * PAGING END
     */

//    fun fetchImages() {
//            showLoading.value = true
//            viewModelScope.launch {
//                val result = repository.fetchUsers(searchQuery.value.toString(), page)
//                showLoading.postValue(false)
//                when (result) {
//                    is NetworkState.Success -> {
//                        val data = result.data.documents
//                        if (!data.isNullOrEmpty()) {
////                            var filter =HashSet<String>()
////                            for(item in data){
////                                filter.add(item.collection)
////                            }
//                            userList.postValue(data)
//                            showError.postValue(null)
//                        } else {
////                                showError.postValue("Result is empty")
//                        }
//                    }
//                    is NetworkState.Error -> showError.postValue(result.message)
//                }
//            }
//    }

    fun init() {
        showError.value = null
        showLoading.value = false
    }
}