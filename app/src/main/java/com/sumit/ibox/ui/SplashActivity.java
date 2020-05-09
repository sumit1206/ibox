package com.sumit.ibox.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.ui.student.*;
import com.sumit.ibox.ui.teacher.*;

public class SplashActivity extends AppCompatActivity {

    Context context;
    Intent loginIntent;
    Intent studentMainIntent, teacherMainIntent;
    AlertDialog errorDialog;
    boolean alreadyLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Utils.logPrint(this.getClass(),"Splash","executed");

        init();
        if(permissionsGranted()) {
            startNextActivity();
        }
    }

    private void init() {
        context = this;
        loginIntent = new Intent(context, LoginActivity.class);
        studentMainIntent = new Intent(context, StudentMain.class);
        teacherMainIntent = new Intent(context, TeacherMain.class);
        errorDialog = new AlertDialog.Builder(context)
                .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permissionsGranted();
                    }
                })
                .setMessage(getString(R.string.permission_needed))
                .setCancelable(false)
                .create();
    }

    private boolean permissionsGranted() {
        for(String permission: Constant.permissionArray){
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, Constant.permissionArray, Constant.PERMISSION_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.PERMISSION_REQUEST_CODE && grantResults.length == Constant.permissionArray.length) {
            for (int i = 0;i < grantResults.length; i++){
                int result = grantResults[i];
                String permission = permissions[i];
                if(result == PackageManager.PERMISSION_DENIED &&
                        (!permission.equals(Manifest.permission.FOREGROUND_SERVICE) &&
                                !permission.equals(Manifest.permission.REQUEST_COMPANION_RUN_IN_BACKGROUND) &&
                                !permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION))){
                    errorDialog.show();
                    return;
                }
            }
            startNextActivity();
        }
    }


    private void startNextActivity() {
        alreadyLoggedIn = Utils.getBoolean(context, Constant.KEY_LOGGED_IN, false);
        if(alreadyLoggedIn){
            int lastOpened = Utils.getInt(context,Constant.KEY_ACTIVITY_TO_LOAD,99);
            if(lastOpened == 99) {//logically never triggered
                int type = Utils.getInt(context, Constant.KEY_USER_TYPE, Constant.TYPE_PARENT);
                if (type == Constant.TYPE_PARENT || type == Constant.TYPE_BOTH) {
                    startActivity(studentMainIntent);
                } else if (type == Constant.TYPE_TEACHER) {
                    startActivity(teacherMainIntent);
                }
            }else {
                loginIntent.putExtra(Constant.KEY_ACTIVITY_TO_LOAD, lastOpened);
                startActivity(loginIntent);
            }
        }else {
            start(loginIntent);
        }
    }

    private void start(final Intent intent) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
