package com.snow.map.tracking.Dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.snow.map.tracking.R;


/**
 * Created by Muhammad Shan on 27/12/2016.
 */

public class Processing {

    static ProgressDialog progressDialog;
    public static void showProgressDialog(Context context, String message) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public static void stopProgressDialog() {
        progressDialog.dismiss();
    }

    public static void myToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
