package com.example.makcha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import static android.content.ContentValues.TAG;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CalculationActivity extends AppCompatActivity {
    private String departure_id, arrive_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);
        Intent intent = getIntent();
        departure_id = intent.getStringExtra("departure_id");
        arrive_id = intent.getStringExtra("arrive_id");

        Log.i(TAG, "departure: " + departure_id + "\narrive :" + arrive_id);
        new Thread(() -> {
            showDirection(); // network 동작, 인터넷에서 xml을 받아오는 코드
        }).start();
    }

    public void showDirection(){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String url_str = "https://maps.googleapis.com/maps/api/directions/xml?origin=place_id%" + departure_id + "&destination=place_id%" + arrive_id + "&mode=transit&key=" + getString(R.string.api_key);
        Log.i(TAG, url_str);
        Request request = new Request.Builder()
                .url(url_str)
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.i(TAG, "response: " + response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}