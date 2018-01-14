package com.fireflylearning.tasksummary;

import android.support.test.runner.AndroidJUnit4;

import com.fireflylearning.tasksummary.objects.Task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.junit.Assert.assertNotEquals;

/**
 * Created by javie on 14/01/2018.
 */

@RunWith(AndroidJUnit4.class)
public class TaskInstrumentalTest {
    Task task;

    @Before
    public void initTests(){
        task = new Task(1, "Title", "page.aspx?id=2888&theme=minimal", new Date(), new Date(), false, false, false, false, false, false);
    }

    @Test
    public void addition_isCorrect() throws Exception {
        String flags = task.toFlagsString();
        assertNotEquals(flags, null);
    }
}
