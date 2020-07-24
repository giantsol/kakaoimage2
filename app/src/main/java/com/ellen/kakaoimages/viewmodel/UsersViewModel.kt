package com.ellen.kakaoimages.viewmodel

import androidx.lifecycle.LiveData
import com.ellen.kakaoimages.network.repository.ImageRepository
import com.ellen.kakaoimages.network.util.AppResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ellen.kakaoimages.data.model.ImagesDocuments
import kotlinx.coroutines.launch

class UsersViewModel(private val repository: ImageRepository) : ViewModel() {

    val showLoading = MutableLiveData<Boolean>()
    var showError = MutableLiveData<String>()

    val searchQuery = MutableLiveData<String>()

    private var page: Int = 1
    private var isFinished: Boolean = false

    val config = PagedList.Config.Builder()
        .setInitialLoadSizeHint(20)
        .setPageSize(50)
        .setPrefetchDistance(5)
        .setEnablePlaceholders(true)
        .build()

    val userList = MutableLiveData<List<ImagesDocuments>>()


    fun fetchUsers() {
        if (!isFinished) {
            showLoading.value = true
            viewModelScope.launch {
                val result = repository.fetchUsers(searchQuery.value.toString(), page)
                showLoading.postValue(false)
                when (result) {
                    is AppResult.Success -> {
                        val data = result.data.documents
                        if (!data.isNullOrEmpty()) {
                            page++
                            userList.postValue(data)
                            showError.postValue(null)
                        } else {
                            isFinished = true
                            if (page==1)
                                showError.postValue("Result is Empty")
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