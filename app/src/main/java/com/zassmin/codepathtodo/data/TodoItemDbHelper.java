package com.zassmin.codepathtodo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zassmin on 6/12/15.
 */
public class TodoItemDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todoListDatabase";
    private static final String TABLE_TODO = "todo_items";

    // table column names
    private static final String KEY_ID = "id";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_ITEM_NAME = "item_name";

    public TodoItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_TODO = "CREATE TABLE " + TABLE_TODO + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ITEM_NAME + " TEXT,"
                + KEY_PRIORITY + " INTEGER" + ")";
        db.execSQL(CREATE_TABLE_TODO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
            onCreate(db);
        }
    }

    // this will replace the writer
    public void addTodoItem(TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, item.getItemName());
        values.put(KEY_PRIORITY, item.getPriority());
        long rowId = db.insertOrThrow(TABLE_TODO, null, values);
        db.close();
    }

    // this will replace finding by position
    // instance.find(id)
    public TodoItem getTodoItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TODO,
                new String[] { KEY_ID, KEY_ITEM_NAME, KEY_PRIORITY },
                KEY_ID + "= ?", new String[] { String.valueOf(id) },
                null, null, "id ASC", "100");
        if (cursor != null) {
            cursor.moveToFirst();
        }

        // load result into item object
        TodoItem item = new TodoItem(cursor.getString(1), cursor.getInt(1));
        item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));

        if (cursor != null) {
            cursor.close();
        }

        // return object
        return item;
    }

    // this gets us the count of the entire list
    public int getTodoItemCount() {
        String countQuery = "SELECT * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    // this will replace getting the entire list
    public List<TodoItem> getAllTodoItems() {
        List<TodoItem> todoItems = new ArrayList<TodoItem>();

        String selectQuery = "SELECT * FROM " + TABLE_TODO;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // create item object for each and add to list
        if (cursor.moveToFirst()) {
            do {
                TodoItem item = new TodoItem(cursor.getString(1), cursor.getInt(2));
                item.setId(cursor.getInt(0));
                todoItems.add(item);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return todoItems;
    }

    // this will replace editing/updating the records
    public int updateTodoItem(TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues(); // why isn't this a list, Sting[]?
        values.put(KEY_ITEM_NAME, item.getItemName());
        values.put(KEY_PRIORITY, item.getPriority());
        int result = db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
        db.close();
        return result; // this is not an object anymore, is it?
    }

    // this will replace deleting/remove records
    public void deleteTodoItem(TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[] { String.valueOf(item.getId()) });
        db.close();
    }
}
