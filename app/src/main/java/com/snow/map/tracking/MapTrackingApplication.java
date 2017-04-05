package com.snow.map.tracking;

import android.app.Application;
import android.graphics.Typeface;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MapTrackingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("com.muhammadshan.it")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);


        AppConfigs.getInstance().ROBOTO_THIN = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        AppConfigs.getInstance().ROBOTO_CONDENSED_REGULAR = Typeface.createFromAsset(getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        AppConfigs.getInstance().OPEN_WHEATHER_MAP_API_KEY = getString(R.string
            .open_weather_map_api);
    }
}
