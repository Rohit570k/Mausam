package com.example.mausam;


import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Loader;
import android.app.LoaderManager.LoaderCallbacks;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity implements LoaderCallbacks<Weather> {

    String cityValue;
    /** TextView that is displayed when the list is empty */
//    TextView mEmptyStateTextView;
//    /** Adapter for the list of earthquakes */
    private ProgressBar loadingIndicator;


    private static final String DAY ="d";
    private static  final String LOG_TAG =WeatherActivity.class.getName();
    private static final String API_KEY ="4febf6082a3b5dbe322d92367d5df558";
    private static final String USGS_REQUEST_URL =
            "https://api.openweathermap.org/data/2.5/weather";

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int WEATHER_LOADER_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);

        Bundle bundle =getIntent().getExtras();
         cityValue =bundle.getString("CityFromUser");
         Log.i(LOG_TAG,"City value"+cityValue);
//
//        //To set an Empty View state
//        ConstraintLayout mainView = (ConstraintLayout)findViewById(R.id.mainView);
//        mEmptyStateTextView =(TextView)findViewById(R.id.empty_view);
//        mainView.setEmptyView(mEmptyStateTextView);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

       //  Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            android.app.LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            Log.i(LOG_TAG, "TEST: initLoader() calling...");
            loaderManager.initLoader(WEATHER_LOADER_ID, null, this);
        }
        else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator = findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);

//            // Update empty state with no connection error message
//            mEmptyStateTextView.setText("No Internet Connection");
        }
    }

    private void updateUi(Weather data) {
        Log.i(LOG_TAG,"TEST: updateUI() called");

        //Description
        TextView description = (TextView)findViewById(R.id.description);
        description.setText(data.getmDescription());

        //Humidity
        TextView humidity = (TextView)findViewById(R.id.humidity_name);
        humidity.setText(Integer.toString(data.getmHumidity()));

        //pressure
        TextView pressure = (TextView)findViewById(R.id.pressure_name);
        pressure.setText(Integer.toString(data.getmPressure()));

        //Wind
        TextView wind_speed =(TextView)findViewById((R.id.wind_name));
        wind_speed.setText(Double.toString(data.getmWind()));

        //Cityname
        TextView cityname =(TextView)findViewById(R.id.city_name_code);
        cityname.setText(data.getmName()+ ", "+ data.getmCityCode());

        //time of sunset n sunrise
        TextView temptime =(TextView)findViewById(R.id.temp_name);
        TextView temp_state =findViewById(R.id.temp_state);
        String icon = data.getmIcon();
        long sunrise=data.getmSunrise();
        long sunset=data.getmSunset();
        String setrise_time;
        if(icon.contains(DAY)){
             setrise_time= formatTime(sunset);
             temp_state.setText("Sunset");
        }else{
             setrise_time = formatTime(sunrise);
            temp_state.setText("Sunrise");
        }
        temptime.setText(setrise_time);


        //Temperature in degree celscius
        TextView temperature = (TextView)findViewById(R.id.temperature);
        double tempinC =data.getmTemp()-273.15;
        temperature.setText(String.format("%s ℃", formatTemperature(tempinC)));

        //Weather Image
        ImageView  iconImage = (ImageView)findViewById(R.id.image_weather);
        iconImage.setImageResource(getImageId(icon));


    }

    private String formatTemperature(double v) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0");
        return magnitudeFormat.format(v);
    }

    private int getImageId(String icon) {
        int weatherImageId;
        switch(icon){
            case "01d":
                weatherImageId= R.drawable.sunny;
                break;
            case "01n":
                weatherImageId= R.drawable.night;
                break;
            case "02d":
                weatherImageId= R.drawable.day;
                break;
            case "02n":
                weatherImageId= R.drawable.cloudy_night;
                break;
            case "03d":
                weatherImageId= R.drawable.cloudy;
                break;
            case "03n":
                weatherImageId= R.drawable.cloudy;
                break;
            case "04d":
                weatherImageId= R.drawable.perfectday;
                break;
            case "04n":
                weatherImageId= R.drawable.cloudy_night;
                break;
            case "09d":
                weatherImageId= R.drawable.rain;
                break;
            case "09n":
                weatherImageId= R.drawable.rainnight;
                break;
            case "10d":
                weatherImageId= R.drawable.rain;
                break;
            case "10n":
                weatherImageId= R.drawable.rainnight;
                break;
            case "11d":
                weatherImageId= R.drawable.storm;
                break;
            case "11n":
                weatherImageId= R.drawable.storm;
                break;
            case "13d":
                weatherImageId= R.drawable.mist;
                break;
            case "13n":
                weatherImageId= R.drawable.mist;
                break;
            case "50d":
                weatherImageId= R.drawable.snow;
                break;
            case "50n":
                weatherImageId= R.drawable.snow;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + icon);
        }
        return (weatherImageId);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(long time) {

        Date dateObject =new Date(time*1000);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    @Override
    public Loader<Weather> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG,"TEST: Callback onCreateLoader() called");
        Uri baseUri =Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        //Constructing Desired URL with the city name
        uriBuilder.appendQueryParameter("q",cityValue);
        uriBuilder.appendQueryParameter("appid",API_KEY);

        // Create a new loader for the custom URL
        return  new WeatherLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<Weather> loader, Weather data) {
        Log.i(LOG_TAG,"TEST: Callback onLoadFinished() called");


        loadingIndicator= findViewById(R.id.progress_bar);
        loadingIndicator.setVisibility(View.GONE);
//        // Set empty state text to display "No earthquakes found."
//        mEmptyStateTextView.setText(R.string.no_weather);

//        //Clear the laoder of the previous weather data
//        loader.isReset();

        if(data == null){return;}
        Log.i(LOG_TAG ,"Weather data "+data);
        updateUi(data);
    }

    @Override
    public void onLoaderReset(Loader<Weather> loader) {
        Log.i(LOG_TAG,"TEST: Callback onLoaderReset() called");

        // Loader reset, so we can clear out our existing data.
        loader.isReset();
    }

}
