package com.example.jaska.mdc_harjoitustyo;




import org.json.JSONArray;
import org.json.JSONObject;


public class WeatherEngine implements HTTPGetThread.OnWeatherRequestDone
{

    private String temperature;
    private String icon;
    protected WeatherDataAvailableInterface uiCallback = null;
    private final static double KELVIN_CONVERT = 272.15;

    public WeatherEngine(WeatherDataAvailableInterface uiCallback)
    {
        this.uiCallback = uiCallback;
    }


    public interface WeatherDataAvailableInterface
    {
        void weatherDataAvailable();
    }

    public String getTemperature()
    {
        return temperature;
    }

    public String getIcon()
    {
        return this.icon;
    }


    @Override
    public void requestDone(String data)
    {
        try
        {
            JSONObject jObject = new JSONObject(data);
            JSONObject mainObj = jObject.getJSONObject("main");
            JSONArray jArray = jObject.getJSONArray("weather");
            JSONObject iconObject = jArray.getJSONObject(0);

            double temp = (double)mainObj.getDouble("temp");
            double tempC = temp - KELVIN_CONVERT;

            this.temperature = String.format("%.1f", tempC);
            this.icon = iconObject.getString("icon");

            uiCallback.weatherDataAvailable();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void getWeatherData(String city)
    {
        final String BASE_URL =  "http://api.openweathermap.org/data/2.5/weather?q=";
        final String API_KEY = "&APPID=f19445a739d4ac7835137f837e441d4b";
        String url = BASE_URL + city + API_KEY;

        HTTPGetThread client = new HTTPGetThread(url, this);
        client.start();
    }


}
