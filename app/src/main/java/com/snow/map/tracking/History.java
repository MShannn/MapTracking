package com.snow.map.tracking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.snow.map.tracking.DataBase.UserData;
import com.snow.map.tracking.Validator.Validations;
import com.snow.map.tracking.adapters.HistoryAdapter;
import com.snow.map.tracking.models.UserRecord;

import java.util.ArrayList;

import io.realm.Realm;

public class History extends AppCompatActivity {


    ArrayList<UserRecord> list;
    HistoryAdapter historyAdapter;
    double totalMiles = 0.0;
    FrameLayout frame;
    CircularProgressView progressView;
    private RecyclerView recyclerView;
    private TextView txt_totalMile;
    private TextView txt_label;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        frame = (FrameLayout) findViewById(R.id.frame);
        frame.setVisibility(View.INVISIBLE);
        progressView = (CircularProgressView) findViewById(R.id.progress_view);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        txt_totalMile = (TextView) findViewById(R.id.txt_totalMiles);
        txt_label = (TextView) findViewById(R.id.txt_label);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        list = new ArrayList<>();

        getData();


    }

    public void getData() {


        frame.setVisibility(View.VISIBLE);

        Realm realm = Realm.getDefaultInstance();
        UserData userData = realm.where(UserData.class).findFirst();
        if (userData != null) {
            userData.getToken();
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference users = mDatabase.child("users");
        DatabaseReference usersRef = users.child(userData.getToken());


        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot msgSnapshot : dataSnapshot.getChildren()) {
                    UserRecord userRecord = msgSnapshot.getValue(UserRecord.class);


                    totalMiles = totalMiles + userRecord.getMiles();


                    Log.d("SHAN", "mies  " + totalMiles);
                    Log.d("SHAN", "mies  ob " + userRecord.getMiles());

                    list.add(userRecord);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        frame.setVisibility(View.INVISIBLE);


                        if (list.size() < 1) {

                            txt_label.setText("No record available");
                            txt_totalMile.setText("");

                        } else txt_totalMile.setText(Validations.roundTwoDecimal(totalMiles) + "");

                        historyAdapter = new HistoryAdapter(getApplicationContext(), list);
                        recyclerView.setAdapter(historyAdapter);
                    }
                });
                Log.d("SHAN", "list size" + list.size());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("SHAN", "Failed to read value.", error.toException());
            }
        });


    }


}
