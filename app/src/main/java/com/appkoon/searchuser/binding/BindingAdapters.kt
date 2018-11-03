package com.appkoon.searchuser.binding

import android.databinding.BindingAdapter
import android.view.View
import com.facebook.drawee.view.SimpleDraweeView

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }
    @JvmStatic
    @BindingAdapter("imageUrlFresco")
    fun bindImage(imageView: SimpleDraweeView, url: String?) {
        imageView.setImageURI(url)
    }
}