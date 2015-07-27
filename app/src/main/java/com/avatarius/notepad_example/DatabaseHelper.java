package com.avatarius.notepad_example;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper {

    private static final String DATABASE_NAME = "notepade_example_database_1";
    private static final String TABLE_NAME = "notepad_example_notes";
    private static final int DATABASE_VERSION = 1;

    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";

    private Context context;
    private Helper mHelper;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        this.context = context;
    }

    private class Helper extends SQLiteOpenHelper {

        private Helper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + TABLE_NAME + " (" + KEY_ID + " integer primary key " +
                    "autoincrement, " + KEY_TITLE + " text not null, " + KEY_CONTENT + " text not null);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop if exists " + TABLE_NAME);
        }
    }

    public DatabaseHelper open() {
        mHelper = new Helper(context);
        mDatabase = mHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mHelper.close();
    }

    public long addRecord(String title, String content) {
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_CONTENT, content);
        return mDatabase.insert(TABLE_NAME, null, values);
    }

    public boolean deleteRecord(long id) {
        return mDatabase.delete(TABLE_NAME, KEY_ID + "=" + id, null) > 0;
    }

    public void deleteAllRecord() {
        mHelper.onUpgrade(mDatabase, DATABASE_VERSION, DATABASE_VERSION);
    }

    public Cursor getAllRecords() {
        Cursor mCursor = mDatabase.query(TABLE_NAME, new String[] {KEY_ID, KEY_TITLE, KEY_CONTENT}, null,
                null, null, null, null);
        return mCursor;
    }

    public Cursor getRecord(long id) {
        Cursor mCursor = mDatabase.rawQuery("select * from " + TABLE_NAME + " where " +
                KEY_ID + "=" + id, null);
        return mCursor;
    }

    public boolean updateNote(long id, String content) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_CONTENT, content);
        String title = "";
        if (content.length() > 23) {
            title = content.substring(0, 23);
        } else {
            title = content;
        }
        values.put(DatabaseHelper.KEY_TITLE, title);
        return mDatabase.update(TABLE_NAME, values, KEY_ID + "=" + id, null) > 0;
    }



}
