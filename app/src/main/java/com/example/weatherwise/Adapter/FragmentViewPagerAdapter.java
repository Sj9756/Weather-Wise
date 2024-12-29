package com.example.weatherwise.Adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.weatherwise.Api.WeatherApi;
import com.example.weatherwise.Fragments.Hourly_Forecast;
import com.example.weatherwise.Fragments.Weekly_Forecast;

public class FragmentViewPagerAdapter extends FragmentStateAdapter {
    WeatherApi api;
    public FragmentViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, WeatherApi api) {
        super(fragmentActivity);
        this.api=api;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0){
            return new Hourly_Forecast(api);
        }
        return new Weekly_Forecast(api);
    }
    @Override
    public int getItemCount() {
        return 2;
    }


}
