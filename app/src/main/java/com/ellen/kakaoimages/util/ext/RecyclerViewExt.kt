package com.ellen.kakaoimages.util.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ellen.kakaoimages.ui.adapter.ImageListAdapter

@BindingAdapter("bind:setCurrFilter")
fun RecyclerView.setCurrFilter(filter: String?) {
    (adapter as? ImageListAdapter)?.setCurrFilter(filter ?: "ALL")
}