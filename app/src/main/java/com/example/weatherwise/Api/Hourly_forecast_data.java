package com.example.weatherwise.Api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Hourly_forecast_data {
    private final JSONObject hour;

    public Hourly_forecast_data(JSONObject hour) {
        this.hour = hour;
    }
    public String getTime(){
        try {
            String date=hour.getString("time");
            String[] date_time=date.split(" ");
            String time=date_time[1];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalTime localTime = LocalTime.parse(time);
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh a");
                return localTime.format(timeFormatter).toUpperCase();
            }
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }
    public String getTemp(){
        try {
            double temp_c=hour.getDouble("temp_c");
            return  Math.round(temp_c)+"°";
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }
    public String getConditionText() {
        try {
            JSONObject condition=hour.getJSONObject("condition");
            return condition.getString("text");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }
    public int getConditionCode() {
        try {
            JSONObject condition=hour.getJSONObject("condition");
            return condition.getInt("code");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return -1;
    }
    public String getConditionUrl() {
        try {
            JSONObject condition=hour.getJSONObject("condition");
            return "https:"+condition.getString("icon");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public boolean getRainOrNot() {
        try {
            int bool = hour.getInt("will_it_rain");
            return 1==bool;
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return false;
    }
    public String getRainChance() {
        try {
            return hour.getString("chance_of_rain")+"%";
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }
}
