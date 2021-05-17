package com.juanmacapuano.appmapeo.tools

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun View.bindVisible(visible : Boolean?) {
    visibility = if (visible == true) View.VISIBLE else View.GONE
}