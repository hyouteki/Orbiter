package com.hyouteki.projects.orbiter.activities.settings

import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hyouteki.projects.orbiter.R
import com.hyouteki.projects.orbiter.databinding.ActivityGeneralSettingsBinding
import com.hyouteki.projects.orbiter.utils.Saver

class GeneralSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGeneralSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeneralSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleTopAppBarActions()
        initializeVariables()
        initializeSwitches()
        initializeDescription()
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
        binding.defaultLaunchStateSwitch.isChecked =
            sharedPreferences.getBoolean(Saver.LAUNCH_NEW_TAB, false)
        binding.searchEngineDesc.text =
            sharedPreferences.getString(Saver.SEARCH_ENGINE, Saver.GOOGLE)
    }

    private fun handleContainerActions() {
        binding.defaultLaunchStateContainer.setOnClickListener {
            binding.defaultLaunchStateSwitch.isChecked = !binding.defaultLaunchStateSwitch.isChecked
            initializeDescription()
            storeSettings()
        }
        binding.searchEngineContainer.setOnClickListener {
            with(MaterialAlertDialogBuilder(this)) {
                setTitle("Search engine")
                val items = arrayOf(
                    Saver.BRAVE,
                    Saver.BING,
                    Saver.DUCK_DUCK_GO,
                    Saver.ECOSIA,
                    Saver.GOOGLE,
                    Saver.START_PAGE
                )
                var checkedItem = when (binding.searchEngineDesc.text) {
                    Saver.BRAVE -> 0
                    Saver.BING -> 1
                    Saver.DUCK_DUCK_GO -> 2
                    Saver.ECOSIA -> 3
                    Saver.GOOGLE -> 4
                    else -> 5
                }
                setSingleChoiceItems(items, checkedItem,
                    DialogInterface.OnClickListener { _, which ->
                        checkedItem = which
                    })
                setPositiveButton("Save") { _, _ ->
                    binding.searchEngineDesc.text = items[checkedItem]
                    editor.putString(Saver.SEARCH_ENGINE, items[checkedItem]).apply()
                }
                setNegativeButton("Cancel") { _, _ -> }
                show()
            }
        }
    }

    private fun handleSwitchActions() {
        binding.defaultLaunchStateSwitch.setOnCheckedChangeListener { _, _ ->
            initializeDescription()
            storeSettings()
        }
    }

    private fun storeSettings() {
        editor.apply {
            putBoolean(Saver.LAUNCH_NEW_TAB, binding.defaultLaunchStateSwitch.isChecked)
            apply()
        }
    }

    private fun initializeDescription() {
        when (binding.defaultLaunchStateSwitch.isChecked) {
            true -> binding.defaultLaunchStateDesc.setText(R.string.open_new_tab_on_launch)
            false -> binding.defaultLaunchStateDesc.setText(R.string.resume_from_where_you_left)
        }
    }
}