package com.fireflylearning.tasksummary.activities.tasksList.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fireflylearning.tasksummary.R;
import com.fireflylearning.tasksummary.activities.tasksList.presenter.TaskListPresenter;
import com.fireflylearning.tasksummary.objects.Task;
import com.fireflylearning.tasksummary.adapters.TaskListAdapter;
import java.util.ArrayList;

public class TaskListActivity extends AppCompatActivity {

    TextView emptyTextView;
    ListView listView;

    TaskListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_list);
        presenter = new TaskListPresenter(this, this);

        emptyTextView = (TextView) findViewById(R.id.empty);
        listView = (ListView) findViewById(R.id.list);

        showStatus(true, R.string.task_list_loading);

        showTasksList();
    }

    private void showTasksList() {
        presenter.getTaskList();
    }

    public void loadAdapter(ArrayList<Task> taskArray) {
        showStatus(taskArray.size() == 0, R.string.task_list_no_tasks);

        TaskListAdapter adapter = new TaskListAdapter(this, taskArray);

        listView.setAdapter(adapter);
    }

    public void showDataError(String message) {
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
}
