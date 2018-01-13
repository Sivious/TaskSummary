package com.fireflylearning.tasksummary.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebView
import com.fireflylearning.tasksummary.R

/**
 * Created by javie on 13/01/2018.
 */


class TaskDetailActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_task_detail)

        init()
        loadUrl()
    }

    private fun loadUrl() {
        webView.loadUrl("http://www.google.es")

    }

    private fun init() {
        webView = findViewById<View>(R.id.webview_task_detail) as WebView
    }


}