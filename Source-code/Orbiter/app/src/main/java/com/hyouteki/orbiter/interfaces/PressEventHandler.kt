package com.hyouteki.orbiter.interfaces

interface PressEventHandler {
    companion object {
        const val CONTINUE = true
        const val WAIT = false
    }

    fun onBackPressed(): Boolean {
        return CONTINUE
    }
}