package com.example.makcha;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity{
    private double departure_lat, departure_lng;
    private double arrive_lat, arrive_lng;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Places.initialize(getApplicationContext(), getString(R.string.api_key));

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment departureFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.departure);
        AutocompleteSupportFragment arriveFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.arrive);

        // Specify the types of place data to return.
        assert departureFragment != null;
        departureFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        assert arriveFragment != null;
        arriveFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));


        // Set up a PlaceSelectionListener to handle the response.
        departureFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                departure_lat = Objects.requireNonNull(place.getLatLng()).latitude;
                departure_lng = Objects.requireNonNull(place.getLatLng()).longitude;
                Log.i(TAG, "Place: " + place.getName() + "\n" + departure_lat + ", " + departure_lng);
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        arriveFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                arrive_lat = Objects.requireNonNull(place.getLatLng()).latitude;
                arrive_lng = Objects.requireNonNull(place.getLatLng()).longitude;
                Log.i(TAG, "Place: " + place.getName() + "\n" + arrive_lat + ", " + arrive_lng);
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }
    public void clickBtn(View view){
        Intent intent = new Intent(this, CalculationActivity.class);
        intent.putExtra("departure_lat", departure_lat);
        intent.putExtra("departure_lng", departure_lng);
        intent.putExtra("arrive_lat", arrive_lat);
        intent.putExtra("arrive_lng", arrive_lng);
        startActivity(intent);
        finish();
    }

}