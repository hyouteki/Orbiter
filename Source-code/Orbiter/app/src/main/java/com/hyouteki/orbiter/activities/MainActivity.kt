package com.hyouteki.orbiter.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hyouteki.orbiter.R
import com.hyouteki.orbiter.dialogs.AddBookmarkBottomSheet
import com.hyouteki.orbiter.dialogs.BookmarkOptionsBottomSheet
import com.hyouteki.orbiter.fragments.BookmarksFragment
import com.hyouteki.orbiter.fragments.HomeFragment
import com.hyouteki.orbiter.fragments.TabsFragment
import com.hyouteki.orbiter.interfaces.*
import com.hyouteki.orbiter.models.WebPage
import com.hyouteki.orbiter.viewmodels.MyViewModel

class MainActivity : AppCompatActivity(), Communicator, Helper {
    private val viewModel: MyViewModel by viewModels()
    private lateinit var bottomNavBar: BottomNavigationView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    companion object {
        var webPageTemp: WebPage? = null
        const val THEME_TAG = "color_theme"
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavBar = findViewById<BottomNavigationView>(R.id.bnv_am_bottom_navigation_bar)
        sharedPreferences = Preferences.getPreferences(this)
        editor = Preferences.getEditor(this)
        setTheme()
        bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.i_mbnbm_bookmarks -> {
                    setFragment(BookmarksFragment())
                    true
                }
                R.id.i_mbnbm_tabs -> {
                    setFragment(TabsFragment())
                    true
                }
                R.id.i_mbnbm_home -> {
                    setFragment(HomeFragment())
                    true
                }
                else -> false
            }
        }
        bottomNavBar.selectedItemId = R.id.i_mbnbm_home
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .apply {
                replace(R.id.fl_am_fragment_container, fragment)
                commit()
            }
    }

    override fun onBackPressed() {
        val fragmentInstance =
            supportFragmentManager.findFragmentById(R.id.fl_am_fragment_container)
        if (fragmentInstance is PressEventHandler) {
            Logger.information("backPressed")
            if (fragmentInstance.onBackPressed() == PressEventHandler.CONTINUE) {
                super.onBackPressed()
            }
        }
    }

    override fun shareUrl(url: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
        startActivity(shareIntent)
    }

    override fun openSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    override fun openHistory() {
        startActivity(Intent(this, HistoryActivity::class.java))
    }

    override fun exit() {
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setTheme() {
        Theme.setTheme(sharedPreferences.getInt(THEME_TAG, R.color.takoCream))
        Logger.debugger(sharedPreferences.getInt(THEME_TAG, R.color.takoCream).toString())
        bottomNavBar.itemActiveIndicatorColor = getColorStateList(Theme.getTheme())
    }

    override fun openAddBookmarkBottomSheet(url: String) {
        val dialog = AddBookmarkBottomSheet()
        val bundle = Bundle()
        bundle.putString("url", url)
        bundle.putString("name", "")
        bundle.putInt("mode", AddBookmarkBottomSheet.NEW_MODE)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "bottomSheet")
    }

    override fun launchWebpage(bookmark: WebPage) {
        editor.putString(HomeFragment.URL_TAG, bookmark.url).apply()
        setFragment(HomeFragment())
        bottomNavBar.selectedItemId = R.id.i_mbnbm_home

    }

    override fun openBookmarkOptions(bookmark: WebPage) {
        webPageTemp = bookmark
        val dialog = BookmarkOptionsBottomSheet()
        dialog.show(supportFragmentManager, "bottomSheet")
    }

    override fun shareWebPage() {
        webPageTemp?.let {
            this.shareUrl(it.url)
        }
    }

    override fun updateWebPage() {
        webPageTemp?.let {
            val dialog = AddBookmarkBottomSheet()
            val bundle = Bundle()
            bundle.putString("url", it.url)
            bundle.putString("name", it.name)
            bundle.putInt("mode", AddBookmarkBottomSheet.EDIT_MODE)
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, "bottomSheet")
        }
    }

    override fun updateWebPage(name: String) {
        webPageTemp?.let {
            viewModel.updateWebPage(it.id, name, it.url, "bookmark")
        }
    }

    override fun removeWebPage() {
        webPageTemp?.let {
            viewModel.removeWebPage(it)
        }
    }

}