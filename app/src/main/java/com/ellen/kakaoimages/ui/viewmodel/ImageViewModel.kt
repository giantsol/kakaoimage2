package com.ellen.kakaoimages.ui.viewmodel

import com.ellen.kakaoimages.network.repository.ImageRepository
import androidx.lifecycle.MutableLiveData
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
    val imageList = MutableLiveData<List<ImagesDocuments>>()

    fun fetchImages(page: Int) {
        showLoading.value = true
        viewModelScope.launch {
            val result = repository.fetchImages(searchQuery.value.toString(), page)
            showLoading.postValue(false)
            when (result) {
                is NetworkState.Success -> {
                    val data = result.data.documents
                    if (!data.isNullOrEmpty()) {
                        setFilter(data)
                        imageList.postValue(data)
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

    private fun setFilter(data: List<ImagesDocuments>) {
        for (item in data) {
            if (filter.isEmpty())
                filter.add("ALL")
            filter.add(item.collection)
        }
    }

    fun init() {
        showError.value = null
        showLoading.value = false
        FILTER = "ALL"
        filter.clear()
    }
}