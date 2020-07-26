package com.ellen.kakaoimages.viewmodel

import com.ellen.kakaoimages.network.repository.ImageRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ellen.kakaoimages.data.model.ImagesDocuments
import com.ellen.kakaoimages.network.util.NetworkState
import com.ellen.kakaoimages.util.Constants.Companion.FILTER
import com.ellen.kakaoimages.util.SortedSetLiveData
import kotlinx.coroutines.launch

class ImageViewModel(private val repository: ImageRepository) : ViewModel() {

    val showLoading = MutableLiveData<Boolean>()
    var showError = MutableLiveData<String>()
    val searchQuery = MutableLiveData<String>()
    val filter = SortedSetLiveData<String>()


    private val recipeDataSource = ImageDataSourceFactory(repository = repository, scope = CoroutineScope(Dispatchers.Default)
    )

    fun fetchImages(page: Int) {
        showLoading.value = true
        viewModelScope.launch {
            val result = repository.fetchUsers(searchQuery.value.toString(), page)
            showLoading.postValue(false)
            when (result) {
                is NetworkState.Success -> {
                    val data = result.data.documents
                    if (!data.isNullOrEmpty()) {
                        for (item in data) {
                            if (filter.isEmpty())
                                filter.add("ALL")
                            filter.add(item.collection)
                        }
                        userList.postValue(data)
                        showError.postValue(null)
                    } else {
                        if (page == 1)
                            showError.postValue("Result is empty")
                    }
                }
                is NetworkState.Failure ->
                    showError.postValue(result.message)
            }
        }
    }




    val userList = MutableLiveData<List<ImagesDocuments>>()


//    fun fetchImages() {
//        if (!isFinished) {
//            showLoading.value = true
//            viewModelScope.launch {
//                val result = repository.fetchUsers(searchQuery.value.toString(), page)
//                showLoading.postValue(false)
//                when (result) {
//                    is AppResult.Success -> {
//                        val data = result.data.documents
//                        if (!data.isNullOrEmpty()) {
////                            var filter =HashSet<String>()
////                            for(item in data){
////                                filter.add(item.collection)
////                            }
//                            page++
//                            userList.postValue(data)
//                            showError.postValue(null)
//                        } else {
//                            isFinished = true
//                            if (page == 1)
//                                showError.postValue("Result is empty")
//                        }
//                    }
//                    is AppResult.Error -> showError.postValue(result.message)
//                }
//            }
//        }//end isFinished
//    }

    fun init() {
        showError.value = null
        showLoading.value = false
        FILTER = "ALL"
        filter.clear()
    }
}