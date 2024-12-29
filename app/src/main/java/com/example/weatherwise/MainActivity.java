package com.example.weatherwise;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieCompositionFactory;
import com.example.weatherwise.Adapter.FragmentViewPagerAdapter;
import com.example.weatherwise.Api.Current_forecast_data;
import com.example.weatherwise.Api.Location_data;
import com.example.weatherwise.Api.MapsActivity;
import com.example.weatherwise.Api.WeatherApi;
import com.example.weatherwise.Api.Weekly_forecast_data;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    ViewPager2 viewPager;
    TabLayout tab;
    CircularProgressIndicator circularProgressIndicator;
    LinearLayout main_layout;
    ImageView refresh;
    TextView city_name, current_condition, live_temp;
    TextView index_condition_text,last_update,locale_time;
    AppCompatSeekBar seekBar_air_quality, uv_condition_indicator;
    TextView uv_point, uv_condition_text, uv_precaution;
    TextView sunrise_time, sunset_time;
    TextView wind_speed, wind_direction;
    TextView rain_detail;
    TextView feels_like_temp, location_name, region_name, today_low, today_high, condition_text;
    TextView humidity;
    TextView visibility_text, date_today;
    TextView pressure_text;
    WeatherApi api;
    LatLng orlatlong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            getLocationPermission();
        }
        if (getIntent().hasExtra("place")&&getIntent().hasExtra("address")) {
            LatLng latLn=getIntent().getParcelableExtra("place");
            orlatlong=latLn;
            Log.d("aaaaaa",latLn.toString());
            String address=getIntent().getStringExtra("address");
            assert address != null;
            String []split=address.split(",");
            String placeName=split[0];
            String place_city;
            if (split.length>1){
                 place_city=split[1];
            }
            else {
                 place_city=split[0];
            }
            api = new WeatherApi(getApplicationContext(), latLn);
            setTabLayout();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    city_name.setText(placeName);
                    location_name.setText(placeName);
                    region_name.setText(place_city);
                    setLocationData(api);
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    setCurrentData(api);
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    setAstroData(api);
                }
            }).start();
        } else if (getIntent().hasExtra("place") && !getIntent().hasExtra("address")){
            LatLng latLn=getIntent().getParcelableExtra("place");
            assert latLn != null;
            api=new WeatherApi(getApplicationContext(),latLn);
            setTabLayout();
            geocoder(latLn);
            orlatlong=latLn;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    setCurrentData(api);
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    setAstroData(api);
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    setLocationData(api);
                }
            }).start();

        }
        else {
            getLocation();
        }

        city_name.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Place_finder.class);
            startActivity(intent);
        });
        refresh.setOnClickListener(v -> {
            restartApp(orlatlong);
        });
    }

    private void restartApp(LatLng place) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (place!=null){
            intent.putExtra("place",place);
        }
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void initialize() {
        main_layout = findViewById(R.id.main_layout);
        viewPager = findViewById(R.id.view_pager);
        tab = findViewById(R.id.tab_layout);
        circularProgressIndicator = findViewById(R.id.circularProgressIndicator);

        city_name = findViewById(R.id.city_name);
        current_condition = findViewById(R.id.current_condition);
        live_temp = findViewById(R.id.live_temp);

        seekBar_air_quality = findViewById(R.id.seekBar_air_quality);
        seekBar_air_quality.setEnabled(false);
        seekBar_air_quality.setMax(500);
        index_condition_text = findViewById(R.id.index_condition_text);

        uv_point = findViewById(R.id.uv_point);
        uv_condition_text = findViewById(R.id.uv_condition_text);
        uv_precaution = findViewById(R.id.uv_precaution);
        uv_condition_indicator = findViewById(R.id.uv_condition_indicator);
        uv_condition_indicator.setEnabled(false);
        uv_condition_indicator.setMax(11);

        sunrise_time = findViewById(R.id.sunrise_time);
        sunset_time = findViewById(R.id.sunset_time);

        wind_speed = findViewById(R.id.wind_speed);
        wind_direction = findViewById(R.id.wind_direction);

        rain_detail = findViewById(R.id.rain_detail);

        feels_like_temp = findViewById(R.id.feels_like_temp);
        location_name = findViewById(R.id.location_name);
        region_name = findViewById(R.id.region_name);
        today_low = findViewById(R.id.today_low);
        today_high = findViewById(R.id.today_high);
        condition_text = findViewById(R.id.condition_text);

        humidity = findViewById(R.id.humidity);

        visibility_text = findViewById(R.id.visibility_text);
        date_today = findViewById(R.id.date_today);

        pressure_text = findViewById(R.id.pressure_text);
        locale_time=findViewById(R.id.locale_time);
        last_update=findViewById(R.id.last_update);
        refresh=findViewById(R.id.refresh);
    }

    private void setTabLayout() {
        FragmentStateAdapter pagerAdapter = new FragmentViewPagerAdapter(this, api);
        viewPager.setAdapter(pagerAdapter);
        new TabLayoutMediator(tab, viewPager, (tab, i) -> {
            if (i == 0) {
                tab.setText("Hourly Forecast");
            } else if (i == 1) {
                tab.setText("Weekly Forecast");
            }
        }).attach();
    }

    private void setLocationData(WeatherApi api) {
        api.locationDetails(new WeatherApi.DataCallback() {
            @Override
            public void onSuccess(ArrayList<JSONObject> dataList) {
                Location_data data = new Location_data(dataList.get(0));
               // city_name.setText(data.getLocationName());
                locale_time.setText(data.getLocalTime());
               // location_name.setText(data.getLocationName());
               // region_name.setText(data.getRegionName());
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void setCurrentData(WeatherApi api) {
        api.CurrentData(new WeatherApi.DataCallback() {
            @Override
            public void onSuccess(ArrayList<JSONObject> dataList) {

                Current_forecast_data data = new Current_forecast_data(dataList.get(0));
                live_temp.setText(data.getCurrentTemp());
                current_condition.setText(data.getConditionText());

                last_update.setText(data.getLastUpdateTime());


                seekBar_air_quality.setProgress(data.getAqi());
                index_condition_text.setText(data.getAQICategory(data.getAqi()));

                uv_point.setText(String.valueOf(data.getUvPoint()));
                uv_precaution.setText(data.getUvPrecaution());
                uv_condition_text.setText(data.getUvLevel());
                uv_condition_indicator.setProgress(data.getUvPoint());

                wind_speed.setText(data.getWindSpeed());
                wind_direction.setText(data.getWindDirection());

                condition_text.setText(data.getConditionText());
                rain_detail.setText(data.getRainMeter());

                feels_like_temp.setText(data.getFeelsLikeTemp());

                humidity.setText(data.getHumidity());

                visibility_text.setText(data.getVisibility());
                date_today.setText(data.getCurrentDate());

                pressure_text.setText(data.getPressure());

                circularProgressIndicator.setVisibility(View.INVISIBLE);
                main_layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void setAstroData(WeatherApi api) {
        api.forecastDayWeekly(new WeatherApi.DataCallback() {
            @Override
            public void onSuccess(ArrayList<JSONObject> dataList) {
                JSONObject today = dataList.get(0);
                Weekly_forecast_data data = new Weekly_forecast_data(today);
                sunrise_time.setText(data.getSunRise());
                sunset_time.setText(data.getSunSet());

                today_low.setText(data.getMinTemp());
                today_high.setText(data.getMaxTemp());
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void getLocationPermission() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    android.Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                getLocation();
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                            } else {

                            }
                        }
                );
        locationPermissionRequest.launch(new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    private void getLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                            orlatlong=latLng;
                            api = new WeatherApi(getApplicationContext(),latLng);
                            setTabLayout();
                            geocoder(latLng);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    setLocationData(api);
                                }
                            }).start();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    setCurrentData(api);
                                }
                            }).start();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    setAstroData(api);
                                }
                            }).start();
                        }
                    }
                });
    }
    private void geocoder(LatLng location){
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            assert addresses != null;
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                Log.d("area",address.toString());
                String subLocality = address.getSubLocality();
                String locality = address.getLocality();
                if (subLocality != null) {
                    Log.d("subLocality", subLocality);
                }
                if (locality != null) {
                    Log.d("locality", locality);
                }
                StringBuilder addressStringBuilder = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressStringBuilder.append(address.getAddressLine(i)).append(", ");
                }
                String completeAddress = addressStringBuilder.toString().replaceAll(", $", "");
                String complete_add=extractAddress(completeAddress);
                String spliter[]=complete_add.split(", ");
                String city=null;
                String localty=null;
                if(spliter.length==1){
                    city=spliter[0];
                    localty=spliter[0];
                }
                else if (spliter.length==2){
                  city=spliter[0];
                    localty=spliter[1];
                }
               else if(spliter.length==3){
                    city=spliter[1];
                    localty=spliter[2];
                }
               else if (spliter.length==4){
                    city=spliter[2];
                    localty=spliter[3];
                }
               else if (spliter.length>4){
                    city=spliter[2];
                    localty=spliter[4];
                }
                city_name.setText(city);
                location_name.setText(city);
                region_name.setText(localty);
            } else {
                Log.e("Geocoder", "No address found for the location");
            }
        } catch (IOException e) {
            Log.e("Geocoder", "Error getting address", e);
        }
    }
    private String extractAddress(String addressWithCode) {
        // Define the regular expression pattern
        String pattern = "^[A-Z]+[+-][A-Z0-9]+, ";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Create a Matcher object
        Matcher m = r.matcher(addressWithCode);

        // If a match is found, remove the location code and return the rest of the string
        if (m.find()) {
            return addressWithCode.substring(m.end());
        } else {
            // If no match is found, return the original string
            return addressWithCode;
        }

    }
}