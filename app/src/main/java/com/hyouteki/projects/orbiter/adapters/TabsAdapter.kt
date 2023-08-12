package com.hyouteki.projects.orbiter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hyouteki.projects.orbiter.R
import com.hyouteki.projects.orbiter.models.WebPage

class TabsAdapter(
    private val listener: TabCommunicator,
    private val context: Context,
    private val layout: Int = R.layout.tabs_list_item,
) : RecyclerView.Adapter<TabsAdapter.ViewHolder>() {

    private val dataSet = arrayListOf<WebPage>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val url: TextView = view.findViewById(R.id.url)
        val close: ImageView = view.findViewById(R.id.close)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(layout, parent, false)
        val viewHolder = ViewHolder(view)
        view.setOnClickListener {
            listener.openTab(dataSet[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        when (layout) {
            R.layout.tabs_list_item -> {
                holder.url.text = item.url
                holder.close.setOnClickListener {
                    listener.closeTab(item)
                }
            }
        }
    }

    override fun getItemCount() = dataSet.size

    fun getItem(position: Int) = dataSet[position]

    fun updateData(newDataSet: List<WebPage>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }
}

interface TabCommunicator {
    fun closeTab(tab: WebPage)
    fun openTab(tab: WebPage)
}