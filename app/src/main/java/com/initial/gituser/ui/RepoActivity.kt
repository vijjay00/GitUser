package com.initial.gituser.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.initial.gituser.R
import kotlinx.android.synthetic.main.activity_repo.*

class RepoActivity : AppCompatActivity() {

    private lateinit var names: String
    private lateinit var url: String

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo)

        val b = intent.extras
        if (b != null) {
            names = b.getString("name")!!
            url = b.getString("url")!!
        }

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = names
        }

        val settings: WebSettings = webview.settings
        settings.javaScriptEnabled = true

        webview.webViewClient = WebViewClient()
        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                progressbar.setVisibility(View.GONE)
                webview.setVisibility(View.VISIBLE)
            }
        }
        webview.loadUrl(url)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
