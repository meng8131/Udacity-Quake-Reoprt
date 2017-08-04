package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

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
        forceLoad();
    }

    //background thread

    @Override
    public List<Earthquake> loadInBackground() {
        if (mURL == null) {
            return null;
        }
        //perform network request, parse response and extract earthquake list
        List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(mURL);
        return earthquakes;
    }
}
