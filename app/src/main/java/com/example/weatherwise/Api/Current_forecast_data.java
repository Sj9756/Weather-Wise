package com.example.weatherwise.Api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Current_forecast_data {
    private final JSONObject current;

    public Current_forecast_data(JSONObject current) {
        this.current = current;
    }

    public String getLastUpdateTime() {
        try {
            String[] timeDate = current.getString("last_updated").split(" ");
            String time = timeDate[1];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalTime localTime = LocalTime.parse(time);
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
                return localTime.format(timeFormatter);
            }
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public String getCurrentDate() {
        try {
            String[] timeDate = current.getString("last_updated").split(" ");
            String date = timeDate[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate localdate = LocalDate.parse(date);
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return localdate.format(timeFormatter);
            }
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public String getCurrentTemp() {
        try {
            double temp = current.getDouble("temp_c");
            return Math.round(temp) + "°";
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public String getWindSpeed() {
        try {
            return current.getString("wind_kph") + "Km/h";
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public String getWindDirection() {
        try {
            return current.getString("wind_dir");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public String getUvLevel() {
        try {
            int point = current.getInt("uv");
            if (point >= 0 && point <= 2) {
                return "Low";
            } else if (point >= 3 && point <= 5) {
                return "Moderate";
            } else if (point >= 6 && point <= 7) {
                return "High";
            } else if (point >= 8 && point <= 10) {
                return "Very High";
            } else {
                return "Extreme";
            }
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }
    public String getUvPrecaution() {
        try {
            int point = current.getInt("uv");
            if (point >= 0 && point <= 2) {
                return "No protection required";
            } else if (point >= 3 && point <= 5) {
                return "Protection required";
            } else if (point >= 6 && point <= 7) {
                return "Protection essential";
            } else if (point >= 8 && point <= 10) {
                return "Extra protection";
            } else {
                return "Stay inside";
            }
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public int getUvPoint() {
        try {
            return current.getInt("uv");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return -1;
    }

    public String getPressure() {
        try {
            double pre=current.getDouble("pressure_mb");
            return Math.round(pre)+"mb";
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public String getRainMeter() {
        try {
            return current.getString("precip_mm") + "mm";
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public String getConditionText() {
        try {
            JSONObject condition = current.getJSONObject("condition");
            return condition.getString("text");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public String getFeelsLikeTemp() {
        try {
            double temp = current.getDouble("feelslike_c");
            return Math.round(temp) + "°";
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public String getHumidity() {
        try {
            return current.getString("humidity") + "%";
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public String getVisibility() {
        try {
            return current.getString("vis_km") + "KM";
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }

    public int getAqi() {
        try {
            JSONObject air_quality = current.getJSONObject("air_quality");
            double co = air_quality.getDouble("co");
            double no2 = air_quality.getDouble("no2");
            double o3 = air_quality.getDouble("o3");
            double so2 = air_quality.getDouble("so2");
            double pm2_5 = air_quality.getDouble("pm2_5");
            double pm10 = air_quality.getDouble("pm10");
            double[] pollutants = {co, no2, o3, so2, pm2_5, pm10};
            double[] cLow = {0, 4.5, 36, 55, 35, 55, 150};
            double[] cHigh = {4.4, 9.4, 54, 134, 54, 154, 500};
            int[] aqiLow = {0, 51, 101, 151, 201, 301, 401};
            int[] aqiHigh = {50, 100, 150, 200, 300, 400, 500};

            double aqi = 0;
            for (int i = 0; i < pollutants.length; i++) {
                double c = pollutants[i];
                double cl = cLow[i];
                double ch = cHigh[i];
                int aqiL = aqiLow[i];
                int aqiH = aqiHigh[i];

                if (c > cl && c <= ch) {
                    aqi = Math.max(aqi, ((aqiH - aqiL) / (ch - cl)) * (c - cl) + aqiL);
                } else if (c > ch) {
                    aqi = Math.max(aqi, aqiH);
                }
            }
            return (int) Math.round(aqi);
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return -1;
    }
    public  String getAQICategory(int aqi) {
        if (aqi >= 0 && aqi <= 50) {
            return aqi +"-Good";
        } else if (aqi >= 51 && aqi <= 100) {
            return aqi +"-Moderate";
        } else if (aqi >= 101 && aqi <= 150) {
            return aqi +"-Unhealthy for Sensitive Groups";
        } else if (aqi >= 151 && aqi <= 200) {
            return aqi +"-Unhealthy";
        } else if (aqi >= 201 && aqi <= 300) {
            return aqi +"-Very Unhealthy";
        } else if (aqi >= 301 && aqi <= 500) {
            return aqi +"-Hazardous";
        } else {
            return "Invalid AQI value";
        }
    }
    public String getLastUpdate(){
        try {
          return current.getString("last_updated");
        } catch (JSONException e) {
            Log.e("error", e.toString());
        }
        return null;
    }
}

