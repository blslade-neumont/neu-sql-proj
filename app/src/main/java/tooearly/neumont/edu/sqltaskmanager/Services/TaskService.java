package tooearly.neumont.edu.sqltaskmanager.Services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import tooearly.neumont.edu.sqltaskmanager.Models.Task;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class TaskService extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dev";

    private static final String TASKS_TABLE_NAME = "tasks";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESC = "desc";
    private static final String KEY_COLOR = "color";
    private static final String KEY_TIME = "time_spent";

    public TaskService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TASKS_TABLE_NAME + "(" +
                       KEY_ID + " integer primary key, " +
                       KEY_NAME + " varchar(80), " +
                       KEY_DESC + " text, " +
                       KEY_COLOR + " integer, " +
                       KEY_TIME + " integer" +
                   ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TASKS_TABLE_NAME + ";");
        onCreate(db);
    }

    public Task[] find(String where) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            return find(db, where);
        }
        finally {
            if (db != null) db.close();
        }
    }
    private Task[] find(SQLiteDatabase db, String where) {
        List<Task> tasks = new ArrayList<>();
        Cursor cursor = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select * from ").append(TASKS_TABLE_NAME);
            if (where != null && !where.isEmpty()) sb.append(" where ").append(where);
            sb.append(";");
            cursor = db.rawQuery(
                    sb.toString(),
                    null
            );
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.id = Integer.parseInt(cursor.getString(0));
                    task.name = cursor.getString(1);
                    task.description = cursor.getString(2);
                    task.color = Integer.parseInt(cursor.getString(3));
                    task.time_spent = Integer.parseInt(cursor.getString(4));
                    tasks.add(task);
                }
                while (cursor.moveToNext());
            }
        }
        finally {
            if (cursor != null) cursor.close();
        }
        return tasks.toArray(new Task[tasks.size()]);
    }

    public Task findById(int id) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            return findById(db, id);
        }
        finally {
            if (db != null) db.close();
        }
    }
    private Task findById(SQLiteDatabase db, int id) {
        Cursor cursor = null;
        try {
            cursor = db.query(
                    TASKS_TABLE_NAME,
                    new String[] {
                            KEY_ID, KEY_NAME, KEY_DESC, KEY_COLOR, KEY_TIME
                    },
                    KEY_ID + "=?",
                    new String[] { String.valueOf(id) },
                    null, //groupBy
                    null, //orderBy
                    null, //having
                    null //limit
            );
            if (cursor == null || !cursor.moveToFirst()) return null;
            Task task = new Task();
            task.id = Integer.parseInt(cursor.getString(0));
            task.name = cursor.getString(1);
            task.description = cursor.getString(2);
            task.color = Integer.parseInt(cursor.getString(3));
            task.time_spent = Integer.parseInt(cursor.getString(4));
            return task;
        }
        finally {
            if (cursor != null) cursor.close();
        }
    }

    public Task create(Task task) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            return create(db, task);
        }
        finally {
            if (db != null) db.close();
        }
    }
    private Task create(SQLiteDatabase db, Task task) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.name);
        values.put(KEY_DESC, task.description);
        values.put(KEY_COLOR, task.color);
        values.put(KEY_TIME, task.time_spent);
        long id = db.insert(TASKS_TABLE_NAME, null, values);
        if (id == -1) return null;
        return findById(db, (int)id);
    }

    public boolean update(Task task) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            return update(db, task);
        }
        finally {
            if (db != null) db.close();
        }
    }
    private boolean update(SQLiteDatabase db, Task task) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.name);
        values.put(KEY_DESC, task.description);
        values.put(KEY_COLOR, task.color);
        values.put(KEY_TIME, task.time_spent);
        int rowsUpdated = db.update(
                TASKS_TABLE_NAME,
                values,
                KEY_ID + "=?",
                new String[] { String.valueOf(task.id) }
        );
        return rowsUpdated != 0;
    }

    public boolean delete(Task task) {
        return delete(task.id);
    }
    public boolean delete(int id) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            return delete(db, id);
        }
        finally {
            if (db != null) db.close();
        }
    }
    private boolean delete(SQLiteDatabase db, int id) {
        int rowsDeleted = db.delete(
                TASKS_TABLE_NAME,
                KEY_ID + "=?",
                new String[] { String.valueOf(id) }
        );
        return rowsDeleted != 0;
    }
}
