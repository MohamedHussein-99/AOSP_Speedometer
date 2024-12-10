package com.example.speedometerfinal.View

import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.speedometerfinal.R
import android.view.View
import android.webkit.WebChromeClient
import android.widget.ImageView

class WebActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var imageView3: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        // Initialize the WebView
        webView = findViewById(R.id.webView)!!
        imageView3 = findViewById(R.id.imageView3)!!

        // Configure WebView settings
        webView.settings.apply {
            javaScriptEnabled = true // Enable JavaScript
            domStorageEnabled = true // Enable DOM storage
            cacheMode = WebSettings.LOAD_DEFAULT // Load default cache
            mediaPlaybackRequiresUserGesture = false // Allow autoplay for media
        }

        // Set WebViewClient to handle navigation within WebView
        webView.webViewClient = WebViewClient()

        // Set WebChromeClient for advanced video handling (fullscreen)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                // Enter fullscreen
                view?.let {
                    supportActionBar?.hide()
                    setContentView(view)
                }
            }

            override fun onHideCustomView() {
                // Exit fullscreen
                supportActionBar?.show()
                setContentView(R.layout.activity_main)
            }
        }

        // Load the initial URL
        webView.loadUrl("https://www.google.com/")

        // ImageView click listener to go back to MainActivity
        imageView3.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}