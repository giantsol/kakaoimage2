package com.ellen.kakaoimages.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ellen.kakaoimages.R

@BindingAdapter("android:src")
fun setSrc(v: ImageView, url: String?) {
    url?.let {
        Glide.with(v.context)
            .load(it)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(v)
    }
}

