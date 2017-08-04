package com.example.android.quakereport;

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
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {
    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static List<Earthquake> fetchEarthquakeData(String stringUrl) {
        Log.i(LOG_TAG,"fetchEarthquakeData() method is called");

        //make thread to sleep for 2s to test loading indicator
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        String jsonResponse = null;
        URL url = createUrl(stringUrl);
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Error with closing input stream: ",e);
        }
        List<Earthquake> earthquakeList = extractFromStream(jsonResponse);
        return earthquakeList;

    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
         url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error with creating URL: ",e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");

            int code = urlConnection.getResponseCode();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                //inputStream作为输入，调用readFromStream方法进一步获取jsonResponse的String
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG,"Error Response Code is: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG,"Error with open URL connection: ",e);
        } finally {
            //释放HttpURLConnection及inputStream资源
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        //把output（StringBuilder）转换为String，进行输出
        return output.toString();
    }

    private static List<Earthquake> extractFromStream(String earthquakeJSON){
        if (TextUtils.isEmpty(earthquakeJSON)){
            return null;
        }
        List<Earthquake> quakeInfoList = new ArrayList<>();
        try {
            JSONObject baseJsonObject = new JSONObject(earthquakeJSON);
            JSONArray featureArray = baseJsonObject.getJSONArray("features");

            for (int i=0; i<featureArray.length();i++){
                //获取Feature序列的的第一个值（对象）
                JSONObject firstFeature = featureArray.getJSONObject(i);
                //获取Feature对象中的properties对象
                JSONObject properties = firstFeature.getJSONObject("properties");

                double mag = properties.getDouble("mag");
                String place = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");

                Earthquake quakeInfo = new Earthquake(mag, place, time, url);
                quakeInfoList.add(quakeInfo);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return quakeInfoList;
    }

}