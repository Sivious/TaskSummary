package com.fireflylearning.tasksummary.activities.taskDetail.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.webkit.WebView
import com.fireflylearning.tasksummary.R
import android.webkit.WebViewClient
import com.fireflylearning.tasksummary.activities.taskDetail.presenter.TaskDetailPresenterKotlin
import com.fireflylearning.tasksummary.logic.common.Constants


/**
 * Created by javie on 13/01/2018.
 */

class TaskDetailActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private var url: String = ""
    private val presenter: TaskDetailPresenterKotlin = TaskDetailPresenterKotlin()

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
        url = presenter.getUrl(intent.getStringExtra(Constants.INTENT_URL))

        //Some urls does not have detail descriptions
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}