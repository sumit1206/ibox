package com.sumit.ibox.ui.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sumit.ibox.IBoxApp;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.services.UpdateHolidayList;
import com.sumit.ibox.services.UpdateTeacherRoutine;

public class TeacherMain extends AppCompatActivity {

    Context context;
    boolean doubleBackToExitPressedOnce = false;
    BottomNavigationView navView;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
        Utils.logPrint(getClass(),"TeacherMain","onCreate");
        IBoxApp.updateProfiles();
        init();
        Utils.saveInt(context, Constant.KEY_ACTIVITY_TO_LOAD, Constant.TYPE_TEACHER);
        Utils.saveString(context, Constant.KEY_SESSION,"2020-2021");
        NavigationUI.setupWithNavController(navView, navController);
        new UpdateTeacherRoutine().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new UpdateHolidayList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void init() {
        context = this;
        navView = findViewById(R.id.teacher_nav_view);
        navController = Navigation.findNavController(this, R.id.teacher_nav_host_fragment);
    }

    @Override
    public void onBackPressed() {
        if(navView.getSelectedItemId() != R.id.navigation_home){
            navView.setSelectedItemId(R.id.navigation_home);
            return;
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        doubleBackToExitPressedOnce = true;
        Utils.showShortToast(context,getString(R.string.double_back));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}