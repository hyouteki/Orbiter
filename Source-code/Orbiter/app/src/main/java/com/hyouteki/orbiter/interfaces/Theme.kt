package com.hyouteki.orbiter.interfaces

import com.hyouteki.orbiter.R

interface Theme {
    companion object {
        const val TAKO = R.color.takoCream
        const val MIDNIGHT = R.color.midnightPurple
        private var currentTheme = TAKO
        fun setTheme(theme: Int) {
            when (theme) {
                TAKO, MIDNIGHT -> {
                    currentTheme = theme
                }
                else -> {
                    Logger.exception("Wrong theme id: $theme")
                }
            }
        }

        fun getTheme() = currentTheme
        fun getResource(): Int {
            return when (currentTheme) {
                TAKO -> R.drawable.tako_theme
                MIDNIGHT -> R.drawable.midnight_theme
                else -> 0
            }
        }
    }
}