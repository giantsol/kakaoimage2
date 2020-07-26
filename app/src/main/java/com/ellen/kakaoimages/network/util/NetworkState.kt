package com.ellen.kakaoimages.network.util

sealed class NetworkState<T>(    val data: T? = null,
                                 val message: String? = null) {
    class Success<T> : NetworkState<T>()
    class Loading<T> : NetworkState<T>()
    class Error<T>() : NetworkState<T>()

}
