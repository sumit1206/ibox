package com.sumit.ibox.ui.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sumit.ibox.model.ProgressDialog;

import com.sumit.ibox.model.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.NotificationAdapter;
import com.sumit.ibox.model.NotificationData;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Notification extends AppCompatActivity {
    private Context context;
    private RecyclerView notificationRecyclerView;
    private List<NotificationData> notificationDataArrayList = new ArrayList<>();
    NotificationAdapter notificationAdapter;
    Toolbar notificationToolbar;
    ProgressDialog loading;
    AlertDialog errorDialog;

    int userType;
    String teacherId, studentId;

    VolleyCallback notificationCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            try {//success, details, notify, date, time, url, user_type
                NotificationData notificationData;
                JSONObject jsonObject = (JSONObject) result;
                int success = jsonObject.getInt("success");
                if(success == 1){
                    JSONArray jsonArray = jsonObject.getJSONArray("details");
                    for(int i = 0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String title = jsonObject1.getString("notify");
                        String date = jsonObject1.getString("date");
                        String time = jsonObject1.getString("time");
                        String dateTime = date + " " + time;
                        String url = jsonObject1.getString("url");
                        int senderType = jsonObject1.getInt("user_type");
                        notificationData = new NotificationData(title, dateTime, url, senderType);
                        notificationDataArrayList.add(notificationData);
                    }
                }
                notificationAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Utils.errorLog(getClass(),"Error parsing json",e);
                errorDialog.setMessage(getString(R.string.error_occurred));
                errorDialog.show();
            }
        }

        @Override
        public void noDataFound() {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.no_notification));
            errorDialog.show();
        }

        @Override
        public void onCatch(JSONException e) {
            loading.dismiss();
            Utils.logPrint(getClass(),"response error2", Log.getStackTraceString(e));
            errorDialog.setMessage(getString(R.string.error_occurred));
            errorDialog.show();
        }

        @Override
        public void onError(VolleyError e) {
            loading.dismiss();
            Utils.logPrint(getClass(),"no response error", Log.getStackTraceString(e));
            errorDialog.setMessage(getString(R.string.error_occurred));
            errorDialog.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        init();
        toolbarSetUp();
        setUpRecyclerView();
        boolean hasParams = fetchParams(getIntent());
        if(hasParams){
            fetchNotification();
        }
    }

    private void init() {
        context = this;
        loading = new ProgressDialog(context);
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        errorDialog = new AlertDialog.Builder(context)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        errorDialog.cancel();
                    }
                })
                .create();
    }

    private boolean fetchParams(Intent intent) {
        if(intent.hasExtra(Constant.KEY_USER_TYPE)){
            int type = intent.getIntExtra(Constant.KEY_USER_TYPE,99);
            if(type == 99)
                return false;
            this.userType = type;
            if(type == Constant.TYPE_TEACHER){
                this.teacherId = Utils.getString(context, Constant.KEY_USER_ID, Constant.DUMMY);
                this.studentId = "";
            }else if(type == Constant.TYPE_PARENT){
                this.teacherId = "";
                this.studentId = Utils.getString(context, Constant.KEY_STUDENT_ID, Constant.DUMMY);
            }
            return true;
        }else {
            return false;
        }
    }

    private void toolbarSetUp(){
        notificationToolbar = findViewById(R.id.notification_toolbar);
        notificationToolbar.setTitle(R.string.notification);
        notificationToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        notificationToolbar.setNavigationIcon(R.drawable.arrow_left);
        notificationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void setUpRecyclerView(){
        notificationRecyclerView = findViewById(R.id.notification_recycler_view);
        notificationAdapter = new NotificationAdapter(notificationDataArrayList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        notificationRecyclerView.setLayoutManager(mLayoutManager);
        notificationRecyclerView.setAdapter(notificationAdapter);
    }

    private void fetchNotification() {
        loading.show();
        Perform.fetchNotification(context, notificationCallback, userType, studentId, teacherId);
    }
}
