package com.snow.map.tracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.snow.map.tracking.DataBase.UserData;
import com.snow.map.tracking.Dialogs.Processing;
import com.snow.map.tracking.Validator.Validations;

import io.realm.Realm;

public class SignupActivity extends AppCompatActivity {

    private EditText edt_fname;
    private EditText edt_lname;
    private EditText edt_email;
    private EditText edt_password;
    private EditText edt_cpassword;

    private Button btn_signup;
    CircularProgressView progress_view;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Signup");




        mAuth = FirebaseAuth.getInstance();

        init();
        progress_view.setVisibility(View.INVISIBLE);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filedValidation();
            }
        });

    }

    public void init() {

        edt_fname = (EditText) findViewById(R.id.edt_fname);
        edt_lname = (EditText) findViewById(R.id.edt_lname);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);
        edt_cpassword = (EditText) findViewById(R.id.edt_cpassword);

        btn_signup = (Button) findViewById(R.id.btn_signup);
        progress_view= (CircularProgressView) findViewById(R.id.progress_view);

    }


    public void filedValidation() {

        if (edt_fname.getText().toString().length() < 1) {
            Processing.myToast(getApplicationContext(), " First name can't be empty");
        } else if (edt_fname.getText().toString().length() < 3) {
            Processing.myToast(getApplicationContext(), "Name required minimum four characters");
        } else if (edt_lname.getText().toString().length() < 1) {
            Processing.myToast(getApplicationContext(), " Last name can't be empty");
        } else if (edt_lname.getText().toString().length() < 3) {
            Processing.myToast(getApplicationContext(), "Name required minimum four characters");
        } else if (edt_email.getText().toString().length() < 1) {
            Processing.myToast(getApplicationContext(), " Email can't be empty");
        } else if (!(Validations.isEmailValid(edt_email.getText().toString()))) {
            Processing.myToast(getApplicationContext(), "Email is not valid!");
        } else if (edt_password.getText().toString().length() < 1) {
            Processing.myToast(getApplicationContext(), " Password can't be empty");
        } else if (edt_password.getText().toString().length() < 6) {
            Processing.myToast(getApplicationContext(), "Password required minimum six characters");
        } else if (edt_cpassword.getText().toString().length() < 1) {
            Processing.myToast(getApplicationContext(), " Confirm password can't be empty");
        } else if (edt_cpassword.getText().toString().length() < 6) {
            Processing.myToast(getApplicationContext(), "Password required minimum six characters");
        } else if (!(edt_password.getText().toString().equals(edt_cpassword.getText().toString()))) {
            Processing.myToast(getApplicationContext(), " Password not matched");
        } else {

            userSignup(edt_email.getText().toString(), edt_cpassword.getText().toString());


        }


    }

    public void userSignup(String email, String password) {

        progress_view.setVisibility(View.VISIBLE);


        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("SHAN", "createUserWithEmail:onComplete:" + task.isSuccessful());
                      /*  Log.d("SHAN", "createUserWithEmail:resutl:" + task.getResult());
                        Log.d("SHAN", "createUserWithEmail:error:" + task.getException());*/



                        if (task.isSuccessful()) {

                            // Saving data in database

                            Realm realm = Realm.getDefaultInstance();
                            if (realm.where(UserData.class).count() > 0) {

                                realm.beginTransaction();
                                realm.clear(UserData.class);
                                realm.commitTransaction();

                            }

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            realm.beginTransaction();
                            UserData userData = realm.createObject(UserData.class);
                            userData.setEmail(edt_email.getText().toString());
                            userData.setPassword(edt_password.getText().toString());
                            userData.setName(edt_fname.getText().toString()+" "+edt_lname.getText().toString());
                            userData.setToken(user.getUid());
                            realm.commitTransaction();

                            //start home activity

                            realm.close();



                            progress_view.setVisibility(View.INVISIBLE);


                            Log.d("SHAN"," current user signup"+user.getUid());
                            Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                            finish();
                            startActivity(intent);
                        }else {
                            Toast.makeText(SignupActivity.this, "Signup error", Toast.LENGTH_SHORT).show();
                            progress_view.setVisibility(View.INVISIBLE);
                        }




                        // [END_EXCLUDE]
                    }
                });
    }

}
