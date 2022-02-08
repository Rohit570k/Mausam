package com.example.mausam;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Helper methods related to requesting and receiving Weather data from Open Weather api
 */
public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the  Open Weather api  and return a list of {@Link Weather} objects.
     */
    public static Weather fetchWeatherData(String requestUrl) {
        Log.i(LOG_TAG,"fetchWeatherData() called");
//        //       /*Sleep this thread for 2 sec */
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // Create URL object
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try{
            jsonResponse =makeHttpRequest(url);
            Log.i(LOG_TAG,"Json res" +jsonResponse);
        } catch (IOException e){
            Log.e(LOG_TAG,"Problem making http request ",e);
        }
        // Extract relevant field from json resonse and  instanstiate a {@link Weather}  object and store it
        Weather weathers= extractFeatureFromJson(jsonResponse);

        //Return the weathers object
        return weathers;
    }




    /**
     *
     * @param requestUrl take string url as a parameter
     * @return new URL object from the given request url
     */
    private static URL createUrl(String requestUrl) {
        URL url=null;
        try{
           url =new URL(requestUrl);
        }catch(MalformedURLException e){
            Log.e(LOG_TAG ,"Problem Building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     *
     * @param inputStream
     * @return
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static Weather extractFeatureFromJson(String weatherJSON) {
        //If JSON String is empty or null, it will return early
        if(TextUtils.isEmpty(weatherJSON)) {
            return null;
        }
        Weather weathers=null;

        try {
            // Create a JSONObject from the JSON_RESPONSE string
            JSONObject baseJsonResponse = new JSONObject(weatherJSON);

            String name = baseJsonResponse.getString("name");

            JSONObject sys  = baseJsonResponse.getJSONObject("sys");
            String countryCode = sys.getString("country");
            long sunrise =sys.getLong("sunrise");
            long sunset = sys.getLong("sunset");

            JSONObject main = baseJsonResponse.getJSONObject("main");
            double  temp = main.getDouble("temp");
            int humidity = main.getInt("humidity");
            int pressure  = main.getInt("pressure");

            JSONObject wind = baseJsonResponse.getJSONObject("wind");
            double wind_speed = wind.getDouble("speed");

            JSONArray weather =baseJsonResponse.getJSONArray("weather");
            JSONObject weatherArray =weather.getJSONObject(0);
            String description = weatherArray.getString("description");
            String icon = weatherArray.getString("icon");

            weathers =new Weather(name,countryCode,temp,description,
                    sunrise,sunset,humidity,wind_speed,pressure,icon);
        }catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the weather JSON results", e);
        }
        //Return the Weather object containing weather data
        return weathers;
    }

}
