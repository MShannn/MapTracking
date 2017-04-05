package com.snow.map.tracking;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.snow.map.tracking.DataBase.UserData;

import io.realm.Realm;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler h=new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent=null;
                Realm realm=Realm.getDefaultInstance();

                UserData userData=realm.where(UserData.class).findFirst();
                if (userData!=null){

                    intent=new Intent(getApplicationContext(),HomeActivity.class);
                    finish();
                    startActivity(intent);
                }else {
                    intent=new Intent(getApplicationContext(),LoginActivity.class);
                    finish();
                    startActivity(intent);
                }


            }
        },4000);




    }
}
