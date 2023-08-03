package com.hyouteki.projects.orbiter.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "web_page_table")
class WebPage(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "name") val name: String = "null",
    @ColumnInfo(name = "time") val time: String = "null",
    @ColumnInfo(name = "url") var url: String = "https://www.google.com/search",
    @ColumnInfo(name = "type") val type: String
)