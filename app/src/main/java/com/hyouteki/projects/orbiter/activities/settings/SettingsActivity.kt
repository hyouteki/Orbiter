package com.hyouteki.projects.orbiter.activities.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hyouteki.projects.orbiter.R
import com.hyouteki.projects.orbiter.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleTopAppBarActions()
        handleButtonActions()
    }

    private fun handleButtonActions() {
        binding.generalSettingsContainer.setOnClickListener {
            startActivity(Intent(this, GeneralSettingsActivity::class.java))
        }
        binding.searchBarSettingsContainer.setOnClickListener {
            startActivity(Intent(this, SearchSettingsActivity::class.java))
        }
        binding.historySettingsContainer.setOnClickListener {
            startActivity(Intent(this, HistorySettingsActivity::class.java))
        }
        binding.webViewSettingsContainer.setOnClickListener {
            startActivity(Intent(this, WebViewSettingsActivity::class.java))
        }
    }

    private fun handleTopAppBarActions() {
        binding.topAppBar.setNavigationOnClickListener { finish() }
    }
}