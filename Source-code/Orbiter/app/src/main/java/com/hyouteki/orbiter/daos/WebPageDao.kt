package com.hyouteki.orbiter.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hyouteki.orbiter.models.WebPage

@Dao
interface WebPageDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(webPage: WebPage)

    @Delete
    fun delete(webPage: WebPage)

    @Query("Update web_page_table set name = :name, url = :url, type = :type where id = :id")
    fun updateWebpage(id: Int, name: String, url: String, type: String)

    @Query("Select * from web_page_table where type = 'bookmark' order by id DESC")
    fun allBookmarks(): LiveData<List<WebPage>>

    @Query("Select * from web_page_table where type = 'history' order by id DESC")
    fun allHistory(): LiveData<List<WebPage>>

    @Query("Select * from web_page_table order by id DESC")
    fun getAll(): LiveData<List<WebPage>>

    @Query("Select * from web_page_table where type = 'tab'")
    fun allTabs(): LiveData<List<WebPage>>

    @Query("Delete from web_page_table where type = 'history'")
    fun clearHistory()

    @Query("Delete from web_page_table where type = 'tab'")
    fun clearTabs()

}