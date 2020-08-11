package com.ellen.kakaoimages.ui.view

import com.ellen.kakaoimages.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ellen.kakaoimages.databinding.ActivityMainBinding
import com.ellen.kakaoimages.util.*
import com.ellen.kakaoimages.ui.viewmodel.ImageViewModel
import com.ellen.kakaoimages.ui.adapter.ImageListAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val vm: ImageViewModel by viewModel()
    private lateinit var mViewDataBinding: ActivityMainBinding
    private lateinit var imageListAdapter: ImageListAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private var job: Job? = null

    private var beforeSelected = 0    //for Check Spinner Changed


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mViewDataBinding.viewModel = vm
        mViewDataBinding.lifecycleOwner = this

        /**
         * SPINNER
         */
        setupFilter()
        /**
         * RecyclerView
         */
        setUpRecyclerView()
        /**
         * EditText
         */
        setupEditText()

        mViewDataBinding.viewModel = vm
        vm.imageList.observe(this, Observer {
            if (it.isNotEmpty() && it != null) {
                imageListAdapter.setImages(it)
            }
        })

    }

    private fun initPage() {
        vm.init()
        imageListAdapter.clear()
        scrollListener.resetState()
        beforeSelected = 0
        job = null
    }

    private fun setUpRecyclerView() {
        val linearLayoutManager = GridLayoutManager(this, 2)
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            var isKeyboardDismissedByScroll = false

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                vm.fetchImages(page)
            }

            //Hide Keyboard when scroll Dragging
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (!isKeyboardDismissedByScroll) {
                        hideKeyboard()
                        isKeyboardDismissedByScroll = !isKeyboardDismissedByScroll
                    }
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isKeyboardDismissedByScroll = false
                }
            }
        }

        imageListAdapter = ImageListAdapter()
        rv_images.apply {
            layoutManager = linearLayoutManager
            addOnScrollListener(scrollListener)
            rv_images.adapter = imageListAdapter
        }

    }

    private fun setupEditText() {

        ed_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                val clearIcon = if (editable?.isNotEmpty() == true) R.drawable.ic_clear else 0
                ed_search.setCompoundDrawablesWithIntrinsicBounds(0, 0, clearIcon, 0)
                job?.cancel()
                job = MainScope().launch {
                    delay(500L)
                    editable?.let {
                        if (editable.toString().isNotEmpty()) {
                            //init
                            initPage()
                            vm.fetchImages(1)
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        })

        ed_search.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
                if (event.rawX >= ((v as EditText).right - v.compoundPaddingRight)) {
                    v.setText("")
                    initPage()
                    return@OnTouchListener true
                }
            }
            return@OnTouchListener false
        })
    }

    private fun setupFilter() {
        vm.filter.observe(this, Observer {
            spinner.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, it.toTypedArray())
            spinner.setSelection(beforeSelected)
        })
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (beforeSelected != position) {
                    vm.setCurrFilter(spinner.selectedItem.toString())
                }

                beforeSelected = position
            }

        }
    }

    override fun onBackPressed() {
        if (closeBackPressed(this)) super.onBackPressed()
    }

}
