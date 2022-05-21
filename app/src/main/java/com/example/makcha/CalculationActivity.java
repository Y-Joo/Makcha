package com.example.makcha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import static android.content.ContentValues.TAG;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class CalculationActivity extends AppCompatActivity {
    private String departure_lat, departure_lng, arrive_lat, arrive_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);
        Intent intent = getIntent();
        departure_lat = intent.getStringExtra("departure_lat");
        departure_lng = intent.getStringExtra("departure_lng");
        arrive_lat = intent.getStringExtra("arrive_lat");
        arrive_lng = intent.getStringExtra("arrive_lng");

        new Thread(() -> {
            showDirection(); // network 동작, 인터넷에서 xml을 받아오는 코드
        }).start();
    }

    public void showDirection(){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String url_str = "https://maps.googleapis.com/maps/api/directions/json?origin=" + departure_lat + "," + departure_lng + "&destination=" + arrive_lat + "," + arrive_lng + "&mode=transit&alternatives=true&language=ko&key=" + getString(R.string.api_key);
        Log.i(TAG, url_str);
        Request request = new Request.Builder()
                .url(url_str)
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            JSONObject jsonObject = new JSONObject(responseBody.string());
            Log.i(TAG, "response : " + jsonObject.toString(2));
            JSONArray jsonArray = jsonObject.getJSONArray("routes");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = (JSONObject)jsonArray.get(i);
                JSONArray legsArr = obj.getJSONArray("legs");
                for(int j = 0; j < legsArr.length();j++)
                {
                    JSONObject detailObj = legsArr.getJSONObject(j);
                    JSONArray stepsArr = detailObj.getJSONArray("steps");
                    for(int k = 0;k < stepsArr.length(); k++)
                    {
                        JSONObject stepObj = stepsArr.getJSONObject(k);
                        if (stepObj.getString("travel_mode").equals("TRANSIT"))
                            Log.i(TAG, "step : " + stepObj.getString("html_instructions") + ", departure_stop : " + stepObj.getJSONObject("transit_details").getJSONObject("departure_stop").getString("name") + ", departure_name : " + stepObj.getJSONObject("transit_details").getJSONObject("departure_time").getString("text") + ", arrival_stop : " + stepObj.getJSONObject("transit_details").getJSONObject("arrival_stop").getString("name") + ", arrival_time : " + stepObj.getJSONObject("transit_details").getJSONObject("arrival_time").getString("text"));
                        else
                            Log.i(TAG, "step : " + stepObj.getString("html_instructions"));
                    }
                    Log.i(TAG, "=========================================");
                }
            }


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

}