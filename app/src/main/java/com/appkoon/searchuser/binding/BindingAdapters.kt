package com.appkoon.searchuser.binding

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.appkoon.searchuser.ui.common.ItemAdapter
import com.appkoon.searchuser.vo.Item
import com.bumptech.glide.Glide

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("setItemLike")
    fun bindItemLike(recyclerView: RecyclerView, items: List<Item>) {
        val adapter = recyclerView.adapter as ItemAdapter
        if(adapter.itemCount == 0) adapter.setData(items)
    }
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        Glide.with(imageView.context).load(url).into(imageView)
    }



}