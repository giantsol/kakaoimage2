package com.ellen.kakaoimages.views.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.ellen.kakaoimages.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class UserBaseFragment : Fragment() {
    abstract fun onSearch()
    abstract fun onClear()

    protected fun setupEditText(ed: EditText) {

        ed.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                val clearIcon = if (editable?.isNotEmpty() == true) R.drawable.ic_clear else 0
                ed.setCompoundDrawablesWithIntrinsicBounds(0, 0, clearIcon, 0)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int){
                var job: Job? = null
                ed.addTextChangedListener { editable ->
                    job?.cancel()
                    job = MainScope().launch {
                        delay(500L)
                        editable?.let {
                            if (editable.toString().isNotEmpty()) {
                                //init
                                onSearch()
                            }
                        }
                    } //end job
                } //end textlistener
            }
        })

        ed.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= ((v as EditText).right - v.compoundPaddingRight)) {
                    v.setText("")
                    onClear()
                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })
    }

}