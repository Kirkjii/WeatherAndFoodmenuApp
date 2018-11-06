package com.example.jaska.mdc_harjoitustyo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void onFoodMenuClick(View view)
    {
        Intent i = new Intent(this, FoodmenuActivity.class);
        startActivity(i);
    }

    public void onWeatherClick(View view)
    {
        Intent i = new Intent(this, WeatherActivity.class);
        startActivity(i);
    }


}
