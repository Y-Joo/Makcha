package com.example.makcha;

import static android.content.ContentValues.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class DetailedInform extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_inform);

        LinearLayout detailedPage = (LinearLayout)findViewById(R.id.detailedPage);
        //detailedPage.addView(makeTextOnBar());
        //detailedPage.addView(makeImageBar());
        LinearLayout directionCardInDetailedPage = (LinearLayout)findViewById(R.id.directionCardInDetailedPage);

        /////첫번째 레이아웃 !!!!!!!!!!!!여기에 정보 가져와야함..!!!!
        //cardview
        //divider
        View divider = new View(this);
        LinearLayout.LayoutParams dividerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 4);
        dividerParams.setMargins(20,20,20,20);
        divider.setBackgroundColor(getColor(com.google.android.libraries.places.R.color.quantum_grey));
        divider.setLayoutParams(dividerParams);
        directionCardInDetailedPage.addView(divider);
        Intent intent = getIntent();


        JSONObject detailObj = null;
        try {
            detailObj = new JSONObject(intent.getStringExtra("route_detail"));
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

            //int providedTakingTime = 60; //제공된 걸린시간
            TextView takingTime = new TextView(getApplicationContext());
            takingTime.setText(duration + "분");
            takingTime.setTextSize(20);
            takingTime.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            textParams.weight = 1;
            takingTime.setLayoutParams(textParams);

            //int providedDepartureHours = 0; //제공된 출발 시간
            //int providedDepartureMinutes = 0;
            //LocalTime providedDepartureTime = LocalTime.newInstance(providedDepartureHours, providedDepartureMinutes);  //도착 시간
            TextView departureTime = new TextView(getApplicationContext());
            departureTime.setText(departure_time + "분");
            departureTime.setTextSize(20);
            departureTime.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            LinearLayout.LayoutParams textParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //textParams2.setMargins(100, 0,0, 0);
            textParams2.weight = 1;
            departureTime.setLayoutParams(textParams2);

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
            topBaseLayout.addView(takingTime);
            topBaseLayout.addView(departureTime);
            topBaseLayout.addView(arriveTime);
            directionCardInDetailedPage.addView(topBaseLayout);
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
            directionCardInDetailedPage.addView(routeFrame);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ///////////////////////////////////////////첫번째 레이아웃 추가








        //middle 중간 레이아웃 => 이미지 경로 나타냄
        LinearLayout.LayoutParams middleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        LinearLayout middleBaseLayout = new LinearLayout(this);
        middleBaseLayout.setOrientation(LinearLayout.VERTICAL);
        middleBaseLayout.setLayoutParams(middleParams);

        LinearLayout.LayoutParams coverMiddleBaseLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        coverMiddleBaseLayoutParams.weight = 4;
        ScrollView coverMiddleBaseLayout = new ScrollView(this);
        coverMiddleBaseLayout.setLayoutParams(coverMiddleBaseLayoutParams);

        JSONObject route_detail = null;
        try {
            route_detail = new JSONObject(intent.getStringExtra("route_detail"));
            JSONArray steps_arr = route_detail.getJSONArray("steps");
            for (int i = 0; i < steps_arr.length(); i++){
                JSONObject step_obj = steps_arr.getJSONObject(i);
                String step_type = step_obj.getString("travel_mode");
                if (step_type.equals("WALKING"))
                    makeCard(0, this, step_obj.getString("html_instructions"), "", middleBaseLayout);
                else{
                    String departure_stop = step_obj.getJSONObject("transit_details").getJSONObject("departure_stop").getString("name");
                    String transit_inform = step_obj.getJSONObject("transit_details").getJSONObject("line").getString("short_name");
                    Log.i(TAG, departure_stop + transit_inform);
                    if (step_obj.getJSONObject("transit_details").getJSONObject("line").getJSONObject("vehicle").getString("type").equals("BUS"))
                        makeCard(1, this, departure_stop, transit_inform + "번 버스", middleBaseLayout);
                    else
                        makeCard(2, this, departure_stop, "지하철 " + transit_inform, middleBaseLayout);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //transit_detail.line.short_name - 2호선, 7780 transit_detail.line.vehicle.type == SUBWAY - 지하철 html_instructions - 사당까지 도보
        //transit_detail.departure_stop.name - 출발역
        
//        makeCard(1, this, "쌍용스윗닷홈, 우성. 건영아파트",  "1660번 버스", middleBaseLayout);
//        makeCard(2, this, "경의중앙선 도심역",  "", middleBaseLayout);
//        makeCard(0, this, "도보 이동 00분 (도심역 -> 덕소역)",  "", middleBaseLayout);

        coverMiddleBaseLayout.addView(middleBaseLayout);
        detailedPage.addView(coverMiddleBaseLayout);



        //세번째 정보
        LinearLayout.LayoutParams lastParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        lastParams.weight = 1;
        lastParams.setMargins(40,40,40,40);
        LinearLayout LastBaseLayout = new LinearLayout(this);
        LastBaseLayout.setOrientation(LinearLayout.HORIZONTAL);
        LastBaseLayout.setLayoutParams(lastParams);

        //첫째줄 출발시간
        LinearLayout.LayoutParams firstRowParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        firstRowParams.weight = 1;
        firstRowParams.setMargins(20,20,20,20);
        LinearLayout firstRow = new LinearLayout(this);
        firstRow.setLayoutParams(firstRowParams);
        firstRow.setOrientation(LinearLayout.VERTICAL);
        firstRow.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams firstRowTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView firstRowStartView = new TextView(this);
        firstRowStartView.setLayoutParams(firstRowTextParams);
        firstRowStartView.setText("출발 시간");
        firstRowStartView.setTextColor(getColor(R.color.black));
        firstRowStartView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        TextView firstRowEndView = new TextView(this);
        firstRowEndView.setLayoutParams(firstRowTextParams);
        firstRowEndView.setTextSize(10);
        firstRowEndView.setText("00시간 00분");    //연결해야할 변수
        firstRowEndView.setTextColor(getColor(R.color.black));
        firstRowEndView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        //첫번째줄 2번째 텍스트뷰 첫차출발
        LinearLayout.LayoutParams firstRowSecondParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        firstRowSecondParams.weight = 1;
        firstRowSecondParams.setMargins(20,20,20,20);
        LinearLayout firstRowSecond = new LinearLayout(this);
        firstRowSecond.setLayoutParams(firstRowSecondParams);
        firstRowSecond.setOrientation(LinearLayout.VERTICAL);
        firstRowSecond.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams firstRowSecondTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView firstRowSecondStartView = new TextView(this);
        firstRowSecondStartView.setLayoutParams(firstRowSecondTextParams);
        firstRowSecondStartView.setText("첫 탑승 시간");
        firstRowSecondStartView.setTextColor(getColor(R.color.black));
        firstRowSecondStartView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        TextView firstRowSecondEndView = new TextView(this);
        firstRowSecondEndView.setLayoutParams(firstRowSecondTextParams);
        firstRowSecondEndView.setTextSize(10);
        firstRowSecondEndView.setText("00시간 00분");    //연결해야할 변수
        firstRowSecondEndView.setTextColor(getColor(R.color.black));
        firstRowSecondEndView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        firstRow.addView(firstRowStartView);
        firstRow.addView(firstRowEndView);
        firstRowSecond.addView(firstRowSecondStartView);
        firstRowSecond.addView(firstRowSecondEndView);
        LastBaseLayout.addView(firstRow);
        LastBaseLayout.addView(firstRowSecond);
        detailedPage.addView(LastBaseLayout);

        //둘째줄
        LinearLayout.LayoutParams secondRowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        secondRowParams.weight = 1;
        secondRowParams.setMargins(40,40,40,40);
        LinearLayout secondRow = new LinearLayout(this);
        secondRow.setLayoutParams(secondRowParams);
        secondRow.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams secondRowfirstParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        secondRowfirstParams.weight = 1;
        secondRowfirstParams.setMargins(20,20,20,20);
        LinearLayout secondRowfirst = new LinearLayout(this);
        secondRowfirst.setOrientation(LinearLayout.VERTICAL);
        secondRowfirst.setGravity(Gravity.CENTER_VERTICAL);
        secondRowfirst.setLayoutParams(secondRowfirstParams);

        LinearLayout.LayoutParams alarmTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView alarmText = new TextView(this);
        alarmText.setLayoutParams(alarmTextParams);
        alarmText.setText("알림 기능");
        alarmText.setTextColor(getColor(R.color.black));
        alarmText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        secondRowfirst.addView(alarmText);
        secondRow.addView(secondRowfirst);
        detailedPage.addView(secondRow);

    }
    //type 0: 도보 , 1: 버스 , 2: 지하철
    public void makeCard(int type, Context parent, String location, String transitInform, LinearLayout middleBaseLayout)
    {
        //카드 생성
        LinearLayout.LayoutParams directionCardParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        directionCardParams.setMargins(40,20,40,0);
        LinearLayout directionCardLayout = new LinearLayout(this);
        directionCardLayout.setOrientation(LinearLayout.HORIZONTAL);
        directionCardLayout.setLayoutParams(directionCardParams);
        //카드안 사진
        LinearLayout.LayoutParams directionImageParams = new LinearLayout.LayoutParams(0,180);
        directionImageParams.weight = 1;
        ImageView directionImage = new ImageView(this);
        if (type == 0) {
            directionImage.setBackgroundResource(R.drawable.pave_removebg);
        }
        else if (type == 1) {
            directionImage.setBackgroundResource(R.drawable.bus_image_removebg);
        }
        else if (type == 2) {
            directionImage.setBackgroundResource(R.drawable.subway_image);
        }
        directionImage.setLayoutParams(directionImageParams);
        directionCardLayout.addView(directionImage);
        //카드안 텍스트정보
        LinearLayout.LayoutParams textInformParams = new LinearLayout.LayoutParams(0, 180);
        textInformParams.weight = 4;
        LinearLayout textInform = new LinearLayout(this);
        textInform.setOrientation(LinearLayout.VERTICAL);
        textInform.setPadding(20,20,20,20);
        textInform.setLayoutParams(textInformParams);

        LinearLayout.LayoutParams locationTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 90);
        locationTextParams.weight = 5;
        if (type == 1 || type == 2) {
            TextView locationText = new TextView(this);
            locationText.setText(transitInform);
            locationText.setGravity(Gravity.CENTER_VERTICAL);
            if (type == 2)
                locationText.setTextColor(Color.rgb(0, 0, 150));
            else
                locationText.setTextColor(Color.rgb(204,0,0));
            locationText.setLayoutParams(locationTextParams);
            textInform.addView(locationText);
        }

        TextView locationText2 = new TextView(this);
        locationText2.setText(location);
        locationText2.setGravity(Gravity.TOP);
        if (type == 0) {
            locationText2.setTextColor(Color.rgb(150,150,150));
        }
        else if (type == 1) {
            locationText2.setTextColor(Color.rgb(150,150,150));
        }
        else if (type == 2) {
            locationText2.setTextColor(Color.rgb(0,150,0));
        }
        locationText2.setLayoutParams(locationTextParams);
        textInform.addView(locationText2);

        directionCardLayout.addView(textInform);

        middleBaseLayout.addView(directionCardLayout);
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
}