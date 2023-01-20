package com.hyouteki.orbiter.interfaces

import com.hyouteki.orbiter.models.WebPage

interface Communicator {
    fun shareUrl(url: String) {

    }

    fun exit() {

    }

    fun openSettings() {

    }

    fun openHistory() {

    }

    fun openBookmarkOptions(bookmark: WebPage) {

    }

    fun launchWebpage(bookmark: WebPage) {

    }

    fun openAddBookmarkBottomSheet(url: String) {

    }

    fun shareWebPage() {

    }

    fun updateWebPage() {

    }

    fun updateWebPage(name: String) {

    }

    fun removeWebPage() {

    }
}