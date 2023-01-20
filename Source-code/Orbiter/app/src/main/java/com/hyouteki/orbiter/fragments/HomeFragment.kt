package com.hyouteki.orbiter.fragments

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.navigation.NavigationView
import com.hyouteki.orbiter.R
import com.hyouteki.orbiter.activities.SettingsActivity
import com.hyouteki.orbiter.interfaces.*
import com.hyouteki.orbiter.models.WebPage
import com.hyouteki.orbiter.viewmodels.MyViewModel
import java.time.LocalDateTime

class HomeFragment : Fragment(), PressEventHandler, Helper {
    private lateinit var comm: Communicator
    private lateinit var drawer: DrawerLayout
    private lateinit var webView: WebView
    private lateinit var searchView: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val viewModel: MyViewModel by activityViewModels()

    companion object {
        const val URL_TAG = "saved_url"
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        drawer = view.findViewById(R.id.dl_fh_drawer)
        val navView = view.findViewById<NavigationView>(R.id.nv_fh_menu_panel)
        val navHeader = navView.getHeaderView(0)
        webView = view.findViewById(R.id.wv_fh_web_view)
        progressBar = view.findViewById(R.id.pb_fh_progress)
        val backButton = navHeader.findViewById<ImageView>(R.id.iv_mdh_back)
        val refreshButton = navHeader.findViewById<ImageView>(R.id.iv_mdh_refresh)
        val shareButton = navHeader.findViewById<ImageView>(R.id.iv_mdh_share)
        val closeButton = navHeader.findViewById<ImageView>(R.id.iv_mdh_close)
        searchView = view.findViewById(R.id.sv_fh_search)
        val menuButton = view.findViewById<ImageView>(R.id.iv_fh_menu)
        comm = activity as Communicator
        sharedPreferences = Preferences.getPreferences(requireContext())
        editor = Preferences.getEditor(requireContext())
        setTheme()
        menuButton.setOnClickListener {
            drawer.openDrawer(GravityCompat.END)
        }
        closeButton.setOnClickListener {
            drawer.closeDrawer(GravityCompat.END)
        }
        backButton.setOnClickListener {
            onBackPressed()
            drawer.closeDrawer(GravityCompat.END)
        }
        refreshButton.setOnClickListener {
            webView.reload()
            drawer.closeDrawer(GravityCompat.END)
        }
        shareButton.setOnClickListener {
            webView.url?.let { url ->
                comm.shareUrl(url)
                Logger.debugger(url)
            }
            drawer.closeDrawer(GravityCompat.END)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                onSubmitClick(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.i_hmd_new_tab -> {
                    TODO("not yet implemented")
                }
                R.id.i_hmd_new_private_tab -> {
                    TODO("not yet implemented")
                }
                R.id.i_hmd_history -> {
                    comm.openHistory()
                }
                R.id.i_hmd_clear_history -> {
                    viewModel.clearHistory()
                }
                R.id.i_hmd_add_bookmark -> {
                    webView.url?.let { url ->
                        comm.openAddBookmarkBottomSheet(url)
                    }
                }
                R.id.i_hmd_open_tab -> {
                    TODO("not yet implemented")
                }
                R.id.i_hmd_settings -> {
                    comm.openSettings()
                }
                R.id.i_hmd_exit -> {
                    comm.exit()
                }
            }
            drawer.closeDrawer(GravityCompat.END)
            true
        }
        initialize()
        return view
    }

    private fun formatQuery(query: String?): String? {
        var url = query
        if (url != null) {
            if (!url.startsWith("http://") && !url.startsWith("https://") && !url.contains(".")) {
                url = "https://www.google.com/search?q=$url"
            }
        }
        return url
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onSubmitClick(query: String?) {
        val url = formatQuery(query)
        if (url != null) {
            performWebSearch(url)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun performWebSearch(url: String) {
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
                webView.visibility = View.INVISIBLE
                searchView.setQuery(webView.url, false);
//                urlField.startIconDrawable = BitmapDrawable(favicon)
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                progressBar.visibility = View.INVISIBLE
                webView.visibility = View.VISIBLE
            }
        }
        webView.loadUrl(url)
        editor.putString(URL_TAG, url).apply()
        if (url != "" && url != "about:blank") {
            Logger.debugger("Url is: $url")
            viewModel.insertWebPage(
                WebPage(
                    id = 0,
                    name = "",
                    time = LocalDateTime.now().toString(),
                    url = url,
                    type = "history"
                )
            )
        }
        Logger.debugger(sharedPreferences.getString(URL_TAG, "dump")!!)
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)
        webView.settings.displayZoomControls = true
    }

    override fun onBackPressed(): Boolean {
        if (webView.canGoBack()) {
            webView.goBack()
            return PressEventHandler.WAIT
        } else {
            return PressEventHandler.CONTINUE
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initialize() {
        setUpWebView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpWebView() {
        if (sharedPreferences.getBoolean(SettingsActivity.LAUNCH_TAG, false)) {
            performWebSearch(sharedPreferences.getString(URL_TAG, "about:blank")!!)
        } else {
            performWebSearch("about:blank")
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setTheme() {
        when (Theme.getTheme()) {
            Theme.TAKO -> {
                progressBar.indeterminateDrawable =
                    requireActivity().resources.getDrawable(R.drawable.tako_progress_bar)
                webView.verticalScrollbarThumbDrawable =
                    requireActivity().resources.getDrawable(R.drawable.tako_scroll_bar)
            }
            Theme.MIDNIGHT -> {
                progressBar.indeterminateDrawable =
                    requireActivity().resources.getDrawable(R.drawable.midnight_progress_bar)
                webView.verticalScrollbarThumbDrawable =
                    requireActivity().resources.getDrawable(R.drawable.midnight_scroll_bar)
            }
        }
    }
}