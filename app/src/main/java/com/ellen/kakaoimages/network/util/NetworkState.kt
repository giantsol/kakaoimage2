package com.ellen.kakaoimages.network.util

sealed class NetworkState<out T: Any> {
    class Success<out T:Any>(val data: T) : NetworkState<T>()
    class Failure(message: String) : NetworkState<Nothing>()
}