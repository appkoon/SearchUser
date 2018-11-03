package com.appkoon.searchuser.ui.common

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import com.appkoon.searchuser.R
import com.appkoon.searchuser.databinding.ItemUserBinding
import com.appkoon.searchuser.vo.Item
import kotlinx.android.synthetic.main.item_user.view.*


class ItemAdapter: RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val items = mutableListOf<Item>()
    private lateinit var context: Context
    var onListItemClickListener: ((Item, Int) -> Unit)? = null

    companion object {
        const val FADE_DURATION = 500L
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        context = parent.context
        val binding: ItemUserBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_user, parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
        viewHolder.binding.item = items[position]
        setFadeAnimation(viewHolder.itemView)
    }

    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = FADE_DURATION
        view.startAnimation(anim)
    }

    fun clear(){
        this.items.clear()
        notifyDataSetChanged()
    }

    fun setData(items: List<Item>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun checkLike(position: Int) {
        items[position].like = true
        notifyItemChanged(position)
    }

    fun checkUnlike(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun checkUnlike(data: Item) {
        val id = data.id
        items.forEachIndexed{ index, item ->
            if (item.id == id) item.like = false
            notifyItemChanged(index)
        }
    }

    inner class ItemViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            with(binding.root) {
                cardView.setOnClickListener { _ ->
                    onListItemClickListener?.let { it(items[adapterPosition], adapterPosition) }
                }
            }
        }
    }

}


