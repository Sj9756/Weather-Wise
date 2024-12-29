package com.example.weatherwise;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Place_finder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_finder);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBaIeyVXkpiUTHe9Ss0p1E8MMVwenFER9k");
        }
        fragment();
    }
    private void fragment(){
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_view);
        autocompleteFragment.setPlaceFields(Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.LAT_LNG,
                Place.Field.OPENING_HOURS,
                Place.Field.PHONE_NUMBER,
                Place.Field.PHOTO_METADATAS,
                Place.Field.PLUS_CODE,
                Place.Field.RATING,
                Place.Field.USER_RATINGS_TOTAL,
                Place.Field.WEBSITE_URI
        ));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                    restartApp(place.getLatLng(),place.getAddress());
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.d("place","An error occurred: " + status);
            }
        });


    }
    private void restartApp(LatLng place,String address) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (place!=null){
            intent.putExtra("place",place);
        }
        if (address!=null){
            intent.putExtra("address",address);
        }
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

//    public void geocoder(LatLng place) {
//        Geocoder geocoder = new Geocoder(this);
//        try {
//            List<Address> addresses = geocoder.getFromLocation(place.latitude, place.longitude, 1);
//            if (addresses != null && !addresses.isEmpty()) {
//                Address address = addresses.get(0);
//                StringBuilder addressStringBuilder = new StringBuilder();
//                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
//                    addressStringBuilder.append(address.getAddressLine(i)).append(", ");
//                }
//                String completeAddress = addressStringBuilder.toString().replaceAll(", $", "");
//                Log.d("Geocoder", completeAddress);
//
//                String[] locationDetails = parseLocation(completeAddress);
//                if (locationDetails != null) {
//                    Log.d("Geocoder", "Area: " + locationDetails[0] + ", City: " + locationDetails[1]);
//                } else {
//                    Log.e("Geocoder", "Unable to parse location from address: " + completeAddress);
//                }
//            } else {
//                Log.e("Geocoder", "No address found for the location");
//            }
//        } catch (IOException e) {
//            Log.e("Geocoder", "Error getting address", e);
//        }
//    }
//
//    public String[] parseLocation(String address) {
//        // Define a regex pattern to match the address string
//        // This pattern assumes the format: "someCode, area, city, state zip, country"
//        String regex = "^(.*?), (.*?), (.*?), (.*? \\d+), (.*)$";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(address);
//
//        if (matcher.matches()) {
//            // Extract area and city from the matched groups
//            String area = matcher.group(2);
//            String city = matcher.group(3);
//            return new String[]{area, city};
//        } else {
//            // Handle cases where the pattern does not match
//            // Try a simpler pattern for addresses with fewer components
//            String simpleRegex = "^(.*?), (.*?), (.* \\d+), (.*)$";
//            pattern = Pattern.compile(simpleRegex);
//            matcher = pattern.matcher(address);
//
//            if (matcher.matches()) {
//                // Extract area and city from the matched groups
//                String area = matcher.group(1);
//                String city = matcher.group(2);
//                return new String[]{area, city};
//            }
//        }
//
//        // Return null if no patterns match
//        return null;
//    }
}