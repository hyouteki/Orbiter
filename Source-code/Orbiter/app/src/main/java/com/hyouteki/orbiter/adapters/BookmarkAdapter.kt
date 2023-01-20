package com.hyouteki.orbiter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hyouteki.orbiter.R
import com.hyouteki.orbiter.interfaces.Communicator
import com.hyouteki.orbiter.models.WebPage
import com.squareup.picasso.Picasso

class BookmarkAdapter(
    private val listener: Communicator,
    private val context: Context
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    private val dataSet = arrayListOf<WebPage>()

    inner class BookmarkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.iv_bli_icon)
        val name: TextView = view.findViewById(R.id.tv_bli_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.bookmark_list_item, parent, false)
        val viewHolder = BookmarkViewHolder(view)
        view.setOnClickListener {
            listener.launchWebpage(dataSet[viewHolder.adapterPosition])
        }
        view.setOnLongClickListener {
            listener.openBookmarkOptions(dataSet[viewHolder.adapterPosition])
            true
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val model = dataSet[position]
//        Picasso
//            .get()
//            .load("${model.url}/favicon.ico")
//            .into(holder.icon)
        holder.name.text = model.name
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<WebPage>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }

    fun getItem(position: Int) = dataSet[position]
}