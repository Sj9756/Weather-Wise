package com.example.weatherwise.Api;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeatherApi {
    private final Context context;
    String url;
    String key="b553f9c9a1d84027b9785745241606";
    public WeatherApi(@NonNull Context context, LatLng latLng) {
        this.context = context;
        this.url="https://api.weatherapi.com/v1/forecast.json?key=b553f9c9a1d84027b9785745241606&q="+latLng.latitude+","+latLng.longitude+"&days=10&aqi=yes&alerts=no";
    }
    public void forecastDayWeekly(final DataCallback callback) {
        final ArrayList<JSONObject> dataList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject forecast = jsonObject.getJSONObject("forecast");
                    JSONArray forecastday = forecast.getJSONArray("forecastday");
                    for (int i = 0; i < forecastday.length(); i++) {
                        dataList.add(forecastday.getJSONObject(i));
                    }
                    callback.onSuccess(dataList);
                } catch (JSONException e) {
                    callback.onError(e);
                }

            }
        }, volleyError -> {
        });
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void forecastDayHourly(final DataCallback callback) {
        final ArrayList<JSONObject> dataList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject forecast = jsonObject.getJSONObject("forecast");
                    JSONArray forecastday = forecast.getJSONArray("forecastday");
                    JSONObject today = forecastday.getJSONObject(0);

                    JSONArray hours = today.getJSONArray("hour");
                    JSONObject location = jsonObject.getJSONObject("location");

                    String  live_date_time = location.getString("localtime");
                    for (int i = 0; i < hours.length(); i++) {
                        JSONObject hour = hours.getJSONObject(i);
                        String datetime = hour.getString("time");
                        Date forecastDate = dateFormater(datetime);
                        Date now = dateFormater(live_date_time);
                        if (forecastDate.compareTo(now) >= 0) {
                            dataList.add(hour);
                        }
                    }
                    JSONObject tomorrow = forecastday.getJSONObject(1);
                    JSONArray tomorrow_hours = tomorrow.getJSONArray("hour");
                    for (int i = 0; i < tomorrow_hours.length(); i++) {
                        dataList.add(tomorrow_hours.getJSONObject(i));
                    }
                    callback.onSuccess(dataList);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
        }, volleyError -> {
        });
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void CurrentData(final DataCallback callback) {
        final ArrayList<JSONObject> dataList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject current = jsonObject.getJSONObject("current");
                    dataList.add(current);
                    callback.onSuccess(dataList);
                } catch (JSONException e) {
                    callback.onError(e);
                }

            }
        }, volleyError -> {

        });
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public void locationDetails(final DataCallback callback) {
        final ArrayList<JSONObject> dataList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONObject location = jsonObject.getJSONObject("location");
                    dataList.add(location);
                    callback.onSuccess(dataList);
                } catch (JSONException e) {
                    callback.onError(e);
                }
            }
        }, volleyError -> {
        });
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public interface DataCallback {
        void onSuccess(ArrayList<JSONObject> dataList);

        void onError(Exception e);
    }

    private Date dateFormater(String dateTime) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            return dateFormat.parse(dateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
