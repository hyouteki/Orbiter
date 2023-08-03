package com.hyouteki.projects.orbiter.activities

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hyouteki.projects.orbiter.R
import com.hyouteki.projects.orbiter.adapters.HistoryAdapter
import com.hyouteki.projects.orbiter.adapters.HistoryCommunicator
import com.hyouteki.projects.orbiter.databinding.ActivityHistoryBinding
import com.hyouteki.projects.orbiter.models.WebPage
import com.hyouteki.projects.orbiter.utils.Saver
import com.hyouteki.projects.orbiter.utils.SwipeGesture
import com.hyouteki.projects.orbiter.viewmodels.ViewModel

class HistoryActivity : AppCompatActivity(), HistoryCommunicator {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val viewModel: ViewModel by viewModels()
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeVariables()
        handleRecyclerView()
        handleTopAppBarActions()
    }

    private fun handleTopAppBarActions() {
        binding.topAppBar.setNavigationOnClickListener { finish() }
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_clear_history -> {
                    viewModel.clearHistory()
                    true
                }

                else -> false
            }
        }
    }

    private fun initializeVariables() {
        sharedPreferences = Saver.getPreferences(this)
        editor = Saver.getEditor(this)
        adapter = HistoryAdapter(this, this)
    }

    private fun handleRecyclerView() {
        val swipeGesture = object : SwipeGesture() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT -> {
                        viewModel.removeWebPage(adapter.getItem(viewHolder.adapterPosition))
                    }
                }
            }
        }
        ItemTouchHelper(swipeGesture).attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.adapter = adapter
        viewModel.allHistory.observeForever(Observer {
            it?.let {
                adapter.updateData(it)
            }
        })
    }

    override fun openTab(tab: WebPage) {
        editor.putInt(Saver.CURRENT_TAB, tab.id).apply()
        finish()
    }
}