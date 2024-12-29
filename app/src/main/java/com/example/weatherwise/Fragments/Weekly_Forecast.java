package com.example.weatherwise.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weatherwise.Adapter.RecyclerViewAdapterWeekly;
import com.example.weatherwise.Api.WeatherApi;
import com.example.weatherwise.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONObject;

import java.util.ArrayList;


public class Weekly_Forecast extends Fragment {
    RecyclerView recycler_view_weekly;
    WeatherApi api;
    CircularProgressIndicator circularProgressIndicator_weekly;
    public Weekly_Forecast() {
        // Required empty public constructor
    }
    public Weekly_Forecast( WeatherApi api) {
        this.api=api;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_weekly__forecast, container, false);
         recycler_view_weekly=view.findViewById(R.id.recycler_view_weekly);
        circularProgressIndicator_weekly=view.findViewById(R.id.circularProgressIndicator_weekly);
        if (api!=null){
            api.forecastDayWeekly(new WeatherApi.DataCallback() {
                @Override
                public void onSuccess(ArrayList<JSONObject> dataList) {
                    circularProgressIndicator_weekly.setVisibility(View.GONE);
                    recycler_view_weekly.setVisibility(View.VISIBLE);
                    Log.d("dadadda",String.valueOf(dataList.size()));
                    RecyclerViewAdapterWeekly adapter=new RecyclerViewAdapterWeekly(requireContext(),dataList);
                    recycler_view_weekly.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false));
                    recycler_view_weekly.setAdapter(adapter);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }

        return view;
    }
}