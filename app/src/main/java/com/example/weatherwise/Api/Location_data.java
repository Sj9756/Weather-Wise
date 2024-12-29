package com.example.weatherwise.Api;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class Location_data {
    private final JSONObject location;

    public Location_data(JSONObject location) {
        this.location = location;
    }

    public String getLocationName() {
        try {
            return location.getString("name");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }
    public String getRegionName() {
        try {
            return location.getString("region");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }
    public String getCountryName() {
        try {
            return location.getString("country");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }
    public LatLng getLatLng() {
        try {
            return new LatLng(location.getDouble("lat"),location.getDouble("lon"));
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }
    public String getLocalTime(){
        try {
           return location.getString("localtime");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }


}
