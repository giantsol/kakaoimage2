package com.ellen.kakaoimages.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


fun Activity.hideKeyboard() {
    // View()를 생성해서 하는건 좋지않고 hideKeyboard() 파라미터로 MainActivity에 있는 view 아무거나 넘기는게 좋겠다.
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}