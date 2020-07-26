package com.ellen.kakaoimages.viewmodel

import androidx.lifecycle.LiveData
import com.ellen.kakaoimages.network.repository.ImageRepository
import com.ellen.kakaoimages.network.util.AppResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ellen.kakaoimages.data.model.ImagesDocuments
import kotlinx.coroutines.launch

class ImageViewModel(private val repository: ImageRepository) : ViewModel() {

    val showLoading = MutableLiveData<Boolean>()
    var showError = MutableLiveData<String>()
    private val _filter = MutableLiveData<String>()
    val filter: LiveData<String>
        get() = _filter


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

    private var page: Int = 1
    private var isFinished: Boolean = false


    val userList = MutableLiveData<List<ImagesDocuments>>()


    fun fetchImages() {
        if (!isFinished) {
            showLoading.value = true
            viewModelScope.launch {
                val result = repository.fetchUsers(searchQuery.value.toString(), page)
                showLoading.postValue(false)
                when (result) {
                    is AppResult.Success -> {
                        val data = result.data.documents
                        if (!data.isNullOrEmpty()) {
//                            var filter =HashSet<String>()
//                            for(item in data){
//                                filter.add(item.collection)
//                            }
                            page++
                            userList.postValue(data)
                            showError.postValue(null)
                        } else {
                            isFinished = true
                            if (page == 1)
                                showError.postValue("Result is empty")
                        }
                    }
                    is AppResult.Error -> showError.postValue(result.message)
                }
            }
        }//end isFinished
    }

    fun init() {
        showError.value = null
        showLoading.value = false
        page = 1
        isFinished = false
    }
}