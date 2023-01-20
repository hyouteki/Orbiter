package com.hyouteki.orbiter.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hyouteki.orbiter.daos.WebPageDAO
import com.hyouteki.orbiter.models.WebPage

@Database(
    entities = [WebPage::class],
    version = 1,
    exportSchema = false
)
abstract class WebPageDatabase: RoomDatabase() {

    abstract fun getWebPageDAO(): WebPageDAO

    companion object {
        @Volatile
        private var INSTANCE: WebPageDatabase? = null

        fun getDatabase(context: Context): WebPageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WebPageDatabase::class.java,
                    "web_page_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}