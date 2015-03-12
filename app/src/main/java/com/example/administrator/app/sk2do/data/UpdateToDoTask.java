package com.example.administrator.app.sk2do.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;


/**
 * Created by Administrator on 28/02/2015.
 */
public class UpdateToDoTask extends AsyncTask<String,Void,Void> {

    private final Context mContext;

    public UpdateToDoTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String[] params) {

        String nameStr = params[0];
        String descStr = params[1];
        String cateStr = params[2];
        String subjStr = params[3];
        String diffStr = params[4];
        String pointStr = params[5];

        String dayStr = params[6];
        String monthStr = params[7];
        String yearStr = params[8];

        long day = Long.parseLong(dayStr,10);
        long month = Long.parseLong(monthStr,10);
        long year = Long.parseLong(yearStr,10);

        Time time = new Time();
        time.set((int)day, (int)month, (int)year);  // set the date to Nov 4, 2007, 12am
        time.normalize(false);       // this sets isDst = 1
        //time.monthDay += 1;     // changes the date to Nov 5, 2007, 12am
        //millis = time.toMillis(false);   // millis is Nov 4, 2007, 11pm
        long dateTime = time.toMillis(true);    // millis is Nov 5, 2007, 12am

        //int julianStartDay = Time.getJulianDay(millis, time.gmtoff);
        //long dateTime = time.setJulianDay(julianStartDay);
        ContentValues Values = new ContentValues();


        Values.put(ToDoContract.ToDoEntry.COLUMN_NAME, nameStr);
        Values.put(ToDoContract.ToDoEntry.COLUMN_DESC,descStr);
        Values.put(ToDoContract.ToDoEntry.COLUMN_CATEGORY,cateStr);
        Values.put(ToDoContract.ToDoEntry.COLUMN_SUBJECT,subjStr);
        Values.put(ToDoContract.ToDoEntry.COLUMN_DIFFICULTY,diffStr);
        Values.put(ToDoContract.ToDoEntry.COLUMN_POINT,pointStr);
        Values.put(ToDoContract.ToDoEntry.COLUMN_DATE,dateTime);


        Uri insertUri =  mContext.getContentResolver().insert(ToDoContract.ToDoEntry.CONTENT_URI,Values);
        //long Id = ContentUris.parseId(insertUri);

        return null;
    }

}
