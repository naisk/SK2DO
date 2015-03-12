package com.example.administrator.app.sk2do;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DatePickerActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DatePickerFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_date_picker, menu);
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

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        /*
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        Time time = new Time();

        static int day_default,month_default,year_default;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = year_default;
            int month = month_default;
            int day = day_default;

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            //TextView textView = (TextView)getActivity().findViewById(R.id.add_textView);

            //textView.setText("" + year + " " + month + " " + day);
            AddActivity.getAddDateData(day,month,year);
            EditActivity.getEditDateData(day,month,year);


            time.set(day,month,year);
            getDefaultDate(day,month,year);

            TextView addTextView = (TextView)getActivity().findViewById(R.id.textview_add_date);
            if(addTextView!=null)addTextView.setText(dateFormat.format(time.toMillis(true)));

            TextView editTextView = (TextView)getActivity().findViewById(R.id.textview_edit_date);
            if(editTextView!=null)editTextView.setText(dateFormat.format(time.toMillis(true)));
        }
        public static void getDefaultDate(int day,int month,int year){
            day_default = day;
            month_default = month;
            year_default = year;
        }


    }

}
