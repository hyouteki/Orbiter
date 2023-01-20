package com.hyouteki.orbiter.activities

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.hyouteki.orbiter.R
import com.hyouteki.orbiter.adapters.HistoryAdapter
import com.hyouteki.orbiter.classes.SwipeGesture
import com.hyouteki.orbiter.interfaces.Communicator
import com.hyouteki.orbiter.viewmodels.MyViewModel

class HistoryActivity : AppCompatActivity(), Communicator {
    private val viewModel: MyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val closeButton = findViewById<Button>(R.id.btn_ah_close)
        val clearHistoryButton = findViewById<MaterialButton>(R.id.btn_ah_clear_history)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_ah_recycler)
        val myAdapter = HistoryAdapter(this, this)

        closeButton.setOnClickListener {
            finish()
        }

        clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        val swipeGesture = object : SwipeGesture() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT -> {
                        viewModel.removeWebPage(myAdapter.getItem(viewHolder.adapterPosition))
                    }
                }
            }
        }
        ItemTouchHelper(swipeGesture).attachToRecyclerView(recyclerView)

        recyclerView.adapter = myAdapter
        viewModel.allHistory.observeForever(Observer {
            it?.let {
                myAdapter.updateData(it)
            }
        })
    }
}