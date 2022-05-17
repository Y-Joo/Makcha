package com.example.makcha;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements PlaceSelectionListener{

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
        departureFragment.setOnPlaceSelectedListener(this);
        arriveFragment.setOnPlaceSelectedListener(this);
    }
    @Override
    public void onPlaceSelected(@NonNull Place place) {
        // TODO: Get info about the selected place.
        Log.i(TAG, "Place: " + place.getName() + "\n" + place.getId() + "\n" + place.getLatLng());
    }


    @Override
    public void onError(@NonNull Status status) {
        // TODO: Handle the error.
        Log.i(TAG, "An error occurred: " + status);
    }

}