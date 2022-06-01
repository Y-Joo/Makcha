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
import android.view.ViewGroup;
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
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.internal.VisibilityAwareImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Array;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity{
    private MainActivity sup;
    private LatLng departure_latlng;
    private LatLng arrive_latlng;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sup = this;
        Places.initialize(getApplicationContext(), getString(R.string.api_key));

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment departureFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.departure);
        departureFragment.getView().setBackground(getDrawable(com.google.android.libraries.places.R.color.quantum_greywhite1000));
        AutocompleteSupportFragment arriveFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.arrive);
        arriveFragment.getView().setBackground(getDrawable(com.google.android.libraries.places.R.color.quantum_greywhite1000));


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

    class GetApi implements Runnable{
        JSONObject jsonObject;
        Request request;

        public GetApi(Request request) {
            this.request = request;
        }

        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            try {
                Response response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();
                jsonObject = new JSONObject(responseBody.string());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        public JSONObject getJsonObject(){
            return jsonObject;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void clickSearchButton(View view){

        LinearLayout directionCardList = (LinearLayout)findViewById(R.id.directionCardList);

        LinearLayout.LayoutParams firstBusParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        firstBusParams.setMargins(40,5,0,5);
        TextView firstBus = new TextView(this);
        firstBus.setLayoutParams(firstBusParams);
        firstBus.setText("첫차 경로");
        firstBus.setTextSize(15);
        firstBus.setTextColor(getColor(R.color.black));

        directionCardList.addView(firstBus);

        LinearLayout.LayoutParams tableAttributeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tableAttributeParams.setMargins(40,5,40,0);
        LinearLayout tableAttribute = new LinearLayout(this);
        tableAttribute.setLayoutParams(tableAttributeParams);
        tableAttribute.setOrientation(LinearLayout.HORIZONTAL);

        TextView timeLast = new TextView(this);
        timeLast.setTextSize(10);
        timeLast.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        timeLast.setTextColor(getColor(R.color.black));
        timeLast.setText("남은 시간");
        LinearLayout.LayoutParams timeLastParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        timeLastParams.weight = 1;
        timeLast.setLayoutParams(timeLastParams);

        TextView timeDuring = new TextView(this);
        timeDuring.setTextSize(10);
        timeDuring.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        timeDuring.setTextColor(getColor(R.color.black));
        timeDuring.setText("걸리는 시간");
        LinearLayout.LayoutParams timeDuringParams =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        timeDuringParams.weight = 1;
        timeDuring.setLayoutParams(timeDuringParams);

        TextView timeArrive = new TextView(this);
        timeArrive.setTextSize(10);
        timeArrive.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        timeArrive.setTextColor(getColor(R.color.black));
        timeArrive.setText("도착 시간");
        LinearLayout.LayoutParams timeArriveParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        timeArriveParams.weight = 1;
        timeArrive.setPadding(0,0,50,0);
        timeArrive.setLayoutParams(timeArriveParams);

        tableAttribute.addView(timeLast);
        tableAttribute.addView(timeDuring);
        tableAttribute.addView(timeArrive);

        directionCardList.addView(tableAttribute);



        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String url_str = "https://maps.googleapis.com/maps/api/directions/json?origin=" + Double.toString(departure_latlng.latitude) + "," + Double.toString(departure_latlng.longitude) + "&destination=" + Double.toString(arrive_latlng.latitude) + "," + Double.toString(arrive_latlng.longitude) + "&mode=transit&alternatives=true&language=ko&key=" + getString(R.string.api_key);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime localDateTime = LocalDateTime.now();
            int hour = localDateTime.getHour();
            if (hour > 3)
                localDateTime = localDateTime.plusDays(1);
            LocalDateTime newLocalDateTime = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 3, 0);
            long epochSecond = newLocalDateTime.toEpochSecond(ZoneOffset.of("+09:00"));
            url_str += "&arrival_time=" + epochSecond;
        }

        Request request = new Request.Builder()
                .url(url_str)
                .method("GET", null)
                .build();
        try {
            GetApi getApi = new GetApi(request);
            Thread thread = new Thread(getApi);
            thread.start();
            thread.join();
            JSONObject jsonObject = getApi.getJsonObject();
            Log.i(TAG, "response : " + jsonObject.toString(2));
            JSONArray jsonArray = jsonObject.getJSONArray("routes");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = (JSONObject)jsonArray.get(i);
                JSONArray legsArr = obj.getJSONArray("legs");
                for(int j = 0; j < legsArr.length();j++)
                {
                    //cardview
                    //divider
                    View divider = new View(this);
                    LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 4);
                    dividerParams.setMargins(20,20,20,20);
                    divider.setBackgroundColor(getColor(com.google.android.libraries.places.R.color.quantum_grey));
                    divider.setLayoutParams(dividerParams);
                    directionCardList.addView(divider);

                    JSONObject detailObj = legsArr.getJSONObject(j);
                    //arrival time, departure time, duration - value
                    String arrival_time = detailObj.getJSONObject("arrival_time").getString("text");
                    Date date = new Date();
                    long departure_time = 0;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        departure_time = (detailObj.getJSONObject("departure_time").getLong("value") - Instant.now().getEpochSecond() + 60) / 60;
                    }
                    long duration = (detailObj.getJSONObject("duration").getLong("value") + 30) / 60;
                    Log.i(TAG, "time_to_departure_time : " + departure_time + "분, duration : " + duration + ", arrival_time : " + arrival_time);
                    Log.i(TAG, "=================================");
                    //60분 00시00분 00시00분 도착 레이아웃만들기
                    DisplayMetrics dm = getResources().getDisplayMetrics();
                    int size = Math.round(5 * dm.density);
                    LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LinearLayout topBaseLayout = new LinearLayout(this);
                    topBaseLayout.setGravity(Gravity.CENTER);
                    topBaseLayout.setOrientation(LinearLayout.HORIZONTAL);
                    topParams.setMargins(size, size, size, size);
                    topBaseLayout.setLayoutParams(topParams);

                    //int providedDepartureHours = 0; //제공된 출발 시간
                    //int providedDepartureMinutes = 0;
                    //LocalTime providedDepartureTime = LocalTime.newInstance(providedDepartureHours, providedDepartureMinutes);  //도착 시간
                    TextView departureTime = new TextView(getApplicationContext());
                    if (departure_time < 60)
                        departureTime.setText(departure_time + "분");
                    else{
                        long hour = departure_time / 60;
                        long minute = departure_time % 60;
                        departureTime.setText(hour + "시간 " + minute +"분");
                    }                    departureTime.setTextSize(20);
                    departureTime.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                    LinearLayout.LayoutParams textParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    //textParams2.setMargins(100, 0,0, 0);
                    textParams2.weight = 1;
                    departureTime.setLayoutParams(textParams2);

                    //int providedTakingTime = 60; //제공된 걸린시간
                    TextView takingTime = new TextView(getApplicationContext());
                    takingTime.setText(duration + "분");
                    takingTime.setTextSize(20);
                    takingTime.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    textParams.weight = 1;
                    takingTime.setLayoutParams(textParams);

                    //int providedArriveHours = 0; //제공된 도착 시간
                    //int providedArriveMinutes = 0;
                    //LocalTime providedArriveTime = LocalTime.newInstance(providedArriveHours, providedArriveMinutes);  //도착 시간
                    TextView arriveTime = new TextView(getApplicationContext());
                    arriveTime.setText(arrival_time);
                    arriveTime.setTextSize(15);
                    arriveTime.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_END);
                    LinearLayout.LayoutParams textParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    arriveTime.setPadding(0,0,50,0);
                    textParams3.weight = 1;
                    arriveTime.setLayoutParams(textParams3);
                    topBaseLayout.addView(departureTime);
                    topBaseLayout.addView(takingTime);
                    topBaseLayout.addView(arriveTime);
                    topBaseLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(sup, DetailedInform.class);
                            intent.putExtra("route_detail", detailObj.toString());
                            startActivity(intent);
                        }
                    });
                    directionCardList.addView(topBaseLayout);
                    JSONArray stepsArr = detailObj.getJSONArray("steps");
                    long sum_step_duration = 0;
                    ArrayList<Long> step_durations = new ArrayList<>();
                    ArrayList<String> vehicles = new ArrayList<>();

                    for(int k = 0;k < stepsArr.length(); k++)
                    {
                        JSONObject stepObj = stepsArr.getJSONObject(k);
                        long step_duration = (stepObj.getJSONObject("duration").getLong("value") + 30) / 60;
                        String vehicle;
                        if (stepObj.getString("travel_mode").equals("TRANSIT"))
                            vehicle = stepObj.getJSONObject("transit_details").getJSONObject("line").getJSONObject("vehicle").getString("type");
                        else
                            vehicle = "WALKING";
                        step_durations.add(step_duration);
                        vehicles.add(vehicle);
                        sum_step_duration += step_duration;
                        Log.i(TAG, "step_duration : " + step_duration + "분, vehicle : " + vehicle);
                    }
                    FrameLayout routeFrame = new FrameLayout(this);
                    FrameLayout.LayoutParams routeFrameParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    routeFrame.setLayoutParams(routeFrameParams);
                    LinearLayout barLayout = new LinearLayout(this);
                    LinearLayout.LayoutParams barLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    barLayout.setGravity(Gravity.CENTER);
                    barLayout.setOrientation(LinearLayout.HORIZONTAL);
                    barLayoutParams.setMargins(40,0,40,0);
                    barLayout.setLayoutParams(barLayoutParams);
                    LinearLayout barTextLayout = new LinearLayout(this);
                    LinearLayout.LayoutParams barTextLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    barTextLayoutParams.setMargins(40,0,40,0);
                    barTextLayout.setLayoutParams(barTextLayoutParams);
                    for(int k = 0; k < step_durations.size(); k++){
                        Long step_duration = step_durations.get(k);
                        String vehicle = vehicles.get(k);

                        //이미지박스로 경로 나타내기. 빨간색: 버스, 파란색: 지하철, 회색:도보

//        int totalTime = 60; //총소요시간 bar비율에 이용할 예정
//        int time = 10;  // 소요시간
//        int type = 0; // 나중에 데이터 제공받으면 while문을 통해서 반복해서 경로 만듬
                        int type;
                        if (vehicle.equals("WALKING"))
                            type = 0;
                        else if (vehicle.equals("BUS"))
                            type = 1;
                        else
                            type = 2;
                        makeImageBar(this, type, sum_step_duration, step_duration, barLayout);
                        //bar 위에 소요시간 텍스트로 표시
                        int textTime = 10; // 소요시간
                        // 나중에 데이터를 받으면 while문을 통해서 반복적으로 text객체 생성
                        makeTextOnBar(this, step_duration, sum_step_duration, barTextLayout);
                    }
                    routeFrame.addView(barLayout);
                    routeFrame.addView(barTextLayout);
                    routeFrame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(sup, DetailedInform.class);
                            intent.putExtra("route_detail", detailObj.toString());
                            startActivity(intent);
                        }
                    });
                    directionCardList.addView(routeFrame);
                }
            }
        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
        }

        madeFirstBusDirection();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void madeFirstBusDirection(){
        LinearLayout directionCardList = (LinearLayout)findViewById(R.id.directionCardList);

        LinearLayout directionCardList = (LinearLayout)findViewById(R.id.directionCardList);
        directionCardList.removeAllViews();

        LinearLayout.LayoutParams firstBusParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        firstBusParams.setMargins(40,5,0,5);
        TextView firstBus = new TextView(this);
        firstBus.setLayoutParams(firstBusParams);
        firstBus.setText("막차 경로");
        firstBus.setTextSize(15);
        firstBus.setTextColor(getColor(R.color.black));

        directionCardList.addView(firstBus);

        LinearLayout.LayoutParams tableAttributeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tableAttributeParams.setMargins(40,5,40,0);
        LinearLayout tableAttribute = new LinearLayout(this);
        tableAttribute.setLayoutParams(tableAttributeParams);
        tableAttribute.setOrientation(LinearLayout.HORIZONTAL);

        TextView timeLast = new TextView(this);
        timeLast.setTextSize(10);
        timeLast.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        timeLast.setTextColor(getColor(R.color.black));
        timeLast.setText("남은 시간");
        LinearLayout.LayoutParams timeLastParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        timeLastParams.weight = 1;
        timeLast.setLayoutParams(timeLastParams);

        TextView timeDuring = new TextView(this);
        timeDuring.setTextSize(10);
        timeDuring.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        timeDuring.setTextColor(getColor(R.color.black));
        timeDuring.setText("걸리는 시간");
        LinearLayout.LayoutParams timeDuringParams =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        timeDuringParams.weight = 1;
        timeDuring.setLayoutParams(timeDuringParams);

        TextView timeArrive = new TextView(this);
        timeArrive.setTextSize(10);
        timeArrive.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        timeArrive.setTextColor(getColor(R.color.black));
        timeArrive.setText("도착 시간");
        LinearLayout.LayoutParams timeArriveParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        timeArriveParams.weight = 1;
        timeArrive.setPadding(0,0,50,0);
        timeArrive.setLayoutParams(timeArriveParams);

        tableAttribute.addView(timeLast);
        tableAttribute.addView(timeDuring);
        tableAttribute.addView(timeArrive);

        directionCardList.addView(tableAttribute);



        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String url_str = "https://maps.googleapis.com/maps/api/directions/json?origin=" + Double.toString(departure_latlng.latitude) + "," + Double.toString(departure_latlng.longitude) + "&destination=" + Double.toString(arrive_latlng.latitude) + "," + Double.toString(arrive_latlng.longitude) + "&mode=transit&alternatives=true&language=ko&key=" + getString(R.string.api_key);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime localDateTime = LocalDateTime.now();
            int hour = localDateTime.getHour();
            if (hour > 3)
                localDateTime = localDateTime.plusDays(1);
            LocalDateTime newLocalDateTime = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 3, 0);
            long epochSecond = newLocalDateTime.toEpochSecond(ZoneOffset.of("+09:00"));
            url_str += "&arrival_time=" + epochSecond;
        }

        Request request = new Request.Builder()
                .url(url_str)
                .method("GET", null)
                .build();
        try {
            GetApi getApi = new GetApi(request);
            Thread thread = new Thread(getApi);
            thread.start();
            thread.join();
            JSONObject jsonObject = getApi.getJsonObject();
            Log.i(TAG, "response : " + jsonObject.toString(2));
            JSONArray jsonArray = jsonObject.getJSONArray("routes");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = (JSONObject)jsonArray.get(i);
                JSONArray legsArr = obj.getJSONArray("legs");
                for(int j = 0; j < legsArr.length();j++)
                {
                    //cardview
                    //divider
                    View divider = new View(this);
                    LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 4);
                    dividerParams.setMargins(20,20,20,20);
                    divider.setBackgroundColor(getColor(com.google.android.libraries.places.R.color.quantum_grey));
                    divider.setLayoutParams(dividerParams);
                    directionCardList.addView(divider);

                    JSONObject detailObj = legsArr.getJSONObject(j);
                    //arrival time, departure time, duration - value
                    String arrival_time = detailObj.getJSONObject("arrival_time").getString("text");
                    Date date = new Date();
                    long departure_time = 0;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        departure_time = (detailObj.getJSONObject("departure_time").getLong("value") - Instant.now().getEpochSecond() + 60) / 60;
                    }
                    long duration = (detailObj.getJSONObject("duration").getLong("value") + 30) / 60;
                    Log.i(TAG, "time_to_departure_time : " + departure_time + "분, duration : " + duration + ", arrival_time : " + arrival_time);
                    Log.i(TAG, "=================================");
                    //60분 00시00분 00시00분 도착 레이아웃만들기
                    DisplayMetrics dm = getResources().getDisplayMetrics();
                    int size = Math.round(5 * dm.density);
                    LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LinearLayout topBaseLayout = new LinearLayout(this);
                    topBaseLayout.setGravity(Gravity.CENTER);
                    topBaseLayout.setOrientation(LinearLayout.HORIZONTAL);
                    topParams.setMargins(size, size, size, size);
                    topBaseLayout.setLayoutParams(topParams);

                    //int providedDepartureHours = 0; //제공된 출발 시간
                    //int providedDepartureMinutes = 0;
                    //LocalTime providedDepartureTime = LocalTime.newInstance(providedDepartureHours, providedDepartureMinutes);  //도착 시간
                    TextView departureTime = new TextView(getApplicationContext());
                    if (departure_time < 60)
                        departureTime.setText(departure_time + "분");
                    else{
                        long hour = departure_time / 60;
                        long minute = departure_time % 60;
                        departureTime.setText(hour + "시간 " + minute +"분");
                    }                    departureTime.setTextSize(20);
                    departureTime.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                    LinearLayout.LayoutParams textParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    //textParams2.setMargins(100, 0,0, 0);
                    textParams2.weight = 1;
                    departureTime.setLayoutParams(textParams2);

                    //int providedTakingTime = 60; //제공된 걸린시간
                    TextView takingTime = new TextView(getApplicationContext());
                    takingTime.setText(duration + "분");
                    takingTime.setTextSize(20);
                    takingTime.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    textParams.weight = 1;
                    takingTime.setLayoutParams(textParams);

                    //int providedArriveHours = 0; //제공된 도착 시간
                    //int providedArriveMinutes = 0;
                    //LocalTime providedArriveTime = LocalTime.newInstance(providedArriveHours, providedArriveMinutes);  //도착 시간
                    TextView arriveTime = new TextView(getApplicationContext());
                    arriveTime.setText(arrival_time);
                    arriveTime.setTextSize(15);
                    arriveTime.setTextAlignment(TextView.TEXT_ALIGNMENT_TEXT_END);
                    LinearLayout.LayoutParams textParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    arriveTime.setPadding(0,0,50,0);
                    textParams3.weight = 1;
                    arriveTime.setLayoutParams(textParams3);
                    topBaseLayout.addView(departureTime);
                    topBaseLayout.addView(takingTime);
                    topBaseLayout.addView(arriveTime);
                    topBaseLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(sup, DetailedInform.class);
                            intent.putExtra("route_detail", detailObj.toString());
                            startActivity(intent);
                        }
                    });
                    directionCardList.addView(topBaseLayout);
                    JSONArray stepsArr = detailObj.getJSONArray("steps");
                    long sum_step_duration = 0;
                    ArrayList<Long> step_durations = new ArrayList<>();
                    ArrayList<String> vehicles = new ArrayList<>();

                    for(int k = 0;k < stepsArr.length(); k++)
                    {
                        JSONObject stepObj = stepsArr.getJSONObject(k);
                        long step_duration = (stepObj.getJSONObject("duration").getLong("value") + 30) / 60;
                        String vehicle;
                        if (stepObj.getString("travel_mode").equals("TRANSIT"))
                            vehicle = stepObj.getJSONObject("transit_details").getJSONObject("line").getJSONObject("vehicle").getString("type");
                        else
                            vehicle = "WALKING";
                        step_durations.add(step_duration);
                        vehicles.add(vehicle);
                        sum_step_duration += step_duration;
                        Log.i(TAG, "step_duration : " + step_duration + "분, vehicle : " + vehicle);
                    }
                    FrameLayout routeFrame = new FrameLayout(this);
                    FrameLayout.LayoutParams routeFrameParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                    routeFrame.setLayoutParams(routeFrameParams);
                    LinearLayout barLayout = new LinearLayout(this);
                    LinearLayout.LayoutParams barLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    barLayout.setGravity(Gravity.CENTER);
                    barLayout.setOrientation(LinearLayout.HORIZONTAL);
                    barLayoutParams.setMargins(40,0,40,0);
                    barLayout.setLayoutParams(barLayoutParams);
                    LinearLayout barTextLayout = new LinearLayout(this);
                    LinearLayout.LayoutParams barTextLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    barTextLayoutParams.setMargins(40,0,40,0);
                    barTextLayout.setLayoutParams(barTextLayoutParams);
                    for(int k = 0; k < step_durations.size(); k++){
                        Long step_duration = step_durations.get(k);
                        String vehicle = vehicles.get(k);

                        //이미지박스로 경로 나타내기. 빨간색: 버스, 파란색: 지하철, 회색:도보

//        int totalTime = 60; //총소요시간 bar비율에 이용할 예정
//        int time = 10;  // 소요시간
//        int type = 0; // 나중에 데이터 제공받으면 while문을 통해서 반복해서 경로 만듬
                        int type;
                        if (vehicle.equals("WALKING"))
                            type = 0;
                        else if (vehicle.equals("BUS"))
                            type = 1;
                        else
                            type = 2;
                        makeImageBar(this, type, sum_step_duration, step_duration, barLayout);
                        //bar 위에 소요시간 텍스트로 표시
                        int textTime = 10; // 소요시간
                        // 나중에 데이터를 받으면 while문을 통해서 반복적으로 text객체 생성
                        makeTextOnBar(this, step_duration, sum_step_duration, barTextLayout);
                    }
                    routeFrame.addView(barLayout);
                    routeFrame.addView(barTextLayout);
                    routeFrame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(sup, DetailedInform.class);
                            intent.putExtra("route_detail", detailObj.toString());
                            startActivity(intent);
                        }
                    });
                    directionCardList.addView(routeFrame);
                }
            }
        } catch (JSONException | InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void makeTextOnBar (Context parent, Long time, Long totalTime, LinearLayout barTextLayout)
    {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int size = Math.round(4 * dm.density);

        TextView barText = new TextView(parent);
        if (time > 5)
            barText.setText(time + "분");
        barText.setTextSize(size);
        barText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        LinearLayout.LayoutParams barTextLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        barTextLayoutParams.weight = time/(float)totalTime;

        barText.setLayoutParams(barTextLayoutParams);
        barTextLayout.addView(barText);
    }
    public void makeImageBar (Context parent, int type, Long totalTime, Long time, LinearLayout barLayout)
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
    public int findCardIndex(LinearLayout parentLayout, View view) {
        int index = parentLayout.indexOfChild(view);
        index = index / 3;
        return index;
    }
}