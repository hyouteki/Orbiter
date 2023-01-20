package com.hyouteki.orbiter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hyouteki.orbiter.R
import com.hyouteki.orbiter.interfaces.Communicator
import com.hyouteki.orbiter.models.WebPage

class HistoryAdapter(
    private val listener: Communicator,
    private val context: Context
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val dataSet = arrayListOf<WebPage>()

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val url: TextView = view.findViewById(R.id.tv_hli_url)
        val time: TextView = view.findViewById(R.id.tv_hli_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(context).inflate(R.layout.history_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = dataSet[position]
        holder.url.text = item.url
        holder.time.text = item.time
    }

    override fun getItemCount() = dataSet.size

    fun updateData(newDataSet: List<WebPage>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }

    fun getItem(position: Int) = dataSet[position]
}