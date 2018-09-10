package com.just_me.news.myNews

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.just_me.news.core.extension.inflate
import com.just_me.news.core.extension.loadFromUrl
import com.just_me.news.news.R
import kotlinx.android.synthetic.main.item_recycler.view.*
import kotlin.properties.Delegates

class MyRecyclerAdapter: RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>() {

    var itemsToShow: ArrayList<RecyclerData> = ArrayList()
    var itemsIsOpen: ArrayList<Boolean> = ArrayList()

        internal var items: ArrayList<RecyclerData> by Delegates.observable(ArrayList()) {
        _, _, _ -> updateItems()
        }

    private fun updateItems() {
        itemsToShow = ArrayList(items.size)
        itemsToShow.addAll(items)
        itemsIsOpen = ArrayList(items.size)
        itemsIsOpen.addAll(items.map { false })
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_recycler))
    }

    override fun getItemCount() = itemsToShow.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemsToShow[position]
        val isOpen = itemsIsOpen[position]
        holder.itemView.imageView.loadFromUrl(data.image)
        holder.itemView.tvName.text = data.title
        holder.itemView.tvText.text = data.about
        holder.itemView.tvText.visibility = if (isOpen) VISIBLE else GONE
        holder.itemView.llContent.setOnClickListener {
            it.tvText.visibility = if (it.tvText.visibility == GONE) VISIBLE else GONE
            itemsIsOpen[position] = it.tvText.visibility == VISIBLE
            notifyItemRangeChanged(maxOf(position-1, 0), 3)
        }
    }

    fun filterDatas(s: String) {
        itemsToShow.clear()
        itemsIsOpen.clear()
        for (item in items) {
            if (item.title.contains(s, true)) {
                itemsToShow.add(item)
                itemsIsOpen.add(false)
            }
        }
        notifyDataSetChanged()
    }

    class ViewHolder(iv: View) : RecyclerView.ViewHolder(iv) {

    }
}