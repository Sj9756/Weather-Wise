package com.example.weatherwise.Api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Weekly_forecast_data {
    private final JSONObject day;

    public Weekly_forecast_data(JSONObject day) {
        this.day = day;
    }

    public String getDate() {
        try {
            return day.getString("date");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public String getDayName() {
        try {
            String date = day.getString("date");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate formatdate = LocalDate.parse(date, formatter);
                DayOfWeek dayOfWeek = formatdate.getDayOfWeek();
                String dayName = dayOfWeek.toString();
                return dayName.substring(0, 3);
            }
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public String getMaxTemp() {
        try {
            JSONObject daywise = day.getJSONObject("day");
            double temp = daywise.getDouble("maxtemp_c");
            return Math.round(temp) + "°";
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public String getMinTemp() {
        try {
            JSONObject daywise = day.getJSONObject("day");
            double temp = daywise.getDouble("mintemp_c");
            return Math.round(temp) + "°";
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public String getConditionText() {
        try {
            JSONObject daywise = day.getJSONObject("day");
            JSONObject condition = daywise.getJSONObject("condition");
            return condition.getString("text");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public int getConditionCode() {
        try {
            JSONObject daywise = day.getJSONObject("day");
            JSONObject condition = daywise.getJSONObject("condition");
            return condition.getInt("code");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return -1;
    }

    public int getUV() {
        try {
            JSONObject daywise = day.getJSONObject("day");
            return daywise.getInt("uv");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return -1;
    }

    public boolean getRainOrNot() {
        try {
            JSONObject daywise = day.getJSONObject("day");
            int bool = daywise.getInt("daily_will_it_rain");
            return bool == 1;
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return false;
    }

    public boolean getSnowOrNot() {
        try {
            JSONObject daywise = day.getJSONObject("day");
            int bool = daywise.getInt("daily_will_it_snow");
            return bool == 1;
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return false;
    }

    public String getRainChance() {
        try {
            JSONObject daywise = day.getJSONObject("day");
            return daywise.getString("daily_chance_of_rain") + "%";
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public String getSnowChance() {
        try {
            JSONObject daywise = day.getJSONObject("day");
            return daywise.getString("daily_chance_of_snow") + "%";
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public String getSunRise() {
        try {
            JSONObject astro = day.getJSONObject("astro");
            return astro.getString("sunrise");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }
    public String getSunSet() {
        try {
            JSONObject astro = day.getJSONObject("astro");
            return astro.getString("sunset");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

}
