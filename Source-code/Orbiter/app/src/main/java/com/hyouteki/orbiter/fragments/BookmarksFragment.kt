package com.hyouteki.orbiter.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.hyouteki.orbiter.R
import com.hyouteki.orbiter.adapters.BookmarkAdapter
import com.hyouteki.orbiter.interfaces.Communicator
import com.hyouteki.orbiter.viewmodels.MyViewModel

class BookmarksFragment : Fragment(), Communicator {
    private val viewModel: MyViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var empty: TextView
    private lateinit var myAdapter: BookmarkAdapter
    private lateinit var comm: Communicator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bookmarks, container, false)
        recyclerView = view.findViewById(R.id.rv_fb_recycler)
        empty = view.findViewById(R.id.tv_fb_empty)
        comm = activity as Communicator
        myAdapter = BookmarkAdapter(comm, requireActivity())
        recyclerView.adapter = myAdapter
        viewModel.allBookmarks.observeForever(Observer {
            it?.let {
                myAdapter.updateData(it)
                handleEmptyText()
            }
        })
        return view
    }

    private fun handleEmptyText() {
        when (myAdapter.itemCount) {
            0 -> empty.visibility = View.VISIBLE
            else -> empty.visibility = View.INVISIBLE
        }
    }
}