package com.example.administrator.app.sk2do;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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

import com.example.administrator.app.sk2do.data.UpdateToDoTask;

import java.text.SimpleDateFormat;


public class AddActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AddFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_add, menu);
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
    public static class AddFragment extends Fragment {

        public AddFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_add, container, false);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            Time time = new Time();
            time.setToNow();
            DatePickerActivity.DatePickerFragment.getDefaultDate(time.monthDay,time.month,time.year);

            TextView dateText = (TextView) rootView.findViewById(R.id.textview_add_date);
            dateText.setText(dateFormat.format(time.toMillis(true)));

            Spinner spinCategory = (Spinner) rootView.findViewById(R.id.spinner_category);
            Spinner spinSubject = (Spinner) rootView.findViewById(R.id.spinner_subject);
            Spinner spinDifficulty = (Spinner) rootView.findViewById(R.id.spinner_difficulty);
            Spinner spinPoints = (Spinner) rootView.findViewById(R.id.spinner_points);


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

            time.setToNow();
            dayy = Integer.toString(time.monthDay);
            monthh = Integer.toString(time.month);
            yearr = Integer.toString(time.year);


            return rootView;
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerActivity.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }
    Time time = new Time();
    static String dayy,monthh,yearr;
    public static void getAddDateData(int day,int month,int year){

        dayy = Integer.toString(day);
        monthh = Integer.toString(month);
        yearr = Integer.toString(year);
    }

    public void getAddData(View view){

        Spinner Cate = (Spinner) findViewById(R.id.spinner_category);
        Spinner Subj = (Spinner) findViewById(R.id.spinner_subject);
        Spinner Diff = (Spinner) findViewById(R.id.spinner_difficulty);
        Spinner Point = (Spinner)findViewById(R.id.spinner_points);

        //TextView textView = (TextView) findViewById(R.id.add_textView);
        EditText Name = (EditText) findViewById(R.id.nameText);
        EditText Desc = (EditText) findViewById(R.id.descText);

        if(Name.getText().toString().length()==0){
            Toast toast = Toast.makeText(getApplicationContext(),"Please insert your TODO name", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        /*
        x = Long.parseLong("473",10)
        (x = 473L)
        */

        String cateStr = Cate.getSelectedItem().toString();
        String subjStr = Subj.getSelectedItem().toString();
        String diffStr = Diff.getSelectedItem().toString();
        String pointStr = Point.getSelectedItem().toString();

        String nameStr = Name.getText().toString();
        String descStr = Desc.getText().toString();


        //textView.setText(cateStr + subjStr + nameStr + descStr + diffStr + pointStr + dayy + "/" + monthh + "/" + yearr);
        //textView.setText( nameStr + descStr  + dayy + "/" + monthh + "/" + yearr);
        String[] param = {
                nameStr,
                descStr,
                cateStr,
                subjStr,
                diffStr,
                pointStr,
                dayy,
                monthh,
                yearr
        };

        UpdateToDoTask updateToDoTask = new UpdateToDoTask(this);

        updateToDoTask.execute(param);
        finish();
    }


}
