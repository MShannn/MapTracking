package com.snow.map.tracking.fragments;

import android.Manifest;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.snow.map.tracking.R;
import com.snow.map.tracking.services.LocationData;
import com.snow.map.tracking.services.TrackingLocationService;

import java.util.ArrayList;


public class CustomMapFragment extends Fragment {

    private MapView mapView;
    private GoogleMap map;
    private ArrayList<LatLng> points; //added
    Polyline line; //added
    int a=0;
    double clat,clong;
    GoogleMap mMap;


    private BroadcastReceiver locationServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int action = intent.getIntExtra(TrackingLocationService.LOCATION_SERVICE_UPDATE, 0);
            switch (action) {
                case TrackingLocationService.ACTION_LOCATION_CHANGED:
                    Location location = LocationData.getInstance().getCurrentLocation();
                    if (location != null) {
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(
                                new LatLng(location.getLatitude(), location.getLongitude()));
                        map.moveCamera(cameraUpdate);
                    }
                    break;
                default:

            }
        }
    };

    public void setMapType(int mapType) {
        map.setMapType(mapType);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_map_fragment, container, false);
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        points = new ArrayList<LatLng>(); //added

        map = mapView.getMap();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && getActivity().checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && getActivity().checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    2);
        } else {
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.setMyLocationEnabled(true);
            //   gps functions.
        }


        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Location loc = LocationData.getInstance().getCurrentLocation();
        if (loc != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 16);

            points.add(new LatLng(loc.getLatitude(),loc.getLongitude()));
         //   redrawLine();

            map.animateCamera(cameraUpdate);
        } else {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 16);
            map.animateCamera(cameraUpdate);
        }

        return v;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(locationServiceReceiver,
                new IntentFilter(TrackingLocationService.LOCATION_SERVICE_UPDATE));
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(locationServiceReceiver);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == 2
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            map.getUiSettings().setMyLocationButtonEnabled(true);
            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMyLocationEnabled(true);

        }
    }


    private void redrawLine(){
        map.clear();

        PolylineOptions options = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
            Log.d("SHAN"," points"+points.get(i).longitude);

        }

   /*     Polyline line = map.addPolyline(new PolylineOptions()
                .add(new LatLng(31.0008, 74.2686),
                        new LatLng(31.5101,74.3414))
                .width(2).color(Color.BLUE).geodesic(true));*/

        //clears all Markers and Polylines

   /*     map.clear();
        Log.d("SHAN"," redraw line poly"+points.size());

        PolylineOptions options = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);

        }
       // addMarker(); //add Marker in current position;/
        line = map.addPolyline(options); //add Polyline*/
    }
}
