package com.example.makcha;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailedInform extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_inform);

        LinearLayout detailedPage = (LinearLayout)findViewById(R.id.detailedPage);

        //middle 중간 레이아웃 => 이미지 경로 나타냄
        LinearLayout.LayoutParams middleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
        middleParams.weight = 4;
        LinearLayout middleBaseLayout = new LinearLayout(this);
        middleBaseLayout.setOrientation(LinearLayout.VERTICAL);
        middleBaseLayout.setLayoutParams(middleParams);

        Intent intent = getIntent();
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
                    if (step_type.equals("BUS"))
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
        detailedPage.addView(middleBaseLayout);

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
        LinearLayout.LayoutParams directionImageParams = new LinearLayout.LayoutParams(0,120);
        directionImageParams.weight = 1;
        ImageView directionImage = new ImageView(this);
        if (type == 0) {
            directionImage.setBackgroundResource(R.drawable.bus_image_removebg);
        }
        else if (type == 1) {
            directionImage.setBackgroundResource(R.drawable.subway_image);
        }
        else if (type == 2) {
            directionImage.setBackgroundResource(R.drawable.pave_removebg);
        }
        directionImage.setLayoutParams(directionImageParams);
        directionCardLayout.addView(directionImage);
        //카드안 텍스트정보
        LinearLayout.LayoutParams textInformParams = new LinearLayout.LayoutParams(0, 120);
        textInformParams.weight = 4;
        LinearLayout textInform = new LinearLayout(this);
        textInform.setOrientation(LinearLayout.VERTICAL);
        textInform.setPadding(20,20,20,20);
        textInform.setLayoutParams(textInformParams);

        LinearLayout.LayoutParams locationTextParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60);
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
}