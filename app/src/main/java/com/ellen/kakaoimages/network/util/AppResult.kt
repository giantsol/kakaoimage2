package com.ellen.kakaoimages.network.util

/**
 * AppResult class is a wrapper class that helps to handle success and failure scenarios with co routines
 */
sealed class AppResult<out T>(
) {
    class Success<T>(val data: T) : AppResult<T>()
    class Error(
        val message: String?
    ) : AppResult<Nothing>()

    class Loading<T> : AppResult<T>()
//    data class Success<out T>(val successData : T) : AppResult<T>()
//    class Error(val exception: java.lang.Exception, val message: String = exception.localizedMessage)
//        : AppResult<Nothing>()

}