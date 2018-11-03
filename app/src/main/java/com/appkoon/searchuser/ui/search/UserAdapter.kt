package com.appkoon.searchuser.ui.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import com.appkoon.searchuser.R
import com.appkoon.searchuser.model.vo.Item
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*


class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val items = mutableListOf<Item>()
    private lateinit var context: Context
    var onListItemClickListener: ((Item, Int) -> Unit)? = null

    companion object {
        const val FADE_DURATION = 500L
    }

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
            imageLike.visibility = if (items[position].like) View.VISIBLE else View.INVISIBLE
        }
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


    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            with(view) {
                cardView.setOnClickListener { _ ->
                    onListItemClickListener?.let { it(items[adapterPosition], adapterPosition) }
                }
            }
        }
    }

}


