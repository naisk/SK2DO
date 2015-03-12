package com.example.administrator.app.sk2do;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.app.sk2do.data.ToDoContract;

import java.util.Vector;

/**
 * Created by Administrator on 10/03/2015.
 */
    public  class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

        public interface Callback{
            public void onItemSelected(Uri dateUri);
        }
        static final int COL_TODO_ID = 0;
        static final int COL_TODO_NAME = 1;
        static final int COL_TODO_CATE = 2;
        static final int COL_TODO_SUBJ = 3;
        static final int COL_TODO_DATE = 4;

        private static final String[] projection = {
                ToDoContract.ToDoEntry._ID,
                ToDoContract.ToDoEntry.COLUMN_NAME,
                ToDoContract.ToDoEntry.COLUMN_CATEGORY,
                ToDoContract.ToDoEntry.COLUMN_SUBJECT,
                ToDoContract.ToDoEntry.COLUMN_DATE
        };

        private  ToDoAdapter mToDoAdapter;

        private ListView mListView;
        private int mPosition = ListView.INVALID_POSITION;

        private static final String SELECTED_KEY = "selected_position";

        private static final int ID_LOADER = 0;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            mToDoAdapter = new ToDoAdapter(getActivity(),null,0);
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mListView = (ListView) rootView.findViewById(R.id.listview_forecast);
            mListView.setAdapter(mToDoAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView adapterView, View view, int position, long id){


                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                    if (cursor != null) {

                        ((Callback)getActivity()).onItemSelected(ContentUris.withAppendedId(ToDoContract.ToDoEntry.CONTENT_URI,id));
                    }
                    mPosition = position;
                }
            });

            if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
                // The listview probably hasn't even been populated yet.  Actually perform the
                // swapout in onLoadFinished.
                mPosition = savedInstanceState.getInt(SELECTED_KEY);
            }
            return rootView;
        }
        @Override
        public void onActivityCreated(Bundle savedInstanceState){

            getLoaderManager().initLoader(ID_LOADER,null,this);

            super.onActivityCreated(savedInstanceState);
        }
        @Override
        public void onSaveInstanceState(Bundle outState) {
            // When tablets rotate, the currently selected list item needs to be saved.
            // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
            // so check for that before storing.
            if (mPosition != ListView.INVALID_POSITION) {
                outState.putInt(SELECTED_KEY, mPosition);
            }
            super.onSaveInstanceState(outState);
        }
        @Override
        public Loader<Cursor> onCreateLoader(int i,Bundle bundle){

            String sortBy = Utility.getSortBy(getActivity());
            String sortSeq = Utility.getSortSeq(getActivity());
            String cateFilter = Utility.getFilterCate(getActivity());
            String subjFilter = Utility.getFilterSubj(getActivity());

            String sortOrder = sortBy + " " + sortSeq;

            String selection = "";
            Vector<String> Args = new Vector<String>();
            if(!cateFilter.equals("null")){
                selection += ToDoContract.ToDoEntry.COLUMN_CATEGORY + " = ?";
                Args.add(cateFilter);
            }
            if(!subjFilter.equals("null")){

                if(!cateFilter.equals("null")){
                    selection += " AND ";
                }
                selection += ToDoContract.ToDoEntry.COLUMN_SUBJECT + " = ?";
                Args.add(subjFilter);
            }

            if(selection.equals("")){
                selection = null;
            }

            String[] selectionArgs = null;
            if(Args!=null){
                selectionArgs = new String[Args.size()];
                Args.toArray(selectionArgs);
            }


            Uri todoUri = ToDoContract.ToDoEntry.CONTENT_URI.buildUpon().build();

            return new CursorLoader(getActivity(),
                    todoUri,
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor){

            mToDoAdapter.swapCursor(cursor);
            if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader){

            mToDoAdapter.swapCursor(null);

        }

        public void onPreferenceChanged(){
            getLoaderManager().restartLoader(ID_LOADER,null,this);
        }

    }


