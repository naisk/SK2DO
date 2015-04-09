package com.example.administrator.app.sk2do;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity implements MainFragment.Callback{
    private Uri mUri;
    private final String DETAILFRAGMENT_TAG = "DETAILFRAGMENT";
    private String bSortBy,bSortSeq,bCateFilter,bSubjFilter;
    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.detail_container)!=null){
            mTwoPane = true;
            if(savedInstanceState==null){
                //getSupportFragmentManager().beginTransaction().replace(R.id.detail_container,new DetailFragment(),DETAILFRAGMENT_TAG).commit();
            }
        }
        else
        {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
        //deleteDatabase("todo.db");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri contentUri){
        if(mTwoPane){
            mUri = contentUri;
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, contentUri);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container,fragment,DETAILFRAGMENT_TAG).commit();
        }
        else{
            Intent intent = new Intent(this,DetailActivity.class).setData(contentUri);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        String sortBy = Utility.getSortBy(this);
        String sortSeq = Utility.getSortSeq(this);
        String cateFilter = Utility.getFilterCate(this);
        String subjFilter = Utility.getFilterSubj(this);

        if(!(bSortBy==sortBy&&bSortSeq==sortSeq&&bCateFilter==cateFilter&&bSubjFilter==subjFilter)){

            MainFragment ff = (MainFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_main);
            ff.onPreferenceChanged();

            bSortBy = sortBy;
            bSortSeq = sortSeq;
            bSubjFilter = subjFilter;
            bCateFilter = cateFilter;

        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public void startAddActivity(View v){

        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }
    public void editData(View view){

        if(mUri!=null) {
            startActivity(new Intent(this, EditActivity.class).setData(mUri));
        }

    }
    public void deleteData(View view){

        if(mUri!=null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Deleting TODO")
                    .setMessage("Are you sure you want to delete?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            AsyncQuery asyncQuery = new AsyncQuery(getContentResolver());
                            asyncQuery.startDelete(-1,null,mUri,null,null);

                            //getContentResolver().delete(mUri,null,null);

                            DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
                            getSupportFragmentManager().beginTransaction().hide(df).commit();
                            mUri = null;
                        }
                    })


                    .show();
        }


        //ff.onPreferenceChanged();
    }

}
