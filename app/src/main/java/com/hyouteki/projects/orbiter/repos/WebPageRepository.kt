package com.hyouteki.projects.orbiter.repos

import com.hyouteki.projects.orbiter.daos.WebPageDAO
import com.hyouteki.projects.orbiter.models.WebPage

class WebPageRepository(private val dao: WebPageDAO) {

    val allBookmarks = dao.allBookmarks()
    val allHistory = dao.allHistory()
    val allTabs = dao.allTabs()
    val all = dao.getAll()

    suspend fun insert(webPage: WebPage) = dao.insert(webPage)

    suspend fun delete(webPage: WebPage) = dao.delete(webPage)

    suspend fun update(id: Int, name: String, url: String, type: String) =
        dao.updateWebpage(id, name, url, type)


    suspend fun updateTab(id: Int, url: String) =
        dao.updateTab(id, url)

    suspend fun clearHistory(): Int {
        dao.clearHistory()
        return 0
    }

    suspend fun clearTabs() {
        dao.clearTabs()
    }

}