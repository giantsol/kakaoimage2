package com.ellen.kakaoimages.network.util

sealed class NetworkState<out T> {
    class Success<T>(val data: T) : NetworkState<T>()
    class Failure(val message: String) : NetworkState<Nothing>()
}