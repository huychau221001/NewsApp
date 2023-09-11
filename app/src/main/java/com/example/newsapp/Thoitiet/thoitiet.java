package com.example.newsapp.Thoitiet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.newsapp.R;
import com.example.newsapp.TrangChu.trangchu;
import com.example.newsapp.TruyenDuLieu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class thoitiet extends AppCompatActivity {
    RecyclerView rcvThoitiet2;
    Thoitiet2_Adapter thoitiet2_adapter;
    ImageView danhsach, icon, quaylai;
    TextView tentp, nhietdo, tinhtrang;
    List<Thoitiet2> thoitiet2s = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thoitiet);

        khaibao();

        tentp.setText(TruyenDuLieu.Truyen_TenTP);

        thoitiet2_adapter = new Thoitiet2_Adapter(this);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        rcvThoitiet2.setLayoutManager(linearLayoutManager2);
        thoitiet2_adapter.setMthoitiet2(thoitiet2s);
        rcvThoitiet2.setAdapter(thoitiet2_adapter);

        CallThoitiet();
        CallThoitiet7days();

        danhsach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thoitiet.this, quanlydiaphuong.class);
                startActivity(intent);
            }
        });

        quaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thoitiet.this, trangchu.class);
                startActivity(intent);
            }
        });

    }

    private void khaibao() {
        tentp = findViewById(R.id.txt_tentp_thoitiet);
        danhsach = findViewById(R.id.img_danhsach_thoitiet);
        rcvThoitiet2 = findViewById(R.id.recycler_doc_thoitiet);
        nhietdo = findViewById(R.id.txt_nhietdo);
        tinhtrang = findViewById(R.id.txt_tinhtrang);
        icon = findViewById(R.id.img_thoitiet);
        quaylai = findViewById(R.id.img_quaylai_thoitiet);
    }

    private void CallThoitiet() {
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+TruyenDuLieu.Truyen_TP+"&appid=830d983248574a22491e0e61de20ba7d";
        RequestQueue requestQueue = Volley.newRequestQueue(thoitiet.this);
        DecimalFormat df = new DecimalFormat("#.#");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                    String status = jsonObjectWeather.getString("main");
                    String icon1 = jsonObjectWeather.getString("icon");

                    Glide.with(icon).load("http://openweathermap.org/img/wn/"+icon1+"@4x.png").into(icon);
                    tinhtrang.setText(status);

                    JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    nhietdo.setText(df.format(temp) + "ºC");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error at sign in : ",error.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    }

    private void CallThoitiet7days() {
        String url = "http://api.openweathermap.org/data/2.5/forecast?q="+TruyenDuLieu.Truyen_TP+"&appid=830d983248574a22491e0e61de20ba7d";
        RequestQueue requestQueue = Volley.newRequestQueue(thoitiet.this);
        DecimalFormat df = new DecimalFormat("#.#");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                    for (int i=0;i<jsonArrayList.length();i++) {
                        JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                        String ngay = jsonObjectList.getString("dt");

                        long l = Long.valueOf(ngay);
                        Date date = new Date(l*1000L);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy HH:mm");
                        String Day = simpleDateFormat.format(date);

                        JSONObject jsonObjectMain = jsonObjectList.getJSONObject("main");
                        double max = jsonObjectMain.getDouble("temp_max") - 273.15;
                        double min = jsonObjectMain.getDouble("temp_min") - 273.15;
                        String NhietdoMax = df.format(max);
                        String NhietdoMin = df.format(min) + "ºC";
                        String Nhietdo = NhietdoMax +"/"+NhietdoMin;

                        JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                        String status = jsonObjectWeather.getString("main");
                        String icon = jsonObjectWeather.getString("icon");

                        thoitiet2s.add(new Thoitiet2(Day,Nhietdo,icon,status));
                    }
                    thoitiet2_adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error at sign in : ",error.getMessage());
            }
        });
        requestQueue.add(stringRequest);
    }
}