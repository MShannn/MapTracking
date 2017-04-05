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
import android.widget.LinearLayout;
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

public class LoginActivity extends AppCompatActivity {


    CircularProgressView progress_view;
    private EditText edt_email;
    private EditText edt_password;
    private Button btn_login;
    private LinearLayout ll_signup;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Login");

        mAuth = FirebaseAuth.getInstance();

        inti();

        progress_view.setVisibility(View.INVISIBLE);

        ll_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filedValidation();
            }
        });


    }


    public void inti() {


        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        ll_signup = (LinearLayout) findViewById(R.id.ll_signup);
        progress_view= (CircularProgressView) findViewById(R.id.progress_view);

    }

    public void filedValidation() {

        if (edt_email.getText().toString().length() < 1) {
            Processing.myToast(getApplicationContext(), " Email can't be empty");
        } else if (!(Validations.isEmailValid(edt_email.getText().toString()))) {
            Processing.myToast(getApplicationContext(), "Email is not valid!");
        } else if (edt_password.getText().toString().length() < 1) {
            Processing.myToast(getApplicationContext(), " Password can't be empty");
        } else if (edt_password.getText().toString().length() < 6) {
            Processing.myToast(getApplicationContext(), "Password required minimum six characters");
        } else {

            signIn(edt_email.getText().toString(), edt_password.getText().toString());

        }

    }

    private void signIn(String email, String password) {

        progress_view.setVisibility(View.VISIBLE);
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("SHAN", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            progress_view.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();

                        }


                        // [START_EXCLUDE]
                        if (task.isSuccessful()) {


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
                            userData.setToken(user.getUid());
                            realm.commitTransaction();
                            Log.d("SHAN","login token"+user.getUid());



                            progress_view.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            finish();
                            startActivity(intent);

                        }



                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }


/*    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }*/
}
