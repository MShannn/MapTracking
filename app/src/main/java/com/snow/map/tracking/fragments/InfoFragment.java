package com.snow.map.tracking.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.snow.map.tracking.AppConfigs;
import com.snow.map.tracking.DataBase.UserData;
import com.snow.map.tracking.R;
import com.snow.map.tracking.Validator.Validations;
import com.snow.map.tracking.models.UserRecord;
import com.snow.map.tracking.services.LocationData;
import com.snow.map.tracking.services.TrackingLocationService;
import com.snow.map.tracking.utils.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;


public class InfoFragment extends Fragment {
    String userId,email,name;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    Handler handler;
    int a=3;
    int Seconds, Minutes, MilliSeconds;
    private DatabaseReference mDatabase;
    private TextView txt_timer;
    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            txt_timer.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);
        }

    };
    private TextView txt_date;
    private TextView txt_miles;
    private Button btn_start;
    private Button btn_stop;
    private Button btn_save;
    private BroadcastReceiver locationChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int action = intent.getIntExtra(TrackingLocationService.LOCATION_SERVICE_UPDATE, 0);
            switch (action) {
                case TrackingLocationService.ACTION_LOCATION_CHANGED:

                    if(a==2) {
                        updateSpeedInfo();
                        Log.d("SHAN","  updating"+a);
                    }
                    else {
                        Log.d("SHAN"," cant update"+a);
                    }
                    //updateWheatherInfo();
                    break;
                default:
                    break;
            }
        }
    };

    private void updateSpeedInfo() {
        // txtSpeed.setText(String.valueOf(LocationData.getInstance().getCurrentSpeed()));
        // txtMaxSpeed.setText(String.valueOf(LocationData.getInstance().getMaxSpeed()));
        txt_miles.setText(String.format("%4.1f", LocationData.getInstance().getDistance()));
        // txtAvgSpeed.setText(String.valueOf(LocationData.getInstance().getAvgSpeed()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_fragment, container, false);
        ViewUtils.setTypeface(AppConfigs.getInstance().ROBOTO_CONDENSED_REGULAR, view);
        txt_timer = (TextView) view.findViewById(R.id.txt_timer);
        txt_date = (TextView) view.findViewById(R.id.txt_date);
        txt_miles = (TextView) view.findViewById(R.id.txt_miles);
        handler = new Handler();

        btn_start = (Button) view.findViewById(R.id.btn_start);
        btn_stop = (Button) view.findViewById(R.id.btn_stop);
        btn_save = (Button) view.findViewById(R.id.btn_save);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Realm realm = Realm.getDefaultInstance();

                UserData userData = realm.where(UserData.class).findFirst();
                if (userData != null) {
                    userId = userData.getToken();
                    email=userData.getEmail();
                }


                mDatabase = FirebaseDatabase.getInstance().getReference();

                DatabaseReference users = mDatabase.child("users");
                DatabaseReference usersRef = users.child(userId);

                //UserRecord user = new UserRecord( txt_timer.getText().toString(),txt_date.getText().toString(),1.2);


                txt_miles.setText("10.9");
                UserRecord user = new UserRecord(txt_timer.getText().toString(), txt_date.getText().toString(), Validations.roundTwoDecimal(Float.parseFloat(txt_miles.getText().toString())),email);

                String userIdd = usersRef.push().getKey();
                usersRef.child(userIdd).setValue(user);

                Toast.makeText(getActivity().getApplicationContext(), "Record has been saved", Toast.LENGTH_SHORT).show();


            }
        });


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd ");
        String currentDateandTime = sdf.format(new Date());
        txt_date.setText(currentDateandTime);


        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                a=2;


            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                a=3;
                TimeBuff += MillisecondTime;

                handler.removeCallbacks(runnable);


            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                locationChange, new IntentFilter(TrackingLocationService.LOCATION_SERVICE_UPDATE));
        //Update Speed info when resume
        if (LocationData.getInstance().getCurrentLocation() != null) {
            Intent intent = new Intent(TrackingLocationService.LOCATION_SERVICE_UPDATE);
            intent.putExtra(TrackingLocationService.LOCATION_SERVICE_UPDATE, TrackingLocationService.ACTION_LOCATION_CHANGED);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        }
        //update unit info
        // updateUnit();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(locationChange);
        super.onPause();
    }


}
