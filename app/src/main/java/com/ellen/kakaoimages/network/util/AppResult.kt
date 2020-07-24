package com.ellen.kakaoimages.network.util

sealed class AppResult<out T>(
) {
    class Success<T>(val data: T) : AppResult<T>()
    class Error(val message: String?) : AppResult<Nothing>()

}