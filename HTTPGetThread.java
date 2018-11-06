package com.example.jaska.mdc_harjoitustyo;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPGetThread extends Thread
{
private HttpURLConnection connection = null;
    private InputStream input;
    private String url = null;

    public interface OnWeatherRequestDone
    {
        void requestDone(String data);
    }

    OnWeatherRequestDone listener = null;

    public HTTPGetThread(String url, OnWeatherRequestDone listener)
    {
        this.url = url;
        this.listener = listener;
    }

    @Override
    public void run()
    {
        getData();
    }

    public void getData()
    {
        try
        {
            URL mUrl = new URL(url);
            connection = (HttpURLConnection)mUrl.openConnection();
            input = new BufferedInputStream(connection.getInputStream());
            String data = getDataFromStream(input);
            input.close();
            listener.requestDone(data);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }
    }

    public static String getDataFromStream(InputStream input) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader (input));
        StringBuilder output = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;

        while ((line = reader.readLine()) != null)
        {
            output.append(line);
            output.append(newLine);
        }
        return output.toString();

    }
}
