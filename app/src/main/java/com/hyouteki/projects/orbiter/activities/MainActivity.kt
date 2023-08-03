package com.hyouteki.projects.orbiter.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hyouteki.projects.orbiter.R
import com.hyouteki.projects.orbiter.activities.settings.SettingsActivity
import com.hyouteki.projects.orbiter.databinding.ActivityMainBinding
import com.hyouteki.projects.orbiter.models.WebPage
import com.hyouteki.projects.orbiter.utils.Logger
import com.hyouteki.projects.orbiter.utils.Saver
import com.hyouteki.projects.orbiter.viewmodels.ViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentFragment: Fragment? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val viewModel: ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setSupportActionBar(binding.searchViewToolbar)

        initializeVariables()
        initializePreprocessing()
        handleSearchMenuActions()
        handleFabActions()
        handleNavMenuActions()
        handleSearchViewActions()
    }

    private fun initializeVariables() {
        sharedPreferences = Saver.getPreferences(this)
        editor = Saver.getEditor(this)
    }

    private fun initializePreprocessing() {
        when (sharedPreferences.getBoolean(Saver.TAB_MENU, false)) {
            false -> binding.searchBar.inflateMenu(R.menu.menu_main_search)
            true -> binding.searchBar.inflateMenu(R.menu.menu_main_search_2)
        }
        webViewVisibility(false)
        setUpTabs()
        if (sharedPreferences.getBoolean(Saver.LAUNCH_NEW_TAB, false)) {
            val id: Int = System.currentTimeMillis().toInt()
            viewModel.insertWebPage(
                WebPage(
                    id = id, type = "tab"
                )
            )
            editor.apply {
                putInt(Saver.CURRENT_TAB, id)
                apply()
            }
        }
        setUpWebView()
        webViewVisibility(true)
    }

    private fun setUpTabs() {
        var flag: Boolean = true
        viewModel.allTabs.observeForever(Observer {
            if (it?.size == 0 && flag) {
                val id: Int = System.currentTimeMillis().toInt()
                viewModel.insertWebPage(
                    WebPage(
                        id = id, type = "tab"
                    )
                )
                editor.apply {
                    putInt(Saver.CURRENT_TAB, id)
                    apply()
                }
                flag = false
            }
        })
    }

    private fun setUpWebView() {
        val id: Int = sharedPreferences.getInt(Saver.CURRENT_TAB, 0)
        if (id != 0) {
            viewModel.allTabs.observeForever(Observer {
                for (webPage in it) {
                    if (webPage.id == id) {
                        binding.searchBar.text = webPage.url
                        performWebSearch(webPage.url)
                        break
                    }
                }
            })
        }
    }

    private fun handleSearchMenuActions() {
        binding.searchBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_more -> {
                    openDrawer()
                    true
                }

                R.id.action_tabs -> {
                    openTabs()
                    true
                }

                else -> false
            }
        }
    }

    private fun handleFabActions() {
        binding.navPanel.getHeaderView(0).findViewById<FloatingActionButton>(R.id.back)
            .setOnClickListener {
                if (binding.webView.canGoBack()) {
                    webViewGoBack()
                }
                closeDrawer()
            }

        binding.navPanel.getHeaderView(0).findViewById<FloatingActionButton>(R.id.refresh)
            .setOnClickListener {
                binding.webView.reload()
                closeDrawer()
            }

        binding.navPanel.getHeaderView(0).findViewById<FloatingActionButton>(R.id.share)
            .setOnClickListener {
                shareWebPage()
                closeDrawer()
            }

        binding.navPanel.getHeaderView(0).findViewById<FloatingActionButton>(R.id.close)
            .setOnClickListener {
                closeDrawer()
            }
    }

    private fun shareWebPage() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, binding.webView.url)
        startActivity(shareIntent)
    }

    private fun handleNavMenuActions() {
        binding.navPanel.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.new_tab -> {
                    val id: Int = System.currentTimeMillis().toInt()
                    viewModel.insertWebPage(
                        WebPage(
                            id = id, type = "tab"
                        )
                    )
                    editor.apply {
                        putInt(Saver.CURRENT_TAB, id)
                        apply()
                    }
                    setUpWebView()
                }

                R.id.new_private_tab -> {
                }

                R.id.history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                }

                R.id.clear_history -> {
                    viewModel.clearHistory()
                }

                R.id.add_bookmark -> {
//                    webView.url?.let { url ->
//                        comm.openAddBookmarkBottomSheet(url)
//                    }
                }

                R.id.bookmarks -> {
                }

                R.id.tabs -> {
                    openTabs()
                }

                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }

                R.id.exit -> {
                    finish()
                }
            }
            closeDrawer()
            true
        }
    }

    private fun openTabs() {
        startActivity(Intent(this, TabsActivity::class.java))
    }

    private fun handleSearchViewActions() {
        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            binding.searchBar.text = binding.searchView.text
            binding.searchView.hide()
            searchSubmitAction(binding.searchView.text.toString())
            false
        }
    }

    private fun formatQuery(query: String?): String? {
        val searchPrompt = when (sharedPreferences.getString(Saver.SEARCH_ENGINE, Saver.GOOGLE)) {
            Saver.BRAVE -> "https://search.brave.com/search?q="
            Saver.BING -> "https://www.bing.com/search?q="
            Saver.DUCK_DUCK_GO -> "https://duckduckgo.com/?q="
            Saver.ECOSIA -> "https://www.ecosia.com/search?q="
            Saver.GOOGLE -> "https://www.google.com/search?q="
            Saver.START_PAGE -> "https://www.startpage.com/search?q="
            else -> "https://www.google.com/search?q="
        }
        return when (query) {
            null -> null
            else -> {
                if (!query.startsWith("http://") && !query.startsWith("https://") && !query.contains(
                        "."
                    )
                ) {
                    "$searchPrompt$query"
                } else {
                    query
                }
            }
        }
    }

    private fun searchSubmitAction(query: String?) {
        val url = formatQuery(query)
        if (url != null) {
            performWebSearch(url)
//            makeToast(url)
            viewModel.updateTab(sharedPreferences.getInt(Saver.CURRENT_TAB, 0), url)
            if (url != "" && url != "about:blank" && !sharedPreferences.getBoolean(
                    Saver.PAUSE_HISTORY, false
                )
            ) {
//            Logger.debugger("Url is: $url")
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val time = LocalDateTime.now().format(formatter)

                viewModel.insertWebPage(
                    WebPage(
                        id = System.currentTimeMillis().toInt(),
                        time = time,
                        url = url,
                        type = "history"
                    )
                )
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun performWebSearch(url: String) {
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                webViewVisibility(false)
                binding.searchBar.text = binding.webView.url
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                webViewVisibility(true)
                super.onPageCommitVisible(view, url)
            }
        }
        binding.webView.loadUrl(url)
        binding.webView.settings.javaScriptEnabled =
            sharedPreferences.getBoolean(Saver.JAVA_SCRIPT_ENABLED, true)
        binding.webView.settings.setSupportZoom(
            sharedPreferences.getBoolean(
                Saver.SUPPORT_ZOOM, true
            )
        )
        binding.webView.settings.allowContentAccess =
            sharedPreferences.getBoolean(Saver.ALLOW_CONTENT_ACCESS, true)
        binding.webView.settings.domStorageEnabled =
            sharedPreferences.getBoolean(Saver.DOM_STORAGE_ENABLED, true)
        binding.webView.settings.displayZoomControls =
            sharedPreferences.getBoolean(Saver.DISPLAY_ZOOM_CONTROLS, true)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            webViewGoBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun webViewGoBack() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
            binding.searchBar.text = binding.webView.url
        }
    }

    // facade methods()
    private fun openDrawer() {
        binding.drawer.openDrawer(GravityCompat.END)
    }

    private fun closeDrawer() {
        binding.drawer.closeDrawer(GravityCompat.END)
    }

    private fun makeToast(message: String?, extended: Boolean = false) {
        when (extended) {
            true -> Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            false -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun webViewVisibility(visibility: Boolean) {
        when (visibility) {
            true -> {
                binding.webView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }

            false -> {
                binding.webView.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        webViewVisibility(false)
        setUpTabs()
        setUpWebView()
        webViewVisibility(true)
        super.onResume()
    }
}