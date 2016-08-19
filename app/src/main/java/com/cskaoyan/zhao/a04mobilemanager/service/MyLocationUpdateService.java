package com.cskaoyan.zhao.a04mobilemanager.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.cskaoyan.zhao.a04mobilemanager.application.MyApplication;

public class MyLocationUpdateService extends Service {
    public MyLocationUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        LocationManager  locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates("gps",1000,0,new MyLocationListener());
        super.onCreate();
    }

    class MyLocationListener implements LocationListener{


        @Override
        public void onLocationChanged(Location location) {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            MyApplication.saveStringToSp("latitude",latitude+"");
            MyApplication.saveStringToSp("longitude",longitude+"");

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
