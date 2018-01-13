package com.fireflylearning.tasksummary.logic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fireflylearning.tasksummary.framework.DBHelper;

import java.util.Date;

/**
 * Created by javie on 12/01/2018.
 */

public class TasksDB {
    //region Variables
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public final static String TASKS_TABLE = "Tasks"; // name of table

    public final static String TASK_ID = "id"; // id value for employee
    public final static String TASK_TITLE = "title";  // name of employee
    public final static String TASK_DESCRIPTION_PAGE_URL = "description_page_url";  // name of employee
    public final static String TASK_SET = "set_date";  // name of employee
    public final static String TASK_DUE = "set_date";  // name of employee
    public final static String TASK_ARCHIVED = "archived";  // name of employee
    public final static String TASK_DRAFT = "draft";  // name of employee
    public final static String TASK_SHOW_IN_MARKBOOK = "show_in_markbook";  // name of employee
    public final static String TASK_HIGHLIGHT_IN_MARKBOOK = "highlight_in_markbook";  // name of employee
    public final static String TASK_SHOW_IN_PARENT_PORTAL = "show_in_parent_portal";  // name of employee
    public final static String TASK_HIDE_ADDRESSEES = "hide_addressees";  // name of employee
    //endregion

    /**
     * @param context
     */
    public TasksDB(Context context) {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long createRecords(int id, String title, String description_page_url, Date set, Date due,
                              Boolean archived, Boolean draft, Boolean show_in_markbook, Boolean highlight_in_markbook, Boolean show_in_parent_portal, Boolean hide_addressees) {

        ContentValues contentValues = new ContentValues();

        contentValues.put("id", id);
        contentValues.put("title", title);
        contentValues.put("description_page_url", description_page_url);
        contentValues.put("set_date", set.getTime());
        contentValues.put("due", due.getTime());
        contentValues.put("archived", archived);
        contentValues.put("draft", draft);
        contentValues.put("show_in_markbook", show_in_markbook);
        contentValues.put("highlight_in_markbook", highlight_in_markbook);
        contentValues.put("show_in_parent_portal", show_in_parent_portal);
        contentValues.put("hide_addressees", hide_addressees);

        return database.insert(TASKS_TABLE, null, contentValues);
    }

    public Cursor selectRecords() {
        String[] cols = new String[]{TASK_ID,
                TASK_TITLE,
                TASK_DESCRIPTION_PAGE_URL,
                TASK_SET,
                TASK_DUE,
                TASK_ARCHIVED,
                TASK_DRAFT,
                TASK_SHOW_IN_MARKBOOK,
                TASK_HIGHLIGHT_IN_MARKBOOK,
                TASK_SHOW_IN_PARENT_PORTAL,
                TASK_HIDE_ADDRESSEES};

        Cursor mCursor = database.query(true, TASKS_TABLE, cols, null
                , null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor; // iterate to get each value.
    }


}
