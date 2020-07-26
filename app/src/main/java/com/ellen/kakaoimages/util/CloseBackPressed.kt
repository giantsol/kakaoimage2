package com.ellen.kakaoimages.util

import android.content.Context
import android.widget.Toast
import com.ellen.kakaoimages.R

private var FINISH_INTERVAL_TIME: Long = 2000
private var backPressedTime: Long = 0

fun closeBackPressed(context: Context): Boolean {
    val tempTime = System.currentTimeMillis()
    val intervalTime = tempTime - backPressedTime
    return if (intervalTime in 0..FINISH_INTERVAL_TIME) {
        true
    } else {
        backPressedTime = tempTime
        Toast.makeText(context, R.string.close_back_pressed, Toast.LENGTH_SHORT).show()
        false
    }
}