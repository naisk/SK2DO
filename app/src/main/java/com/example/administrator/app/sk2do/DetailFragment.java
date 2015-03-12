package com.example.administrator.app.sk2do;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.app.sk2do.data.ToDoContract;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 10/03/2015.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    TextView nameText;
    TextView cateText;
    TextView subjText;
    TextView dateText;
    TextView pointText;
    TextView diffText;
    TextView descText;

    private Uri mUri;

    private static final int DETAIL_LOADER = 0;

    static final String DETAIL_URI = "URI";

    static final int COL_TODO_ID = 0;
    static final int COL_TODO_NAME = 1;
    static final int COL_TODO_DESC = 2;
    static final int COL_TODO_CATE = 3;
    static final int COL_TODO_SUBJ = 4;
    static final int COL_TODO_DIFF = 5;
    static final int COL_TODO_POINT = 6;
    static final int COL_TODO_DATE = 7;

    private static final String[] projection = {
            ToDoContract.ToDoEntry._ID,
            ToDoContract.ToDoEntry.COLUMN_NAME,
            ToDoContract.ToDoEntry.COLUMN_DESC,
            ToDoContract.ToDoEntry.COLUMN_CATEGORY,
            ToDoContract.ToDoEntry.COLUMN_SUBJECT,
            ToDoContract.ToDoEntry.COLUMN_DIFFICULTY,
            ToDoContract.ToDoEntry.COLUMN_POINT,
            ToDoContract.ToDoEntry.COLUMN_DATE
    };



    public DetailFragment() {
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceStates){

        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceStates);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle arguments = getArguments();
        if(arguments!=null){
            mUri = arguments.getParcelable(DETAIL_URI);
        }
        nameText = (TextView)rootView.findViewById(R.id.nameDetailText);
        cateText = (TextView)rootView.findViewById(R.id.categoryDetailText);
        subjText = (TextView)rootView.findViewById(R.id.subjectDetailText);
        dateText = (TextView)rootView.findViewById(R.id.dateDetailText);
        pointText = (TextView)rootView.findViewById(R.id.pointDetailText);
        diffText = (TextView)rootView.findViewById(R.id.difficultyDetailText);
        descText = (TextView)rootView.findViewById(R.id.descDetailText);
        //detailTextView = (TextView)rootView.findViewById(R.id.detail_text);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){



        if(null!=mUri){
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    projection,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){



        if (cursor!=null && !cursor.moveToFirst()) {
            return;
        }

        Long milli = cursor.getLong(COL_TODO_DATE);
        Time time = new Time();
        time.set(milli);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        DatePickerActivity.DatePickerFragment.getDefaultDate(time.monthDay,time.month,time.year);

        String uxStr = cursor.getString(COL_TODO_ID)+
                cursor.getString(COL_TODO_NAME)+
                cursor.getString(COL_TODO_DESC)+
                cursor.getString(COL_TODO_CATE)+
                cursor.getString(COL_TODO_SUBJ)+
                cursor.getString(COL_TODO_DIFF)+
                cursor.getString(COL_TODO_POINT)+
                dateFormat.format(milli);

        nameText.setText(cursor.getString(COL_TODO_NAME));
        cateText.setText(cursor.getString(COL_TODO_CATE));
        subjText.setText(cursor.getString(COL_TODO_SUBJ));
        dateText.setText(dateFormat.format(milli));
        pointText.setText(cursor.getString(COL_TODO_POINT));
        diffText.setText(cursor.getString(COL_TODO_DIFF));
        descText.setText(cursor.getString(COL_TODO_DESC));

        //detailTextView.setText(uxStr);

    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader){}


}
