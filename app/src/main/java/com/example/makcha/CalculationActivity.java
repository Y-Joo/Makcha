package com.example.makcha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import static android.content.ContentValues.TAG;

import com.google.android.gms.maps.model.LatLng;

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
        String url_str = "https://maps.googleapis.com/maps/api/directions/json?origin=" + departure_lat + "," + departure_lng + "&destination=" + arrive_lat + "," + arrive_lng + "&mode=transit&key=" + getString(R.string.api_key);
        Log.i(TAG, url_str);
        Request request = new Request.Builder()
                .url(url_str)
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            Log.i(TAG, "response: " + responseBody.string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}