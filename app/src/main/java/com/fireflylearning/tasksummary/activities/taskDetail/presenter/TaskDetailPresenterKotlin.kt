package com.fireflylearning.tasksummary.activities.taskDetail.presenter

import com.fireflylearning.tasksummary.FireflyRequestQueue

/**
 * Created by javie on 14/01/2018.
 */
class TaskDetailPresenterKotlin {
    fun getUrl(path : String) : String{
        return FireflyRequestQueue.getInstance().createSession(path)
    }
}