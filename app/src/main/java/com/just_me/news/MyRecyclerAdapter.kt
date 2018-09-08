package com.just_me.news

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.just_me.news.core.extension.inflate
import com.just_me.news.core.extension.loadFromUrl
import com.just_me.news.news.R
import kotlinx.android.synthetic.main.item_recycler.view.*

class MyRecyclerAdapter(val items: List<RecyclerData>): RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_recycler))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        holder.itemView.tvName.text = data.title
        holder.itemView.imageView.loadFromUrl(data.image)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {

        }
    }
}