package com.example.ft

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class Tracking_Activity : AppCompatActivity() {

    lateinit var webView: WebView
    lateinit var etTracking: TextInputEditText
    lateinit var btnTrack: Button

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tracking)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        webView = findViewById(R.id.webView)
        etTracking = findViewById(R.id.etTracking)
        btnTrack = findViewById(R.id.btnTrack)

        // ✅ WebView Settings
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true

        webView.webViewClient = WebViewClient()

        // Default page load (optional)
//        webView.loadUrl("https://www.track-trace.com")

        btnTrack.setOnClickListener {

            val trackingNumber = etTracking.text.toString().trim()

            if (trackingNumber.isEmpty()) {
                Toast.makeText(this, "Enter Tracking Number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Track-Trace URL (search query)
            val url = if (trackingNumber.contains("-")) {
                "https://www.track-trace.com/aircargo#${trackingNumber}"
            } else {
                "https://www.track-trace.com/bol#${trackingNumber}"
            }

            webView.loadUrl(url)
        }
    }
    // 🔙 Back press handle (WebView navigation)
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}