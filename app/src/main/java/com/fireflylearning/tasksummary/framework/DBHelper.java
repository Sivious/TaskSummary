package com.fireflylearning.tasksummary.framework;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by javie on 11/01/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DBTasksSummary";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = String.format("create table tasks (id integer primary key, title text, description_page_url text, set_date text, due_date text, archived boolean, draft boolean, show_in_markbook boolean, highlight_in_markbook boolean, show_in_parent_portal boolean, hide_addressees boolean);");

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        database.execSQL("DROP TABLE IF EXISTS Tasks");
        onCreate(database);
    }
}
