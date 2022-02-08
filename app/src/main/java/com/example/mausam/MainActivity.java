package com.example.mausam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static  final String LOG_TAG =MainActivity.class.getName();

    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText =(EditText)findViewById(R.id.cityValue);
    }

    public void nextPage(View view) {

        String city = editText.getText().toString();
        Intent i =new Intent(getApplicationContext(),WeatherActivity.class);
        i.putExtra("CityFromUser",city);
        startActivity(i);
    }


}