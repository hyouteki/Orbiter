package com.hyouteki.projects.orbiter.activities.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hyouteki.projects.orbiter.R
import com.hyouteki.projects.orbiter.databinding.ActivityHistorySettingsBinding
import com.hyouteki.projects.orbiter.databinding.ActivityWebViewSettingsBinding
import com.hyouteki.projects.orbiter.utils.Saver
import com.hyouteki.projects.orbiter.viewmodels.ViewModel

class HistorySettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistorySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val viewModel: ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorySettingsBinding.inflate(layoutInflater)
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
        binding.pauseHistorySwitch.isChecked =
            sharedPreferences.getBoolean(Saver.PAUSE_HISTORY, false)
    }

    private fun handleContainerActions() {
        binding.pauseHistoryContainer.setOnClickListener {
            binding.pauseHistorySwitch.isChecked = !binding.pauseHistorySwitch.isChecked
            storeSettings()
        }
        binding.clearHistoryContainer.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.clear_history_title))
                .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                    viewModel.clearHistory()
                }
                .show()
        }
    }

    private fun handleSwitchActions() {
        binding.pauseHistorySwitch.setOnCheckedChangeListener { _, _ ->
            storeSettings()
        }
    }

    private fun storeSettings() {
        editor.apply {
            putBoolean(Saver.PAUSE_HISTORY, binding.pauseHistorySwitch.isChecked)
            apply()
        }
    }
}