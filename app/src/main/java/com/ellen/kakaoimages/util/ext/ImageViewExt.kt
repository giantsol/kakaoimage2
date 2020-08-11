package com.ellen.kakaoimages.util.ext

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ellen.kakaoimages.R

@BindingAdapter("bind:setSrc")
fun ImageView.setSrc( url: String?) {
    url?.let {
        Glide.with(context)
            .load(it)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(this)
    }
}

