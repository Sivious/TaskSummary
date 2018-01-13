package com.fireflylearning.tasksummary.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.fireflylearning.tasksummary.FireflyRequestQueue;
import com.fireflylearning.tasksummary.R;
import com.fireflylearning.tasksummary.logic.database.TasksDB;
import com.fireflylearning.tasksummary.objects.Task;
import com.fireflylearning.tasksummary.adapters.TaskListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class TaskListActivity extends AppCompatActivity {

    TextView emptyTextView;
    ListView listView;
    TasksDB tasksDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_list);
        tasksDB = new TasksDB(this);

        emptyTextView = (TextView) findViewById(R.id.empty);
        listView = (ListView) findViewById(R.id.list);

        showStatus(true, R.string.task_list_loading);

        showTasksList();
    }

    private void showTasksList() {
        Cursor tasksCursor = null;

        if (tasksDB != null) {
            tasksCursor = tasksDB.selectRecords();
        }

        if (tasksCursor != null && tasksCursor.getCount() > 0) {
            loadListFromCursor(tasksCursor);
        } else {
            loadListFromAPI();
        }
    }

    private void loadListFromCursor(Cursor tasksCursor) {
        ArrayList<Task> tasksList =  createTasksListFromCursor(tasksCursor);
        loadAdapter(tasksList);
    }

    private ArrayList<Task> createTasksListFromCursor(Cursor tasksCursor) {
        ArrayList<Task> mArrayList = new ArrayList<Task>();

        for(tasksCursor.moveToFirst(); !tasksCursor.isAfterLast(); tasksCursor.moveToNext()) {
            mArrayList.add(new Task(tasksCursor.getInt(0), tasksCursor.getString(1), tasksCursor.getString(2), getFormatDate(tasksCursor.getString(3)), getFormatDate(tasksCursor.getString(4)), createBoolean(tasksCursor.getInt(5)), createBoolean(tasksCursor.getInt(6)), createBoolean(tasksCursor.getInt(7)), createBoolean(tasksCursor.getInt(8)), createBoolean(tasksCursor.getInt(9)), createBoolean(tasksCursor.getInt(10))));
        }

        return mArrayList;
    }

    private Boolean createBoolean(int anInt) {
        return anInt != 0;
    }

    private Date getFormatDate(String unformatDate) {
        Date date = null;

        if (TextUtils.isEmpty(unformatDate)){
            return date;
        }

        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z YYYY");
        try {
            date = format.parse(unformatDate);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("SIVI", e.getMessage());
            Log.e("SIVI", unformatDate);
        }

        return date;
    }

    private void loadListFromAPI() {
        FireflyRequestQueue.getInstance().RunGraphqlQuery(
                R.string.tasks_query,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            buildTaskList(response.getJSONArray("tasks"));
                        } catch (JSONException e) {
                            showDataError(e.getLocalizedMessage());
                        }
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showDataError(error.getLocalizedMessage());
                    }
                });
    }

    private void buildTaskList(JSONArray data) {

        Gson gson = new Gson();
        Log.d("SIVI", data.toString());

        ArrayList<Task> taskArray = gson.fromJson(data.toString(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        Collections.sort(taskArray, new TaskSetComparator());

        loadAdapter(taskArray);
        persistTaskList(taskArray);
    }

    private void loadAdapter(ArrayList<Task> taskArray) {
        showStatus(taskArray.size() == 0, R.string.task_list_no_tasks);

        TaskListAdapter adapter = new TaskListAdapter(this, taskArray);

        listView.setAdapter(adapter);
    }

    private void persistTaskList(ArrayList<Task> taskArray) {
        for (Task task : taskArray) {
            tasksDB.createRecords(task.id, task.title, task.description_page_url, task.set != null ? task.set.toString() : "", task.due != null ? task.due.toString() : "", task.archived, task.draft, task.show_in_markbook, task.highlight_in_markbook, task.show_in_parent_portal, task.hide_addressees);
        }
    }

    private void showDataError(String message) {
        showStatus(true, getBaseContext().getResources().getString(R.string.task_list_error) + ": " + message);
    }

    private void showStatus(Boolean visible, int statusResourceId) {
        showStatus(visible, getResources().getString(statusResourceId));
    }

    private void showStatus(Boolean visible, String status) {

        emptyTextView.setText(status);

        if (visible) {
            emptyTextView.setVisibility(View.VISIBLE);
        } else {
            emptyTextView.setVisibility(View.GONE);
        }
    }

    public class TaskSetComparator implements Comparator<Task> {
        public int compare(Task left, Task right) {
            if (left.set == null) return -1;
            if (right.set == null) return 1;

            return left.set.compareTo(right.set);
        }
    }
}
