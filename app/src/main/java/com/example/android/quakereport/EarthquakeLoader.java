package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by lyman on 2017/8/4.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    //Query URL
    private String mURL;

    //Constructor
    public EarthquakeLoader(Context context, String url){
        super(context);
        mURL = url;
    }


    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG,"onStartLoading() method is called");
        forceLoad();
    }

    //background thread

    @Override
    public List<Earthquake> loadInBackground() {
        Log.i(LOG_TAG,"loadInBackground() method is called");
        if (mURL == null) {
            return null;
        }
        //perform network request, parse response and extract earthquake list
        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(mURL);
        return earthquakes;
    }
}
