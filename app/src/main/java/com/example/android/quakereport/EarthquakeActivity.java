/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>>{

    /** Sample request url for a USGS query */
    private static final String SAMPLE_REQUS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    //Constant value for Loadmanager ID
    private static final int EARTHQUAKE_LOADER_ID = 1;

    private EarthquakeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG,"Earthquake Activity onCreate() method is called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find the ListView to set adapter
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        //inflate customized adapter
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        //set the adapter
        earthquakeListView.setAdapter(mAdapter);

        //setup click listener and intent
        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Earthquake currentQuake = mAdapter.getItem(i);
                Intent openURL = new Intent(Intent.ACTION_VIEW,Uri.parse(currentQuake.getURL()));
                openURL.addCategory(Intent.CATEGORY_BROWSABLE);
                startActivity(openURL);
            }
        };
        earthquakeListView.setOnItemClickListener(onItemClickListener);

        LoaderManager loaderManager = getLoaderManager();

        Log.i(LOG_TAG,"initLoader() method is called");
        //Initialize Loader
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);

    }


    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG,"onCreateLoader() method is called");
        //create new loader using given url
        return new EarthquakeLoader(this, SAMPLE_REQUS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        Log.i(LOG_TAG,"onLoadFinish() method is called");

        //clear previous adapter data
        mAdapter.clear();

        //add earthquake data to adapter's data set
        if (earthquakes != null && !earthquakes.isEmpty()){
            mAdapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.i(LOG_TAG,"onLoaderReset() method is called");
        mAdapter.clear();
    }
}
