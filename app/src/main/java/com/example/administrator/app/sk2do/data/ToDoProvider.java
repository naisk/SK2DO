package com.example.administrator.app.sk2do.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Administrator on 26/02/2015.
 */
public class ToDoProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private ToDoDbHelper mOpenHelper;

    static final int TODO = 100;
    static final int TODOWITHID = 101;






    /*
        Students: Here is where you need to create the UriMatcher. This UriMatcher will
        match each URI to the WEATHER, WEATHER_WITH_LOCATION, WEATHER_WITH_LOCATION_AND_DATE,
        and LOCATION integer constants defined above.  You can test this by uncommenting the
        testUriMatcher test within TestUriMatcher.
     */
    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ToDoContract.CONTENT_AUTHORITY;

        // 2) Use the addURI function to match each of the types.  Use the constants from
        // WeatherContract to help define the types to the UriMatcher.
        matcher.addURI(authority,ToDoContract.PATH_TABLE,TODO);
        matcher.addURI(authority,ToDoContract.PATH_TABLE+"/#",TODOWITHID);

        // 3) Return the new matcher!
        return matcher;
    }

    /*
        Students: We've coded this for you.  We just create a new WeatherDbHelper for later use
        here.
     */
    @Override
    public boolean onCreate() {
        mOpenHelper = new ToDoDbHelper(getContext());
        return true;
    }

    /*
        Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
        test this by uncommenting testGetType in TestProvider.

     */
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case TODO:
                return ToDoContract.ToDoEntry.CONTENT_TYPE;
            case TODOWITHID:
                return ToDoContract.ToDoEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "weather/*/*"
            case TODO:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ToDoContract.ToDoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TODOWITHID:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        ToDoContract.ToDoEntry.TABLE_NAME,
                        projection,
                        ToDoContract.ToDoEntry._ID + " =? ",
                        new String[]{Long.toString(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TODO: {
                //normalizeDate(values);
                long _id = db.insert(ToDoContract.ToDoEntry.TABLE_NAME, null, values);

                if ( _id > 0 )
                    returnUri = ToDoContract.ToDoEntry.buildTableContentUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri + _id);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Student: Start by getting a writable database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsDeleted;

        if(null == selection)selection = "1";
        switch (match){

            case TODO:
                rowsDeleted = db.delete(ToDoContract.ToDoEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case TODOWITHID:
            {
                rowsDeleted = db.delete(
                        ToDoContract.ToDoEntry.TABLE_NAME,
                        ToDoContract.ToDoEntry._ID + " =? ",
                        new String[]{Long.toString(ContentUris.parseId(uri))}
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
        // Student: Use the uriMatcher to match the WEATHER and LOCATION URI's we are going to
        // handle.  If it doesn't match these, throw an UnsupportedOperationException.

        // Student: A null value deletes all rows.  In my implementation of this, I only notified
        // the uri listeners (using the content resolver) if the rowsDeleted != 0 or the selection
        // is null.
        // Oh, and you should notify the listeners here.

        // Student: return the actual rows deleted
    }



    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsUpdated;

        switch (match){

            case TODO:
                rowsUpdated = db.update(ToDoContract.ToDoEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case TODOWITHID:
            {
                rowsUpdated = db.update(
                        ToDoContract.ToDoEntry.TABLE_NAME,
                        values,
                        ToDoContract.ToDoEntry._ID + " =? ",
                        new String[]{Long.toString(ContentUris.parseId(uri))}
                        );
                break;
            }

            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return  rowsUpdated;
        // Student: This is a lot like the delete function.  We return the number of rows impacted
        // by the update.
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TODO:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        //normalizeDate(value);
                        long _id = db.insert(ToDoContract.ToDoEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(ToDoContract.ToDoEntry.COLUMN_DATE)) {
            long dateValue = values.getAsLong(ToDoContract.ToDoEntry.COLUMN_DATE);
            values.put(ToDoContract.ToDoEntry.COLUMN_DATE, ToDoContract.normalizeDate(dateValue));
        }
    }
}
