package com.hyouteki.projects.orbiter.activities.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hyouteki.projects.orbiter.R
import com.hyouteki.projects.orbiter.databinding.ActivityHistorySettingsBinding
import com.hyouteki.projects.orbiter.databinding.ActivitySearchSettingsBinding
import com.hyouteki.projects.orbiter.databinding.ActivityWebViewSettingsBinding
import com.hyouteki.projects.orbiter.utils.Saver
import com.hyouteki.projects.orbiter.viewmodels.ViewModel

class SearchSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val viewModel: ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleTopAppBarActions()
        initializeVariables()
        initializeSwitches()
        handleContainerActions()
        handleSwitchActions()
    }

    private fun handleTopAppBarActions() {
        binding.topAppBar.setNavigationOnClickListener { finish() }
    }

    private fun initializeVariables() {
        sharedPreferences = Saver.getPreferences(this)
        editor = Saver.getEditor(this)
    }

    private fun initializeSwitches() {
        binding.searchBarSwitch.isChecked =
            sharedPreferences.getBoolean(Saver.TAB_MENU, false)
        when (binding.searchBarSwitch.isChecked) {
            false -> {
                binding.searchBar.visibility = View.VISIBLE
                binding.searchBar2.visibility = View.INVISIBLE
            }

            true -> {
                binding.searchBar.visibility = View.INVISIBLE
                binding.searchBar2.visibility = View.VISIBLE
            }
        }
    }

    private fun handleContainerActions() {
        binding.searchBarContainer.setOnClickListener {
            binding.searchBarSwitch.isChecked = !binding.searchBarSwitch.isChecked
            doTheMagic()
        }
    }

    private fun doTheMagic() {
        when (binding.searchBarSwitch.isChecked) {
            false -> {
                binding.searchBar.visibility = View.VISIBLE
                binding.searchBar2.visibility = View.INVISIBLE
            }

            true -> {
                binding.searchBar.visibility = View.INVISIBLE
                binding.searchBar2.visibility = View.VISIBLE
            }
        }
        storeSettings()
    }

    private fun handleSwitchActions() {
        binding.searchBarSwitch.setOnCheckedChangeListener { _, _ ->
            doTheMagic()
        }
    }

    private fun storeSettings() {
        editor.apply {
            putBoolean(Saver.TAB_MENU, binding.searchBarSwitch.isChecked)
            apply()
        }
    }
}