package com.fireflylearning.tasksummary.activities.TaskDetail.presenter;

import com.fireflylearning.tasksummary.objects.Task;

import java.util.Comparator;

/**
 * Created by javie on 14/01/2018.
 */

public class TaskSetComparator implements Comparator<Task> {
    public int compare(Task left, Task right) {
        if (left.set == null) return -1;
        if (right.set == null) return 1;

        return left.set.compareTo(right.set);
    }
}
