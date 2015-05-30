package com.relianceit.relianceorder.services;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

/**
 * Created by Suresh on 5/29/15.
 */
public class ROSLocationService {

    public static final String TAG = ROSLocationService.class.getSimpleName();

    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private String locationProvider = null;
    private int checkCount = 0;

    public static interface ROSLocationServiceListener {
        public abstract void onLocationFound(Location location);
        public abstract void onLocationFailed();
    }

    public boolean isLocationEnabled(Context context) {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        List<String> enProviders = locationManager.getProviders(true);

        if (enProviders != null) {
            if (enProviders.contains(LocationManager.NETWORK_PROVIDER) || enProviders.contains(LocationManager.GPS_PROVIDER)) {
                return true;
            }
        }

        return false;
    }

    public void getCurrentLocation(Context context, final ROSLocationServiceListener listener) {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        List<String> enProviders = locationManager.getProviders(true);

        if (enProviders != null && (enProviders.contains(LocationManager.NETWORK_PROVIDER) || enProviders.contains(LocationManager.GPS_PROVIDER))) {

        }else {
            listener.onLocationFailed();
            return;
        }

        locationProvider = null;

        if (enProviders.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }else if (enProviders.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
        }else {
            locationProvider = enProviders.get(0);
        }

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                locationFound(location, listener);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i(TAG, "Location onStatusChanged");
            }

            public void onProviderEnabled(String provider) {
                locationProvider = provider;
                Log.i(TAG, "Location onProviderEnabled: " + provider);
            }

            public void onProviderDisabled(String provider) {
                Log.i(TAG, "Location onProviderDisabled: " + provider);
                locationFailed(listener);
            }
        };

        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);

    }

    private void locationFound(Location location, final ROSLocationServiceListener listener) {
        if (checkCount > 3 || location != null) {
            checkCount = 0;
            locationManager.removeUpdates(locationListener);
            listener.onLocationFound(location);
        }else {
            checkCount++;
        }
    }

    private void locationFailed(final ROSLocationServiceListener listener) {
        locationManager.removeUpdates(locationListener);
        listener.onLocationFailed();
    }
}
