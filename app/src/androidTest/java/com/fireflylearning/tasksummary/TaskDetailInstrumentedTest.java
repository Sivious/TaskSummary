package com.fireflylearning.tasksummary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.fireflylearning.tasksummary.activities.taskDetail.presenter.TaskDetailPresenterKotlin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by javie on 14/01/2018.
 */


@RunWith(AndroidJUnit4.class)
public class TaskDetailInstrumentedTest {

    TaskDetailPresenterKotlin presenterKotlin;
    Context appContext;

    @Before
    public void setUp() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();
        presenterKotlin = new TaskDetailPresenterKotlin();

        FireflyRequestQueue.initialise(appContext, "appdev.tryfirefly.com", "secret");
    }

    @Test
    public void checkPathEmptyString() throws Exception {
        String url = presenterKotlin.getUrl("");
        org.junit.Assert.assertNotEquals(url, null);
    }

    @Test
    public void checkPathNotEmptyString() throws Exception {
        String url = presenterKotlin.getUrl("/page.aspx?id=2888&theme=minimal");
        org.junit.Assert.assertTrue(url.contains("/page.aspx?id=2888&theme=minimal"));
    }
}
