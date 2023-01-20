package com.hyouteki.orbiter.activities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.snackbar.Snackbar
import com.hyouteki.orbiter.R
import com.hyouteki.orbiter.interfaces.Helper
import com.hyouteki.orbiter.interfaces.Logger
import com.hyouteki.orbiter.interfaces.Preferences
import com.hyouteki.orbiter.interfaces.Theme


class SettingsActivity : AppCompatActivity(), Helper {
    private lateinit var launchSwitch: MaterialSwitch
    private lateinit var takoThemeButton: ImageView
    private lateinit var midnightThemeButton: ImageView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var themeLabel: TextView
    private lateinit var launchLabel: TextView
    private lateinit var saveButton: Button
    private var currentTheme = Theme.TAKO

    companion object {
        const val LAUNCH_TAG = "launchWhereYouLeft"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        launchSwitch = findViewById(R.id.ms_as_launch_setting)
        takoThemeButton = findViewById(R.id.iv_as_tako)
        midnightThemeButton = findViewById(R.id.iv_as_midnight)
        themeLabel = findViewById(R.id.tv_as_theme_text)
        launchLabel = findViewById(R.id.tv_as_launch_text)
        saveButton = findViewById(R.id.btn_as_save)
        val closeButton = findViewById<Button>(R.id.btn_as_close)
        sharedPreferences = Preferences.getPreferences(this)
        editor = Preferences.getEditor(this)
        launch()
        closeButton.setOnClickListener {
            finish()
        }
        saveButton.setOnClickListener {
            var flag = true
            if (currentTheme != sharedPreferences.getInt(MainActivity.THEME_TAG, Theme.MIDNIGHT)) {
                this.makeSnackBar(
                    saveButton,
                    "Theme will take effect after restart",
                    resources
                )
                flag = false
            }
            editor.apply {
                Theme.setTheme(currentTheme)
                putBoolean(LAUNCH_TAG, launchSwitch.isChecked)
                putInt(MainActivity.THEME_TAG, currentTheme)
                apply()
            }
            if (flag) {
                super.makeSnackBar(saveButton, "Settings saved", resources)
            }
        }
        takoThemeButton.setOnClickListener {
            if (currentTheme == Theme.MIDNIGHT) {
                midnightThemeButton.setImageDrawable(null)
                takoThemeButton.setImageResource(R.drawable.ic_check)
                currentTheme = Theme.TAKO
            }
        }
        midnightThemeButton.setOnClickListener {
            if (currentTheme == Theme.TAKO) {
                takoThemeButton.setImageDrawable(null)
                midnightThemeButton.setImageResource(R.drawable.ic_check)
                currentTheme = Theme.MIDNIGHT
            }
        }
        launchSwitch.setOnCheckedChangeListener { _, _ ->
            handleSwitchColor(launchSwitch)
        }
    }

    private fun launch() {
        launchSwitch.isChecked = sharedPreferences.getBoolean(LAUNCH_TAG, false)
        handleTheme()
        when (currentTheme) {
            Theme.TAKO -> {
                midnightThemeButton.setImageDrawable(null)
            }
            Theme.MIDNIGHT -> {
                takoThemeButton.setImageDrawable(null)
            }
            else -> Logger.exception("Wrong theme id")
        }
    }

    private fun setSwitchColor(switch: MaterialSwitch, color: Int) {
        switch.trackDrawable?.setColorFilter(
            ContextCompat.getColor(this, color),
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun handleSwitchColor(switch: MaterialSwitch) {
        if (switch.isChecked) {
            setSwitchColor(switch, currentTheme)
        } else {
            setSwitchColor(switch, R.color.switchOffColor)
        }
    }

    private fun handleTheme() {
        currentTheme = sharedPreferences.getInt(MainActivity.THEME_TAG, Theme.TAKO)
        themeLabel.setTextColor(resources.getColor(currentTheme))
        launchLabel.setTextColor(resources.getColor(currentTheme))
        handleSwitchColor(launchSwitch)
        saveButton.backgroundTintList = resources.getColorStateList(currentTheme)
    }

    override fun makeSnackBar(view: View, message: String, resources: Resources) {
        Snackbar
            .make(view, message, Snackbar.LENGTH_SHORT)
            .setAnchorView(view)
            .setBackgroundTint(resources.getColor(R.color.colorQuaternary))
            .setTextColor(resources.getColor(R.color.colorTertiary))
            .setActionTextColor(resources.getColor(currentTheme))
            .setAction("Restart now") {
                restartApp()
            }
            .show()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun restartApp() {
        val pendingIntentId = 666
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            pendingIntentId,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val alarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager[AlarmManager.RTC, System.currentTimeMillis() + 100] = pendingIntent
        System.exit(0)
    }
}