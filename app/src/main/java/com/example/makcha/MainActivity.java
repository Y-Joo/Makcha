package com.example.makcha;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity{
    private LatLng departure_latlng;
    private LatLng arrive_latlng;


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
                departure_latlng = place.getLatLng();
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
                arrive_latlng = place.getLatLng();
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
        intent.putExtra("departure_lat", Double.toString(departure_latlng.latitude));
        intent.putExtra("departure_lng", Double.toString(departure_latlng.longitude));
        intent.putExtra("arrive_lat", Double.toString(arrive_latlng.latitude));
        intent.putExtra("arrive_lng", Double.toString(arrive_latlng.longitude));
        startActivity(intent);
        finish();
    }

}