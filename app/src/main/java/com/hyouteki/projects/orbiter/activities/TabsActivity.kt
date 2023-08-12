package com.hyouteki.projects.orbiter.activities

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hyouteki.projects.orbiter.R
import com.hyouteki.projects.orbiter.adapters.TabCommunicator
import com.hyouteki.projects.orbiter.adapters.TabsAdapter
import com.hyouteki.projects.orbiter.databinding.ActivityMainBinding
import com.hyouteki.projects.orbiter.databinding.ActivityTabsBinding
import com.hyouteki.projects.orbiter.models.WebPage
import com.hyouteki.projects.orbiter.utils.Logger
import com.hyouteki.projects.orbiter.utils.Saver
import com.hyouteki.projects.orbiter.viewmodels.ViewModel
import kotlinx.android.synthetic.main.main_nav_header.share

class TabsActivity : AppCompatActivity(), TabCommunicator {

    private lateinit var binding: ActivityTabsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val viewModel: ViewModel by viewModels()
    private lateinit var adapter: TabsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTabsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeVariables()
        handleRecyclerView()
        handleTopAppBarActions()
    }

    private fun handleTopAppBarActions() {
        binding.topAppBar.setNavigationOnClickListener { finish() }
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_add -> {
                    val id: Int = System.currentTimeMillis().toInt()
                    viewModel.insertWebPage(
                        WebPage(
                            id = id,
                            type = "tab"
                        )
                    )
                    true
                }

                else -> false
            }
        }
    }

    private fun initializeVariables() {
        sharedPreferences = Saver.getPreferences(this)
        editor = Saver.getEditor(this)
        adapter = TabsAdapter(
            this,
            this,
            sharedPreferences.getInt(Saver.TAB_LAYOUT, R.layout.tabs_list_item)
        )
    }

    private fun handleRecyclerView() {
        binding.recyclerView.adapter = adapter
        viewModel.allTabs.observeForever(Observer {
            it?.let {
                adapter.updateData(it)
            }
        })
    }

    override fun closeTab(tab: WebPage) {
        viewModel.removeWebPage(tab)
    }

    override fun openTab(tab: WebPage) {
        editor.putInt(Saver.CURRENT_TAB, tab.id).apply()
        finish()
    }
}