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

class HistoryAdapter(
    private val listener: HistoryCommunicator,
    private val context: Context
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private val dataSet = arrayListOf<WebPage>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val url: TextView = view.findViewById(R.id.url)
        val time: TextView = view.findViewById(R.id.time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.history_list_item, parent, false)
        val viewHolder = ViewHolder(view)
        view.setOnClickListener {
            listener.openTab(dataSet[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.url.text = item.url
        holder.time.text = item.time
    }

    override fun getItemCount() = dataSet.size

    fun getItem(position: Int) = dataSet[position]

    fun updateData(newDataSet: List<WebPage>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }
}

interface HistoryCommunicator {
    fun openTab(tab: WebPage)
}