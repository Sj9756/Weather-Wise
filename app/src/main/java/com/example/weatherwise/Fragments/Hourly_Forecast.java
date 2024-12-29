package com.example.weatherwise.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherwise.Adapter.RecyclerViewAdapterHourly;
import com.example.weatherwise.Api.WeatherApi;
import com.example.weatherwise.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONObject;

import java.util.ArrayList;


public class Hourly_Forecast extends Fragment   {
    RecyclerView recycler_view_hourly;
    WeatherApi api;
    public Hourly_Forecast() {
        // Required empty public constructor
    }
    public Hourly_Forecast(WeatherApi api) {
        this.api=api;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_hourly__forecast_, container, false);
        recycler_view_hourly=view.findViewById(R.id.recycler_view_hourly);
        if(api!=null){
            api.forecastDayHourly(new WeatherApi.DataCallback() {
                @Override
                public void onSuccess(ArrayList<JSONObject> dataList) {
                    RecyclerViewAdapterHourly adapterHourly=new RecyclerViewAdapterHourly(requireContext(),dataList);
                    recycler_view_hourly.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));
                    recycler_view_hourly.setAdapter(adapterHourly);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
        return view;
    }


}