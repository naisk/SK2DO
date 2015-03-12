package com.example.administrator.app.sk2do.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrator.app.sk2do.data.ToDoContract.ToDoEntry;

/**
 * Created by Administrator on 26/02/2015.
 */
public class ToDoDbHelper extends SQLiteOpenHelper{

        // If you change the database schema, you must increment the database version.
        private static final int DATABASE_VERSION = 2;

        public static final String DATABASE_NAME = "todo.db";

        public ToDoDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            final String SQL_CREATE_TODO_TABLE =
                    "CREATE TABLE "+ ToDoEntry.TABLE_NAME + " ("
                            + ToDoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + ToDoEntry.COLUMN_NAME + " TEXT NOT NULL, "
                            + ToDoEntry.COLUMN_DESC + " TEXT , "
                            + ToDoEntry.COLUMN_DATE + " REAL NOT NULL, "
                            + ToDoEntry.COLUMN_SUBJECT + " TEXT NOT NULL, "
                            + ToDoEntry.COLUMN_CATEGORY + " TEXT NOT NULL, "
                            + ToDoEntry.COLUMN_POINT + " TEXT NOT NULL, "
                            + ToDoEntry.COLUMN_DIFFICULTY + " TEXT NOT NULL "
                            + " );";

            //sqLiteDatabase.execSQL("DROP TABLE "+ToDoEntry.TABLE_NAME);
            sqLiteDatabase.execSQL(SQL_CREATE_TODO_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            // Note that this only fires if you change the version number for your database.
            // It does NOT depend on the version number for your application.
            // If you want to update the schema without wiping data, commenting out the next 2 lines
            // should be your top priority before modifying this method.
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ToDoEntry.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
}
