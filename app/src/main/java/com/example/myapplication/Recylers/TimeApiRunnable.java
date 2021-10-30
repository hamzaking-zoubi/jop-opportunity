package com.example.myapplication.Recylers;

//import org.json.JSONException;
//import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public abstract class TimeApiRunnable implements Runnable{
    private final static String TIME_API_URL = "https://currentmillis.com/time/minutes-since-unix-epoch.php";
//    private final static String TIME_KEY = "currentFileTime";

    @Override
    public void run() {
        String timeResult;
        long timeInMillisecond;

        try {
            //Create URL
            URL timeUrl = new URL(TIME_API_URL);

            //Connect and get response
            timeResult = makeHttpRequest(timeUrl);

            //Extract data from string
            if (timeResult == null || timeResult.equals(""))
                throw(new IOException());

            timeInMillisecond = Long.parseLong(timeResult) * 60000;
            onCompletionListener(timeInMillisecond);
        } catch (MalformedURLException e) {
            onFailureListener("Error while create url");
        } catch (IOException e) {
            onFailureListener("Error while receiving data");
        }

    }

    private String makeHttpRequest(URL timeUrl) throws IOException {
        String response;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) timeUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() != 200)
                throw(new IOException());

            inputStream = urlConnection.getInputStream();
            response = readFromStream(inputStream);
        } finally {

            if (urlConnection != null)
                urlConnection.disconnect();

            if (inputStream != null)
                inputStream.close();

        }

        return response;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
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

//    private long extractTimeFromJson(String jsonCreation) throws JSONException {
//        JSONObject jsonObject = new JSONObject(jsonCreation);
//        return jsonObject.getLong(TIME_KEY);
//    }

    public abstract void onCompletionListener(long millisecond);

    public abstract void onFailureListener(String errorMessage);

}
