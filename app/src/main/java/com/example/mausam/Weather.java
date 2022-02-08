package com.example.mausam;
/**
 * An {@link Weather} object contains information related to a single weather data.
 */
public class Weather {

    /** Name of the city*/
    private String mName;

    /** city code */
    private String mCityCode;

    /** Temperature in the Kelvin */
    private double mTemp;

    /** Description of the type of the weather */
    private String mDescription;

    /** Sunrise n Sunset time correspondingly */
    private  long mSunset ;

    /** Sunrise n Sunset time correspondingly */
    private  long mSunrise ;

    /** Humidity of the current city */
    private  int mHumidity;

    /** Wind speed */
    private double mWind;

   /** pressure in city current*/
    private int mPressure;

    /**ICON */
     private String mIcon;


    public Weather(String name ,String cityCode ,double temp,
                   String description ,long sunrise,long sunset, int humidity,
                   double wind, int pressure,String icon){
        mName =name;
        mCityCode =cityCode;
        mTemp =temp;
        mDescription = description;
        mSunrise = sunrise;
        mSunset = sunset;
        mHumidity=humidity;
        mWind =wind;
        mPressure =pressure;
        mIcon= icon;
    }

    /**
     * Returns the name of City
     */
    public String getmName() {
      return mName;
    }

    public String getmCityCode(){
        return mCityCode;
    }
    public double getmTemp(){
        return mTemp;
    }
    public String getmDescription(){
        return mDescription;
    }
    public long getmSunrise(){
        return mSunrise;
    }
    public long getmSunset(){
        return mSunset;
    }
    public int getmHumidity(){
        return mHumidity;
    }
    public double getmWind(){
        return mWind;
    }
    public int getmPressure(){
        return mPressure;
    }
    public  String getmIcon(){
        return  mIcon;
    }
}
