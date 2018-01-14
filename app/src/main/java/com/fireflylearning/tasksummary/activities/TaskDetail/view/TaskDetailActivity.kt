package com.fireflylearning.tasksummary.activities.TaskDetail.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.WebView
import com.fireflylearning.tasksummary.R
import android.webkit.WebViewClient
import com.fireflylearning.tasksummary.FireflyRequestQueue
import com.fireflylearning.tasksummary.logic.common.Constants


/**
 * Created by javie on 13/01/2018.
 */

class TaskDetailActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private var url:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        setContentView(R.layout.activity_task_detail)

        init()
        loadUrl()
    }

    private fun loadUrl() {
        webView.loadUrl(url)

    }

    private fun init() {
        webView = findViewById<View>(R.id.webview_task_detail) as WebView
        webView.setWebViewClient(WebViewClient())
        webView.getSettings().setJavaScriptEnabled(true)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        val intent = intent
        url = FireflyRequestQueue.getInstance().createSession(intent.getStringExtra(Constants.INTENT_URL))

        Log.d("SIVIANES", "url: " + url)
        Log.d("SIVIANES", "data: " + intent.getStringExtra(Constants.INTENT_URL))

        //I really do not understand this. The URL is created exactly as documentation says but, it does not show anything in webview
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}