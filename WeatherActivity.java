package com.example.jaska.mdc_harjoitustyo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener, WeatherEngine.WeatherDataAvailableInterface
{

    TextView tempTv;
    EditText cityEd;
    ImageView weatherIconView;
    Button searchBtn;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    WeatherEngine weather = new WeatherEngine(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tempTv =  findViewById(R.id.textView);
        cityEd =  findViewById(R.id.editText);
        searchBtn = findViewById(R.id.button);

        searchBtn.setOnClickListener(this);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        cityEd.setText(pref.getString("asd", null));
    }


    @Override
    public void weatherDataAvailable() {
        WeatherActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUI();
            }
        });
    }

    @Override
    public void onClick(View v) {
        String city = cityEd.getText().toString();
        editor.putString("asd", city);
        editor.apply();
        weather.getWeatherData(city);
    }

    public void updateUI() {
        String temperature = String.format(getString(R.string.temp), weather.getTemperature());

        weatherIconView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this).
                load("http://openweathermap.org/img/w/" + weather.getIcon() + ".png").
                into(weatherIconView);

        tempTv.setText(temperature);

    }
}
