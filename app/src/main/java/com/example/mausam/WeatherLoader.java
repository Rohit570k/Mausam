package com.example.mausam;

import android.content.Context;
import android.util.Log;


import android.content.AsyncTaskLoader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WeatherLoader extends AsyncTaskLoader<Weather> {

    /** Tag for log messages */
    private static final String LOG_TAG = WeatherLoader.class.getName();

    /** Query URL */
    private String mUrl;


    /**
     * Construct a new {@link WeatherLoader}.
     * @param context it is sent by the super to the base constructor
     * @param url url String it want for using in loadInBackground
     */
    public WeatherLoader(@NonNull Context context, String url) {
        super(context);
        mUrl=url;
    }
    @Override
    protected void onStartLoading() {

        Log.i(LOG_TAG,"TEST:EarthquakeLoader onStartLoading() called");
        forceLoad();
    }

    @Nullable
    @Override
    public Weather loadInBackground() {
        Log.i(LOG_TAG,"TEST:EarthquakeLoader loadInBackground() called");
        // Don't perform the request if there are no URLs, or the first URL is null
        if ((mUrl == null)) {
            return null;
        }

        Weather result = QueryUtils.fetchWeatherData(mUrl);
        return result;
    }
}
