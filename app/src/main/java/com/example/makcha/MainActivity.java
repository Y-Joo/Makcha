package com.example.makcha;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.LocalTime;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.internal.VisibilityAwareImageButton;

import org.w3c.dom.Text;

import java.sql.Time;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void clickSearchButton(View view){
        LinearLayout directionCardList = (LinearLayout)findViewById(R.id.directionCardList);

        //divider
        View divider = new View(this);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 4);
        dividerParams.setMargins(20,20,20,20);
        divider.setBackgroundColor(getColor(com.google.android.libraries.places.R.color.quantum_grey));
        divider.setLayoutParams(dividerParams);
        directionCardList.addView(divider);

        //60분 00시00분 00시00분 도착 레이아웃만들기
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int size = Math.round(5 * dm.density);
        LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout topBaseLayout = new LinearLayout(this);
        topBaseLayout.setGravity(Gravity.CENTER);
        topBaseLayout.setOrientation(LinearLayout.HORIZONTAL);
        topParams.setMargins(size, size, size, size);
        topBaseLayout.setLayoutParams(topParams);

        int providedTakingTime = 60; //제공된 걸린시간
        TextView takingTime = new TextView(getApplicationContext());
        takingTime.setText(Integer.toString(providedTakingTime) + "분");
        takingTime.setTextSize(20);
        takingTime.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.weight = 1;
        takingTime.setLayoutParams(textParams);

        int providedDepartureHours = 0; //제공된 출발 시간
        int providedDepartureMinutes = 0;
        LocalTime providedDepartureTime = LocalTime.newInstance(providedDepartureHours, providedDepartureMinutes);  //도착 시간
        TextView departureTime = new TextView(getApplicationContext());
        departureTime.setText(providedDepartureTime.getMinutes() + "시" + providedDepartureTime.getMinutes() + "분");
        departureTime.setTextSize(20);
        departureTime.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        LinearLayout.LayoutParams textParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //textParams2.setMargins(100, 0,0, 0);
        textParams2.weight = 1;
        departureTime.setLayoutParams(textParams2);

        int providedArriveHours = 0; //제공된 도착 시간
        int providedArriveMinutes = 0;
        LocalTime providedArriveTime = LocalTime.newInstance(providedArriveHours, providedArriveMinutes);  //도착 시간
        TextView arriveTime = new TextView(getApplicationContext());
        arriveTime.setText(providedArriveTime.getMinutes() + "시" + providedArriveTime.getMinutes() + "분 도착");
        arriveTime.setTextSize(15);
        arriveTime.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_END);
        LinearLayout.LayoutParams textParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        arriveTime.setPadding(0,0,50,0);
        textParams3.weight = 1;
        arriveTime.setLayoutParams(textParams3);

        topBaseLayout.addView(takingTime);
        topBaseLayout.addView(departureTime);
        topBaseLayout.addView(arriveTime);
        directionCardList.addView(topBaseLayout);

        //이미지박스로 경로 나타내기. 빨간색: 버스, 파란색: 지하철, 회색:도보
        FrameLayout routeFrame = new FrameLayout(this);
        FrameLayout.LayoutParams routeFrameParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        routeFrame.setLayoutParams(routeFrameParams);

        LinearLayout barLayout = new LinearLayout(this);
        LinearLayout.LayoutParams barLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        barLayout.setGravity(Gravity.CENTER);
        barLayout.setOrientation(LinearLayout.HORIZONTAL);
        barLayoutParams.setMargins(40,0,40,0);
        barLayout.setLayoutParams(barLayoutParams);

        int totalTime = 60; //총소요시간 bar비율에 이용할 예정
        int time = 10;  // 소요시간
        int type = 0; // 나중에 데이터 제공받으면 while문을 통해서 반복해서 경로 만듬
        makeImageBar(this, type, totalTime, time, barLayout);
        makeImageBar(this, 2, totalTime, 40, barLayout);
        makeImageBar(this, 1, totalTime, 10, barLayout);


        routeFrame.addView(barLayout);

        //bar 위에 소요시간 텍스트로 표시
        LinearLayout barTextLayout = new LinearLayout(this);
        LinearLayout.LayoutParams barTextLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        barTextLayoutParams.setMargins(40,0,40,0);
        barTextLayout.setLayoutParams(barTextLayoutParams);
        int textTime = 10; // 소요시간
        // 나중에 데이터를 받으면 while문을 통해서 반복적으로 text객체 생성
        makeTextOnBar(this, textTime, totalTime, barTextLayout);
        makeTextOnBar(this, 40, totalTime, barTextLayout);
        makeTextOnBar(this, 10, totalTime, barTextLayout);
        routeFrame.addView(barTextLayout);

        directionCardList.addView(routeFrame);


    }
    public void makeTextOnBar (Context parent, int time, int totalTime, LinearLayout barTextLayout)
    {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int size = Math.round(4 * dm.density);

        TextView barText = new TextView(parent);
        barText.setText(Integer.toString(time) + "분");
        barText.setTextSize(size);
        barText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        LinearLayout.LayoutParams barTextLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        barTextLayoutParams.weight = time/(float)totalTime;

        barText.setLayoutParams(barTextLayoutParams);
        barTextLayout.addView(barText);
    }
    public void makeImageBar (Context parent, int type, int totalTime, int time, LinearLayout barLayout)
    {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int size = Math.round(5 * dm.density);
        LinearLayout.LayoutParams barImageLayoutParams = new LinearLayout.LayoutParams(0, 40);
        barImageLayoutParams.weight = (float) time/totalTime;
        ImageView moveType = new ImageView(parent);
        //도보
        if (type == 0){
            moveType.setBackgroundResource(R.drawable.greybar);
            moveType.setLayoutParams(barImageLayoutParams);
        }
        //버스
        else if (type == 1){
            moveType.setBackgroundResource(R.drawable.redbar);
            moveType.setLayoutParams(barImageLayoutParams);
            LinearLayout.LayoutParams barImageLayoutOvalParams = new LinearLayout.LayoutParams(60, 40);
            ImageView redOval = new ImageView(parent);
            redOval.setBackgroundResource(R.drawable.redoval);
            barImageLayoutOvalParams.setMargins(-30, 0, -30, 0);
            redOval.setLayoutParams(barImageLayoutOvalParams);
            barLayout.addView(redOval);
        }
        //지하철
        else if (type == 2) {
            moveType.setBackgroundResource(R.drawable.bluebar);
            moveType.setLayoutParams(barImageLayoutParams);
            LinearLayout.LayoutParams barImageLayoutOvalParams = new LinearLayout.LayoutParams(60, 40);
            ImageView blueOval = new ImageView(parent);
            blueOval.setBackgroundResource(R.drawable.blueoval);
            barImageLayoutOvalParams.setMargins(-30, 0, -30, 0);
            blueOval.setLayoutParams(barImageLayoutOvalParams);
            barLayout.addView(blueOval);
        }
        barLayout.addView(moveType);
    }

}