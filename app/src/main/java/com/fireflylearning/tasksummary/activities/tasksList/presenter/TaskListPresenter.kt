package com.fireflylearning.tasksummary.activities.tasksList.presenter

import android.content.Context
import android.database.Cursor
import android.text.TextUtils
import com.fireflylearning.tasksummary.FireflyRequestQueue
import com.fireflylearning.tasksummary.R
import com.fireflylearning.tasksummary.activities.taskDetail.presenter.TaskSetComparator
import com.fireflylearning.tasksummary.activities.tasksList.view.TaskListActivity
import com.fireflylearning.tasksummary.logic.database.TasksDB
import com.fireflylearning.tasksummary.objects.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by javie on 14/01/2018.
 */
class TaskListPresenter {
    var context : Context
    var view : TaskListActivity
    var tasksDB : TasksDB

    constructor(context: Context, view: TaskListActivity) {
        this.context = context
        this.view = view

        tasksDB = TasksDB(context)
    }

    fun getTaskList(){
        var tasksCursor: Cursor? = null

        if (tasksDB != null) {
            tasksCursor = tasksDB.selectRecords()
        }

        if (tasksCursor != null && tasksCursor.count > 0) {
            loadListFromCursor(tasksCursor)
        } else {
            loadListFromAPI()
        }
    }

    private fun loadListFromCursor(tasksCursor: Cursor) {
        val tasksList = createTasksListFromCursor(tasksCursor)
        view.loadAdapter(tasksList)
    }

    private fun loadListFromAPI() {
        FireflyRequestQueue.getInstance().RunGraphqlQuery(
                R.string.tasks_query,
                { response ->
                    try {
                        buildTaskList(response.getJSONArray("tasks"))
                    } catch (e: JSONException) {
                        view.showDataError(e.localizedMessage)
                    }
                }
        ) { error -> view.showDataError(error.localizedMessage) }
    }

    private fun createTasksListFromCursor(tasksCursor: Cursor): ArrayList<Task> {
        val mArrayList = ArrayList<Task>()

        tasksCursor.moveToFirst()
        while (!tasksCursor.isAfterLast) {
            mArrayList.add(Task(tasksCursor.getInt(0), tasksCursor.getString(1), tasksCursor.getString(2), getFormatDate(tasksCursor.getString(3)), getFormatDate(tasksCursor.getString(4)), createBoolean(tasksCursor.getInt(5)), createBoolean(tasksCursor.getInt(6)), createBoolean(tasksCursor.getInt(7)), createBoolean(tasksCursor.getInt(8)), createBoolean(tasksCursor.getInt(9)), createBoolean(tasksCursor.getInt(10))))
            tasksCursor.moveToNext()
        }

        return mArrayList
    }

    private fun getFormatDate(unformatDate: String): Date? {
        var date: Date? = null

        if (TextUtils.isEmpty(unformatDate)) {
            return date
        }

        val format = SimpleDateFormat("EEE MMM dd HH:mm:ss z YYYY")
        try {
            date = format.parse(unformatDate)
            println(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return date
    }

    private fun createBoolean(anInt: Int): Boolean {
        return anInt != 0
    }

    private fun buildTaskList(data: JSONArray) {
        val gson = Gson()

        val taskArray = gson.fromJson<ArrayList<Task>>(data.toString(), object : TypeToken<ArrayList<Task>>() {

        }.type)

        Collections.sort(taskArray, TaskSetComparator())

        view.loadAdapter(taskArray)
        persistTaskList(taskArray)
    }

    fun persistTaskList(taskArray: ArrayList<Task>) {
        for (task in taskArray) {
            tasksDB.createRecords(task.id, task.title, task.description_page_url, if (task.set != null) task.set.toString() else "", if (task.due != null) task.due.toString() else "", task.archived, task.draft, task.show_in_markbook, task.highlight_in_markbook, task.show_in_parent_portal, task.hide_addressees)
        }
    }

}