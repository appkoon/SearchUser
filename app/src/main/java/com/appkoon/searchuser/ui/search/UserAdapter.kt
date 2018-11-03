package com.appkoon.searchuser.ui.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appkoon.searchuser.R
import com.appkoon.searchuser.model.vo.Item
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*


class UserAdapter: RecyclerView.Adapter<UserViewHolder>() {

    private val items = mutableListOf<Item>()
    private lateinit var context: Context
    var onListItemClickListener: ((Item, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(viewHolder: UserViewHolder, position: Int) {
        with(viewHolder.itemView) {
            Glide.with(context).load(items[position].avatar_url).into(imageUser)
            textUser.text = items[position].login
            cardView.setOnClickListener { _ ->
                onListItemClickListener?.let { it(items[viewHolder.adapterPosition], position) }
            }
            imageLike.visibility = if (items[position].like) View.VISIBLE else View.INVISIBLE
        }

    }

    fun setData(items: List<Item>, clear: Boolean) {
        if (clear) this.items.clear()
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

}


class UserViewHolder(view: View) : RecyclerView.ViewHolder(view)