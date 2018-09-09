package com.just_me.news

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.just_me.news.core.extension.inflate
import com.just_me.news.core.extension.loadFromUrl
import com.just_me.news.news.R
import kotlinx.android.synthetic.main.item_recycler.view.*

class MyRecyclerAdapter(val items: ArrayList<RecyclerData>): RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>() {
    lateinit var itemsToShow: ArrayList<RecyclerData>
    init {
        itemsToShow = ArrayList(items.size)
        itemsToShow.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_recycler))
    }

    override fun getItemCount() = itemsToShow.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemsToShow[position]
        holder.itemView.tvName.text = data.title
        holder.itemView.imageView.loadFromUrl(data.image)
    }

    fun filterDatas(s: String) {
        itemsToShow.clear()
        for (item in items) {
            if (item.title.contains(s, true)) {
                itemsToShow.add(item)
            }
        }
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {

        }
    }
}