package com.codepath.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * A SQLite database for to-do items
 */
public class TodoItemDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = TodoItemDatabaseHelper.class.getSimpleName();

    // Database Info
    private static final String DATABASE_NAME = "todoItemDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_ITEMS = "todoItems";

    // To-do Item table columns
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_TEXT = "text";

    // Singleton instance
    private static TodoItemDatabaseHelper sInstance;

    public static synchronized TodoItemDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new TodoItemDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private TodoItemDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // These is where we need to write create table statements.
    // This is called when database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS +
                "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_ITEM_TEXT + " TEXT" +
                ")";

        db.execSQL(CREATE_ITEMS_TABLE);
    }

    // This method is called when database is upgraded like
    // modifying the table structure,
    // adding constraints to database, etc
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
            onCreate(db);
        }
    }

    public ArrayList<TodoItem> getAllTodoItems() {
        ArrayList<TodoItem> todoItems = new ArrayList<>();

        // SELECT * FROM TABLE_ITEMS
        String ITEMS_SELECT_QUERY =
                String.format("SELECT * FROM %s", TABLE_ITEMS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(ITEMS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    TodoItem todoItem = new TodoItem(cursor.getInt(cursor.getColumnIndex(KEY_ITEM_ID)),
                                                     cursor.getString(cursor.getColumnIndex(KEY_ITEM_TEXT)));
                    todoItems.add(todoItem);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get todo items from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return todoItems;
    }

    // Returns a TodoItem corresponding to the new row, or null if the transaction failed
    public TodoItem addTodoItem(String itemText) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();
        TodoItem insertedItem = null;

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ITEM_TEXT, itemText);

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            long id = db.insertOrThrow(TABLE_ITEMS, null, values);
            insertedItem = new TodoItem(id, itemText);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add todo item to database");
        } finally {
            db.endTransaction();
        }

        return insertedItem;
    }

    // Returns updated TodoItem
    public TodoItem editTodoItem(TodoItem item, String newText) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_TEXT, newText);

        // Update to-do list item for list item with id item.id
        int rows = db.update(TABLE_ITEMS, values, KEY_ITEM_ID + " = ?",
                    new String[] { String.valueOf(item.id) });

        if (BuildConfig.DEBUG && rows != 1) {
            throw new RuntimeException();
        }

        return new TodoItem(item.id, newText);
    }

    public void deleteTodoItem(TodoItem item) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_ITEMS, KEY_ITEM_ID + " = ?", new String[] { String.valueOf(item.id) });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete todo item");
        } finally {
            db.endTransaction();
        }
    }

}
