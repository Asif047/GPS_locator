package com.pencilbox.user.getcurrentlocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Location";
    private static final String APPID = "380199723cebdb85ef2e16cc30cee5b6";
    private static final String METIC = "metric";
    private static final String IMPERIAL = "imperial";
    private static final int CNT = 7;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Geocoder geocoder;
    private List<Address> addressList;
//    private Retrofit retrofit;
//    private WeatherApiService weatherApiService;
//    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geocoder = new Geocoder(this);
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        weatherApiService = retrofit.create(WeatherApiService.class);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for(Location location : locationResult.getLocations()){

                    //requestForecastResponse(location.getLatitude(),location.getLongitude());
                    Log.e(TAG, "current Latitude: "+location.getLatitude());
                    Log.e(TAG, "current Longitude: "+location.getLongitude());
                    try {
                        addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        Log.e(TAG, "address line: "+addressList.get(0).getAddressLine(0));
                        Log.e(TAG, "city: "+addressList.get(0).getLocality());
                        Log.e(TAG, "country: "+addressList.get(0).getCountryName());
                        Log.e(TAG, "postal code: "+addressList.get(0).getPostalCode());
                        Log.e(TAG, "sublocality: "+addressList.get(0).getSubLocality());
                        Log.e(TAG, "phone: "+addressList.get(0).getPhone());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };
        getLastLocation();
        createLocationUpadates();


    }

//    private void requestForecastResponse(double latitude, double longitude) {
//
//        String urlString = String.format("forecast/daily?lat=%f&lon=%f&cnt=%d%units=%s&appid=%s",latitude,longitude,CNT,METIC,APPID);
//       // Call<WeatherForecastResponse>weatherForecastResponseCall = weatherApiService.getForecastResponse(urlString);
//        weatherForecastResponseCall.enqueue(new Callback<WeatherForecastResponse>() {
//            @Override
//            public void onResponse(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
//                if(response.code()== 200){
//                    WeatherForecastResponse weatherForecastResponse = response.body();
//                    List<WeatherForecastResponse.ForecastList>forecastLists = weatherForecastResponse.getList();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WeatherForecastResponse> call, Throwable t) {
//
//            }
//        });
//    }

    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void createLocationUpadates() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Toast.makeText(MainActivity.this, "Latitude: " + location.getLatitude() +
                        "Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "latitude"+location.getLatitude());
                Log.e(TAG, "logintude"+location.getLongitude());

            }
        });
    }
}
