package com.snow.map.tracking.services;

import android.location.Location;

import com.snow.map.tracking.AppConfigs;
import com.snow.map.tracking.utils.LocationsUtils;
import com.snow.map.tracking.utils.SpeedUtils;

public class LocationData {
    private static LocationData _instance;

    private LocationData() {

    }

    public static LocationData getInstance() {
        if (_instance == null) {
            _instance = new LocationData();
        }

        return _instance;
    }

    private Location currLocation;
    private Location prevLocation;
    private float currSpeed; //Current Speed in m/s
    private float maxSpeed; //Max Speed in m/s
    private float avgSpeed; //Averge Speed in m/s
    private float distance; //Distance in meter
    private long time;

    private int convertSpeed(float sp) {
        return AppConfigs.getInstance().IS_METRIC_SYSTEM_UNIT ?
                SpeedUtils.toKmph(sp) :
                SpeedUtils.toMph(sp);
    }

    public long getTime() {
        return time;
    }

    public String getSpeedUnit() {
        return AppConfigs.getInstance().IS_METRIC_SYSTEM_UNIT ?
                "kmh" : "mph";
    }

    public String getDistanceUnit() {
        return AppConfigs.getInstance().IS_METRIC_SYSTEM_UNIT ?
                "mi" : "mi";
    }

    public int getCurrentSpeed() {
        return convertSpeed(currSpeed);
    }

    public float getDistance() {
        return AppConfigs.getInstance().IS_METRIC_SYSTEM_UNIT ? SpeedUtils.toMiles(distance)
                : SpeedUtils.toMiles(distance);
    }

    public int getMaxSpeed() {
        return convertSpeed(maxSpeed);
    }

    public int getAvgSpeed() {
        return convertSpeed(avgSpeed);
    }

    public Location getCurrentLocation() {
        return currLocation;
    }

    public void setCurrLocation(Location location) {
        prevLocation = currLocation;
        currLocation = location;
        if (prevLocation != null) {
            this.currSpeed = LocationsUtils.getSpeed(prevLocation, currLocation);
            if (this.maxSpeed < currSpeed) {
                this.maxSpeed = currSpeed;
            }
            this.distance += LocationsUtils.calculateDistance(prevLocation, currLocation);
            this.time += LocationsUtils.timeSpan(prevLocation, currLocation);
            int sec = (int) (this.time / 1000.0f);
            this.avgSpeed = this.distance / sec;
            this.avgSpeed = this.distance / (this.time / 1000.0f);
        }
    }

    public Location getPreviousLocation() {
        return prevLocation;
    }
}
