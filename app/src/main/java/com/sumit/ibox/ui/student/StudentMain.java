package com.sumit.ibox.ui.student;

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
import com.sumit.ibox.services.UpdateStudentRoutine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class StudentMain extends AppCompatActivity {

    Context context;
    BottomNavigationView navView;
    NavController navController;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.logPrint(getClass(),"StudentMain","onCreate");
        IBoxApp.updateProfiles();
        init();
        Utils.saveInt(context, Constant.KEY_ACTIVITY_TO_LOAD, Constant.TYPE_PARENT);
        Utils.saveString(context, Constant.KEY_SESSION,"2020-2021");

        NavigationUI.setupWithNavController(navView, navController);
        new UpdateStudentRoutine().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new UpdateHolidayList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void init() {
        context = this;
        navView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
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
