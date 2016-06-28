package com.codepath.taskhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * TasksDatabaseHelper provides an interface to the SQLite database.
 *
 * The two main functions are used to write the list of Tasks to the database and read the list.
 *
 * @author Rowan-James Tran
 */
public class TasksDatabaseHelper extends SQLiteOpenHelper {
    private static TasksDatabaseHelper singleton;

    private static final String DB_NAME = "tasksDatabase";
    private static final int DB_VERSION = 1;

    private static final String TABLE_TASKS = "tasks";

    private static final String KEY_TASK_ID = "id";
    private static final String KEY_TASK_BODY = "body";
    private static final String KEY_TASK_PRIORITY = "priority";
    private static final String KEY_TASK_DATE = "date";

    private static final String COMMA_SEPARATOR = ",";
    private static final String OPEN_PARENTHESES = "(";
    private static final String CLOSE_PARENTHESES = ")";

    public void updateTasks(List<Task> tasks) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            resetDatabase(db, false);

            for (Task task : tasks) {
                ContentValues row = createContentValuesFromTask(task);

                db.insertOrThrow(TABLE_TASKS, null, row);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();

        String GET_TASKS = "SELECT * FROM " + TABLE_TASKS;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(GET_TASKS, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Task newTask = new Task();
                    newTask.body = cursor.getString(cursor.getColumnIndex(KEY_TASK_BODY));
                    newTask.priority = cursor.getInt(cursor.getColumnIndex(KEY_TASK_PRIORITY));
                    newTask.date = cursor.getLong(cursor.getColumnIndex(KEY_TASK_DATE));

                    tasks.add(newTask);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
                e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return tasks;
    }

    private ContentValues createContentValuesFromTask(Task task) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TASK_BODY, task.body);
        cv.put(KEY_TASK_PRIORITY, task.priority);
        cv.put(KEY_TASK_DATE, task.date);

        return cv;
    }

    public static synchronized TasksDatabaseHelper getInstance(Context context) {
        if (singleton == null) {
            singleton = new TasksDatabaseHelper(context);
        }

        return singleton;
    }

    private TasksDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS +
                OPEN_PARENTHESES +
                    KEY_TASK_ID + " INTEGER PRIMARY KEY" + COMMA_SEPARATOR +
                    KEY_TASK_BODY + " TEXT" + COMMA_SEPARATOR +
                    KEY_TASK_PRIORITY + " INTEGER" + COMMA_SEPARATOR +
                    KEY_TASK_DATE + " INTEGER" +
                CLOSE_PARENTHESES;

        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        resetDatabase(db, true);
    }

    private void resetDatabase(SQLiteDatabase db, boolean fullReset) {
        if (fullReset) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        } else {
            db.execSQL("DELETE FROM " + TABLE_TASKS);
        }
    }
}
