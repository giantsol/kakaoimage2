package com.ellen.kakaoimages.util

import androidx.lifecycle.LiveData
import java.util.*
import kotlin.collections.HashSet

/**
 * Hash Set Live Data
 *
 * @author https://stackoverflow.com/a/54770540
 */
class SortedSetLiveData<V> : LiveData<SortedSet<V>>() {


    fun add(v: V) {
        val oldData = value
        value = if (oldData == null) {
            sortedSetOf(v)
        } else {
            oldData.add(v)
            oldData
        }

//        value = if (oldData == null) {
//
////            hashSetOf(v)
////            var tmp : SortedSet<V> = hashSetOf(v).toSortedSet()
//        } else {
//            oldData.add(v)
//            oldData
//        }
    }

    fun addAll(newData: SortedSet<V>) {
        val oldData = value
        value = if (oldData != null) {
            oldData.addAll(newData)
            oldData
        } else {
            newData
        }
    }


    fun clear() {
        val oldData = value
        if (oldData != null) {
            oldData.clear()
            value = oldData
        }
    }

    fun isEmpty(): Boolean {
        val oldData = value
        return oldData?.isEmpty() ?: true
    }

    var value: SortedSet<V>?
        set(value) = super.setValue(value)
        get() = super.getValue()

}
