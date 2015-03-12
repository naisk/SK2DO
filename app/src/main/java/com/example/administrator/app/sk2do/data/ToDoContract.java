package com.example.administrator.app.sk2do.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by Administrator on 26/02/2015.
 */
public class ToDoContract {
    public static final String CONTENT_AUTHORITY = "com.example.administrator.app.sk2do";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TABLE = "tab";

    public static final class ToDoEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TABLE).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TABLE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TABLE;

        public static final String TABLE_NAME = "tab";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_SUBJECT = "subject";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_POINT = "points";


        public static Uri buildTableContentUri(long id){

            return ContentUris.withAppendedId(CONTENT_URI, id);

        }
    }
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day

        Time time = new Time();
        time.setToNow();
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);

    }
}
