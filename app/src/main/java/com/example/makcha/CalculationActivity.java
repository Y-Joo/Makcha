package com.example.makcha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import static android.content.ContentValues.TAG;


public class CalculationActivity extends AppCompatActivity {
    private double departure_lat, departure_lng;
    private double arrive_lat, arrive_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);
        Intent intent = getIntent();
        departure_lat = intent.getDoubleExtra("departure_lat", 0);
        departure_lng = intent.getDoubleExtra("departure_lng", 0);
        arrive_lat = intent.getDoubleExtra("arrive_lat", 0);
        arrive_lng = intent.getDoubleExtra("arrive_lat", 0);
        Log.i(TAG, "departure: " + departure_lat + departure_lng + "\narrive :" + arrive_lat + arrive_lng);
    }

}