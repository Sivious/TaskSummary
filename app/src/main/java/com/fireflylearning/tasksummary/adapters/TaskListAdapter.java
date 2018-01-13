package com.fireflylearning.tasksummary.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fireflylearning.tasksummary.R;
import com.fireflylearning.tasksummary.activities.TaskDetailActivity;
import com.fireflylearning.tasksummary.logic.common.Constants;
import com.fireflylearning.tasksummary.objects.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Ben on 14/07/2017.
 */

public class TaskListAdapter extends ArrayAdapter<Task> {
    Context context;

    private static class ViewHolder {
        TextView title;
        TextView date;
        TextView flags;
        TextView txtDueDate;
        LinearLayout layoutArchived;
    }

    public TaskListAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);

        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View taskView;
        final ViewHolder viewHolder;

        if (convertView == null) {
            taskView = LayoutInflater.from(this.getContext()).inflate(R.layout.activity_task_list_row, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.title = taskView.findViewById(R.id.title);
            viewHolder.flags = taskView.findViewById(R.id.flags);
            viewHolder.date = taskView.findViewById(R.id.date);
            viewHolder.txtDueDate = taskView.findViewById(R.id.textview_task_list_row_due_date);
            viewHolder.layoutArchived = taskView.findViewById(R.id.linearlayout_task_list_row_archived);
            taskView.setTag(viewHolder);
        } else {
            taskView = convertView;
            viewHolder = (ViewHolder) taskView.getTag();
        }

        final Task task = getItem(position);

        if (task != null) {

            viewHolder.title.setText(task.title);
            viewHolder.flags.setText(task.toFlagsString());

            //SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE d MMM yyyy, h:mm a");
            //viewHolder.date.setText(task.due_date != null ? dateFormatter.format(task.set_date) : "");
            //viewHolder.txtDueDate.setText(task.due_date != null ? dateFormatter.format(task.due_date) : "");

            viewHolder.date.setText(task.set_date != null ? task.set_date.toString(): "");
            viewHolder.txtDueDate.setText(task.due_date != null ? task.due_date.toString(): "");

            viewHolder.layoutArchived.setVisibility(setVisibility(task.archived));

            taskView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, TaskDetailActivity.class);

                    String message = task.description_page_url;
                    intent.putExtra(Constants.INTENT_URL, message);
                    context.startActivity(intent);
                }
            });
        }

        return taskView;
    }

    private int setVisibility(Boolean archived) {
        if (archived == null || !archived)
            return View.GONE;
        else
            return View.VISIBLE;
    }
}
