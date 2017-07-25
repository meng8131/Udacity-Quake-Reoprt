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

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    /** Sample request url for a USGS query */
    private static final String SAMPLE_REQUS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(SAMPLE_REQUS_URL);
    }


        class EarthquakeAsyncTask extends AsyncTask<String, Void, List<QuakeInfoClass>>{
            @Override
            protected List<QuakeInfoClass> doInBackground(String... urls) {
                if (urls.length<1 || urls ==null){
                    return null;
                }
                List<QuakeInfoClass> earthquakeList = QueryUtils.fetchEarthquakeData(urls[0]);
                return earthquakeList;
            }

            @Override
            protected void onPostExecute(List<QuakeInfoClass> earthquakeList) {
                List<QuakeInfoClass> quakeList = earthquakeList;

                // Find the ListView to set adapter
                ListView earthquakeListView = (ListView) findViewById(R.id.list);

                //inflate customized adapter
                final QuakeInfoAdapter quakeInfoAdapter = new QuakeInfoAdapter(EarthquakeActivity.this, quakeList);

                //set the adapter
                earthquakeListView.setAdapter(quakeInfoAdapter);

                //setup click listener and intent
                AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        QuakeInfoClass currentQuake = quakeInfoAdapter.getItem(i);
                        Intent openURL = new Intent(Intent.ACTION_VIEW,Uri.parse(currentQuake.getURL()));
                        openURL.addCategory(Intent.CATEGORY_BROWSABLE);
                        startActivity(openURL);
                    }
                };
                earthquakeListView.setOnItemClickListener(onItemClickListener);
            }
        }

}