package com.hyouteki.projects.orbiter.activities.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hyouteki.projects.orbiter.R
import com.hyouteki.projects.orbiter.databinding.ActivityWebViewSettingsBinding
import com.hyouteki.projects.orbiter.utils.Saver

class WebViewSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleTopAppBarActions()
        initializeVariables()
        initializeSwitches()
        handleVisibility()
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
        binding.javaScriptEnabledSwitch.isChecked =
            sharedPreferences.getBoolean(Saver.JAVA_SCRIPT_ENABLED, true)
        binding.supportZoomSwitch.isChecked =
            sharedPreferences.getBoolean(Saver.SUPPORT_ZOOM, true)
        binding.displayZoomControlsSwitch.isChecked =
            sharedPreferences.getBoolean(Saver.DISPLAY_ZOOM_CONTROLS, true)
        binding.allowContentAccessSwitch.isChecked =
            sharedPreferences.getBoolean(Saver.ALLOW_CONTENT_ACCESS, true)
        binding.domStorageEnabledSwitch.isChecked =
            sharedPreferences.getBoolean(Saver.DOM_STORAGE_ENABLED, true)
    }

    private fun handleVisibility() {
        when (binding.supportZoomSwitch.isChecked) {
            true -> binding.displayZoomControlsContainer.visibility = View.VISIBLE
            false -> binding.displayZoomControlsContainer.visibility = View.GONE
        }
    }

    private fun handleContainerActions() {
        binding.javaScriptEnabledContainer.setOnClickListener {
            binding.javaScriptEnabledSwitch.isChecked = !binding.javaScriptEnabledSwitch.isChecked
            doTheMagic()
        }
        binding.supportZoomContainer.setOnClickListener {
            binding.supportZoomSwitch.isChecked = !binding.supportZoomSwitch.isChecked
            doTheMagic()
        }
        binding.displayZoomControlsContainer.setOnClickListener {
            binding.displayZoomControlsSwitch.isChecked =
                !binding.displayZoomControlsSwitch.isChecked
            doTheMagic()
        }
        binding.allowContentAccessContainer.setOnClickListener {
            binding.allowContentAccessSwitch.isChecked = !binding.allowContentAccessSwitch.isChecked
            doTheMagic()
        }
        binding.domStorageEnabledContainer.setOnClickListener {
            binding.domStorageEnabledSwitch.isChecked = !binding.domStorageEnabledSwitch.isChecked
            doTheMagic()
        }
        binding.revertBackContainer.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.revert_back_title))
                .setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
                .setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                    binding.javaScriptEnabledSwitch.isChecked = true
                    binding.supportZoomSwitch.isChecked = true
                    binding.displayZoomControlsSwitch.isChecked = true
                    binding.allowContentAccessSwitch.isChecked = true
                    binding.domStorageEnabledSwitch.isChecked = true
                    doTheMagic()
                }
                .show()
        }
    }

    private fun doTheMagic() {
        handleVisibility()
        storeSettings()
    }

    private fun handleSwitchActions() {
        binding.javaScriptEnabledSwitch.setOnCheckedChangeListener { _, _ ->
            doTheMagic()
        }
        binding.supportZoomSwitch.setOnCheckedChangeListener { _, _ ->
            doTheMagic()
        }
        binding.displayZoomControlsSwitch.setOnCheckedChangeListener { _, _ ->
            doTheMagic()
        }
        binding.allowContentAccessSwitch.setOnCheckedChangeListener { _, _ ->
            doTheMagic()
        }
        binding.domStorageEnabledSwitch.setOnCheckedChangeListener { _, _ ->
            doTheMagic()
        }
    }

    private fun storeSettings() {
        editor.apply {
            putBoolean(Saver.JAVA_SCRIPT_ENABLED, binding.javaScriptEnabledSwitch.isChecked)
            putBoolean(Saver.SUPPORT_ZOOM, binding.supportZoomSwitch.isChecked)
            putBoolean(Saver.DISPLAY_ZOOM_CONTROLS, binding.displayZoomControlsSwitch.isChecked)
            putBoolean(Saver.ALLOW_CONTENT_ACCESS, binding.allowContentAccessSwitch.isChecked)
            putBoolean(Saver.DOM_STORAGE_ENABLED, binding.domStorageEnabledSwitch.isChecked)
            apply()
        }
    }
}