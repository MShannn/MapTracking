package com.snow.map.tracking;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.snow.map.tracking.DataBase.UserData;
import com.snow.map.tracking.Dialogs.Processing;
import com.snow.map.tracking.adapters.RecyclerViewItemClickListener;
import com.snow.map.tracking.fragments.CustomMapFragment;
import com.snow.map.tracking.fragments.DrawerFragment;
import com.snow.map.tracking.fragments.InfoFragment;
import com.snow.map.tracking.models.NavDrawerItem;
import com.snow.map.tracking.services.TrackingLocationService;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class HomeActivity extends AppCompatActivity implements DrawerFragment.GetNavItemsCallback {

    @Override
    public RecyclerViewItemClickListener getItemClickListener() {
        return new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                switch (position) {
                    case 0: //Home

                        break;
                    case 1: //History
                        Intent intent = new Intent(HomeActivity.this, History.class);
                        startActivity(intent);
                        break;
                    case 2: //Settings
                        Intent settings = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(settings);
                        break;
                    case 3: //Logout




                        Handler h=new Handler();
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Realm realm = Realm.getDefaultInstance();
                                if (realm.where(UserData.class).count() > 0) {

                                    realm.beginTransaction();
                                    realm.clear(UserData.class);
                                    realm.commitTransaction();

                                }


                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        },2000);



                        break;
                    case 4: //About
                        AboutDialog dialog = new AboutDialog(HomeActivity.this);
                        dialog.show();
                        break;
                }
                drawerFragment.closeNavDrawer();
            }
        };
    }

    @Override
    public List<NavDrawerItem> getNavItems() {
        List<NavDrawerItem> items = new ArrayList<>();
        items.add(new NavDrawerItem(R.drawable.ic_home, "Home"));
        items.add(new NavDrawerItem(R.drawable.ic_history, "History"));
        items.add(new NavDrawerItem(R.drawable.ic_settings, "Settings"));
        items.add(new NavDrawerItem(R.drawable.ic_logout, "Logout"));
        items.add(new NavDrawerItem(R.drawable.ic_about, "About"));
        return items;
    }

    private View home_layout;
    private DrawerFragment drawerFragment;
    private Toolbar toolbar;
    private CustomMapFragment mapFragment;
    private InfoFragment infoFragment;
    private FragmentTransaction transaction;

    //ADS


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Start Location Service
        Intent locationService = new Intent(getBaseContext(), TrackingLocationService.class);
        startService(locationService);

        drawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setup(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                toolbar);

        //Add MAP
        mapFragment = new CustomMapFragment();
        transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.mapContainer, mapFragment);
        //Add Info Fragment
        infoFragment = new InfoFragment();
        transaction.add(R.id.infoContainer, infoFragment);
        transaction.commit();

    }
/*
    private void loadPreferences() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String unit = sp.getString("UNIT_SYSTEM", "imperial");
        if (unit.equals("imperial")) {
            AppConfigs.getInstance().IS_METRIC_SYSTEM_UNIT = false;
        } else {
            AppConfigs.getInstance().IS_METRIC_SYSTEM_UNIT = true;
        }

    }*/



    @Override
    protected void onDestroy() {
        Intent locationService = new Intent(getBaseContext(), TrackingLocationService.class);
        stopService(locationService);

        super.onDestroy();
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settings = new Intent(HomeActivity.this, SettingActivity.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

}