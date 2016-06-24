package com.codepath.todoapp;

import android.database.sqlite.SQLiteOpenHelper;

public class TasksDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "tasksDatabase";
    private static final int DB_VERSION = 1;

    private static final String TABLE_TASKS_NAME = "tasks";

    private static final String KEY_TASK_ID = "id";
    private static final String KEY_TASK_BODY = "body";
    private static final String KEY_TASK_PRIORITY = "priority";
    private static final String KEY_TASK_DATE = "date";
    
}
