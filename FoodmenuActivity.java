package com.example.jaska.mdc_harjoitustyo;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.Notification;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class FoodmenuActivity extends AppCompatActivity
{

    private static final String TAG_DAYOFWEEK = "DayOfWeek";
    private static final String TAG_DATE = "Date";
    private static final String TAG_NAME = "Name";
    private static final String TAG = "asd"; //logd tag
    private String viikonPaiva;
    private String paivaMaara;

    public TextView weekDay;
    public TextView date;


    // JsonArray ruokalistalle
    public static JSONObject ruokalista = null;

    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodmenu);

        /// Haetaan tämä päivä ja formatoidaan se stringiin
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        Log.d("TAG", "onCreate: " + formattedDate);

        lv = findViewById(R.id.listView);

        weekDay = findViewById(R.id.textView_weekDay);
        date = findViewById(R.id.textView_date);

        // Pusketaan nykypäivä urliin, jotta saadaan aina nykyisen päivän lista haettua
        String url = "https://www.amica.fi/api/restaurant/menu/day?date=" + formattedDate + "&language=fi&restaurantPageId=66287";

        new ParseTask().execute(url);
    }


    class ParseTask extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>>
    {

        public ProgressBar progressBar;


        @Override
        protected void onPreExecute()
        {
            progressBar = findViewById(R.id.progressbar);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params)
        {
            String url = params[0];
            // Instanssi json-parserista
            JSONParser jParser = new JSONParser();
            // Haetaan json urlista
            JSONObject json = jParser.getJSONFromUrl(url);


            // Hashmappi Listview
            ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();


            try {
                // Haetaan ruokalistan array
                ruokalista = json.getJSONObject("LunchMenu");
                viikonPaiva = ruokalista.getString("DayOfWeek");
                paivaMaara = ruokalista.getString("Date");

                runOnUiThread(new Runnable() {
                    public void run() {
                        date.setText(paivaMaara);
                        weekDay.setText(viikonPaiva);
                    }
                });

                Log.d(TAG, "doInBackground: " + paivaMaara + viikonPaiva);

                JSONArray setMenu = ruokalista.getJSONArray("SetMenus");
                for (int i = 0; i < setMenu.length(); i++)
                {
                    // Parsitaan for loopissa oikeat arvot
                    JSONObject settimenu = setMenu.getJSONObject(i);

                    JSONArray ruuat = settimenu.getJSONArray("Meals");
                    JSONObject okok = ruuat.getJSONObject(0);
                    String annos = okok.getString("Name");
                    Log.d(TAG, annos);


                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_DAYOFWEEK, viikonPaiva);
                    map.put(TAG_DATE, paivaMaara);
                    map.put(TAG_NAME, annos);

                    // Lisätään mappiin
                    contactList.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("qwe", e.toString());
            }
            return contactList;
        }

        protected void onPostExecute(ArrayList<HashMap<String, String>> contactList)
        {
            ListAdapter adapter = new SimpleAdapter(FoodmenuActivity.this, contactList, R.layout.list_item,  new String[]{TAG_NAME}, new int[]{
                           R.id.foodName});

            lv.setAdapter(adapter);
            //ProgressBar näkymättömäksi
            ProgressBar bar =  findViewById(R.id.progressbar);
            bar.setVisibility(View.INVISIBLE);
        }
    }
}
