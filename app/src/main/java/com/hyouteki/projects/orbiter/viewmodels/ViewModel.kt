package com.hyouteki.projects.orbiter.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hyouteki.projects.orbiter.databases.WebPageDatabase
import com.hyouteki.projects.orbiter.models.WebPage
import com.hyouteki.projects.orbiter.repos.WebPageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModel(application: Application) : AndroidViewModel(application) {

    private val webPageRepo: WebPageRepository =
        WebPageRepository(WebPageDatabase.getDatabase(application).getWebPageDAO())
    val allBookmarks: LiveData<List<WebPage>> = webPageRepo.allBookmarks
    val allHistory: LiveData<List<WebPage>> = webPageRepo.allHistory
    val allTabs: LiveData<List<WebPage>> = webPageRepo.allTabs
    val all: LiveData<List<WebPage>> = webPageRepo.all

    fun insertWebPage(webPage: WebPage) = viewModelScope.launch(Dispatchers.IO) {
        webPageRepo.insert(webPage)
    }

    fun removeWebPage(webPage: WebPage) = viewModelScope.launch(Dispatchers.IO) {
        webPageRepo.delete(webPage)
    }

    fun updateWebPage(id: Int, name: String, url: String, type: String) = viewModelScope.launch(
        Dispatchers.IO
    ) {
        webPageRepo.update(id, name, url, type)
    }

    fun updateTab(id: Int, url: String) = viewModelScope.launch(
        Dispatchers.IO
    ) {
        webPageRepo.updateTab(id, url)
    }


    fun clearHistory() = viewModelScope.launch(Dispatchers.IO) {
        webPageRepo.clearHistory()
    }

    fun clearTabs() = viewModelScope.launch(Dispatchers.IO) {
        webPageRepo.clearTabs()
    }

}