package com.example.administrator.app.sk2do;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 28/02/2015.
 */
public class ToDoAdapter extends CursorAdapter {
    public ToDoAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */


    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    public static class ViewHolder {
        public final TextView nameView;
        public final TextView dateView;
        public final TextView categoryView;
        public final TextView subjectView;

        public ViewHolder(View view) {

            nameView = (TextView) view.findViewById(R.id.list_item_name_textview);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            categoryView = (TextView) view.findViewById(R.id.list_item_category_textview);
            subjectView = (TextView) view.findViewById(R.id.list_item_subject_textview);
        }
    }

    private String convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor
        Long milli = cursor.getLong(MainFragment.COL_TODO_DATE);
        Time time = new Time();
        time.set(milli);
        //time.format("dd/mm/yyyy");

        String uxStr = cursor.getString(MainFragment.COL_TODO_NAME)+
                cursor.getString(MainFragment.COL_TODO_CATE)+
                cursor.getString(MainFragment.COL_TODO_SUBJ)+
                " "+
                time.toString();


        return uxStr;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_todo, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.
        ViewHolder viewHolder = (ViewHolder)view.getTag();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");

        viewHolder.nameView.setText(cursor.getString(MainFragment.COL_TODO_NAME));
        viewHolder.dateView.setText(simpleDateFormat.format(cursor.getLong(MainFragment.COL_TODO_DATE)));
        viewHolder.categoryView.setText(cursor.getString(MainFragment.COL_TODO_CATE));
        viewHolder.subjectView.setText(cursor.getString(MainFragment.COL_TODO_SUBJ));
    }
}
