package com.example.administrator.app.sk2do;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.app.sk2do.data.ToDoContract;

import java.text.SimpleDateFormat;


public class EditActivity extends ActionBarActivity {

    static String savedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new EditFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class EditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

        private static final int EDIT_LOADER = 0;

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

        public EditFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.fragment_edit, container, false);

            Spinner spinCategory = (Spinner) rootView.findViewById(R.id.spinner_edit_category);
            Spinner spinSubject = (Spinner) rootView.findViewById(R.id.spinner_edit_subject);
            Spinner spinDifficulty = (Spinner) rootView.findViewById(R.id.spinner_edit_difficulty);
            Spinner spinPoints = (Spinner) rootView.findViewById(R.id.spinner_edit_points);


            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(getActivity(),
                    R.array.array_category, R.layout.spinner_item);

            ArrayAdapter<CharSequence> adapterSubject = ArrayAdapter.createFromResource(getActivity(),
                    R.array.array_subject, R.layout.spinner_item);

            ArrayAdapter<CharSequence> adapterDifficulty = ArrayAdapter.createFromResource(getActivity(),
                    R.array.array_difficulty, R.layout.spinner_item);

            ArrayAdapter<CharSequence> adapterPoints = ArrayAdapter.createFromResource(getActivity(),
                    R.array.array_points, R.layout.spinner_item);

            // Specify the layout to use when the list of choices appears
            adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            adapterSubject.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            adapterPoints.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinCategory.setAdapter(adapterCategory);
            spinSubject.setAdapter(adapterSubject);
            spinDifficulty.setAdapter(adapterDifficulty);
            spinPoints.setAdapter(adapterPoints);

            return rootView;
        }
        @Override
        public void onActivityCreated(Bundle savedInstanceStates){
            if(savedInstanceStates==null)getLoaderManager().initLoader(EDIT_LOADER,null,this);
            super.onActivityCreated(savedInstanceStates);
        }
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args){



            Intent intent = getActivity().getIntent();
            if(intent == null){
                return null;
            }

            return new CursorLoader(
                    getActivity(),
                    intent.getData(),
                    projection,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){



            if (!cursor.moveToFirst()) {
                return;
            }

            Long milli = cursor.getLong(COL_TODO_DATE);
            Time time = new Time();
            time.set(milli);



            EditText nameText = (EditText)getActivity().findViewById(R.id.nameEditText);
            EditText descText = (EditText)getActivity().findViewById(R.id.descEditText);
            Spinner cateSpinner = (Spinner)getActivity().findViewById(R.id.spinner_edit_category);
            Spinner subjSpinner = (Spinner)getActivity().findViewById(R.id.spinner_edit_subject);
            Spinner diffSpinner = (Spinner)getActivity().findViewById(R.id.spinner_edit_difficulty);
            Spinner pointSpinner = (Spinner)getActivity().findViewById(R.id.spinner_edit_points);
            TextView dateView = (TextView) getActivity().findViewById(R.id.textview_edit_date);

            String nameStr = cursor.getString(COL_TODO_NAME);
            String descStr = cursor.getString(COL_TODO_DESC);
            String cateStr = cursor.getString(COL_TODO_CATE);
            String subjStr = cursor.getString(COL_TODO_SUBJ);
            String diffStr = cursor.getString(COL_TODO_DIFF);
            String pointStr = cursor.getString(COL_TODO_POINT);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");


            nameText.setText(nameStr);
            descText.setText(descStr);

            int catePos = 0,subjPos = 0,diffPos = 0,pointPos = 0,i;

            for(i=0;i<3;i++){
                if(cateStr.equals(cateSpinner.getItemAtPosition(i))){catePos = i;}
            }
            for(i=0;i<7;i++){
                if(subjStr.equals(subjSpinner.getItemAtPosition(i))){subjPos = i;}
            }
            for(i=0;i<5;i++){
                if(diffStr.equals(diffSpinner.getItemAtPosition(i))){diffPos = i;}
            }
            for(i=0;i<6;i++){
                if(pointStr.equals(pointSpinner.getItemAtPosition(i))){pointPos = i;}
            }

            cateSpinner.setSelection(catePos);
            subjSpinner.setSelection(subjPos);
            diffSpinner.setSelection(diffPos);
            pointSpinner.setSelection(pointPos);

            dayy = Integer.toString(time.monthDay);
            monthh = Integer.toString(time.month);
            yearr = Integer.toString(time.year);


            dateView.setText(dateFormat.format(time.toMillis(false)));

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader){}
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerActivity.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    static String dayy,monthh,yearr;
    public static void getEditDateData(int day,int month,int year){

        dayy = Integer.toString(day);
        monthh = Integer.toString(month);
        yearr = Integer.toString(year);
    }
    public void getEditData(View view){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        Spinner Cate = (Spinner) findViewById(R.id.spinner_edit_category);
        Spinner Subj = (Spinner) findViewById(R.id.spinner_edit_subject);
        Spinner Diff = (Spinner) findViewById(R.id.spinner_edit_difficulty);
        Spinner Point = (Spinner)findViewById(R.id.spinner_edit_points);


        EditText Name = (EditText) findViewById(R.id.nameEditText);
        EditText Desc = (EditText) findViewById(R.id.descEditText);

        if(Name.getText().toString().length()==0){
            Toast toast = Toast.makeText(getApplicationContext(),"Please insert your TODO name", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        ContentValues values = new ContentValues();

        String cateStr = Cate.getSelectedItem().toString();
        String subjStr = Subj.getSelectedItem().toString();
        String diffStr = Diff.getSelectedItem().toString();
        String pointStr = Point.getSelectedItem().toString();

        String nameStr = Name.getText().toString();
        String descStr = Desc.getText().toString();

        Time time = new Time();
        long day = Long.parseLong(dayy,10);
        long month = Long.parseLong(monthh,10);
        long year = Long.parseLong(yearr,10);
        time.set((int)day,(int)month,(int)year);

        values.put(ToDoContract.ToDoEntry.COLUMN_NAME,nameStr);
        values.put(ToDoContract.ToDoEntry.COLUMN_DESC,descStr);
        values.put(ToDoContract.ToDoEntry.COLUMN_CATEGORY,cateStr);
        values.put(ToDoContract.ToDoEntry.COLUMN_SUBJECT,subjStr);
        values.put(ToDoContract.ToDoEntry.COLUMN_DIFFICULTY,diffStr);
        values.put(ToDoContract.ToDoEntry.COLUMN_POINT,pointStr);
        values.put(ToDoContract.ToDoEntry.COLUMN_DATE,time.toMillis(true));



        //textView.setText( nameStr + descStr  + dayy + "/" + monthh + "/" + yearr);
        AsyncQuery asyncQuery = new AsyncQuery(getContentResolver());
        asyncQuery.startUpdate(-1,null,getIntent().getData(),values,null,null);
        //getContentResolver().update(getIntent().getData(),values,null,null);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
