package com.sumit.ibox.ui.teacher;

import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.UploadAttandanceStudentListAdapter;
import com.sumit.ibox.model.UploadAttandanceStudentListData;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    private RecyclerView attandanceUploadStudentList;
    public static List<UploadAttandanceStudentListData> uploadAttandanceStudentListarray = new ArrayList<>();
    UploadAttandanceStudentListAdapter uploadAttandanceStudentListAdapter;

    Context context;
    Toolbar toolbar;
    LinearLayout searchLayout;
    Spinner klass, division, period;
    AlertDialog errorDialog;
    ProgressDialog loading;
    ImageView save;

    String classString, divisionString, periodString,
            session, institutionId;
    String classId, sectionId;
    VolleyCallback studentSearchCallback, uploadAttendenceCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attandance_teacher);

        init();
        setupSpinners();
        setupRecyclerView();
        setupVolleyCallback();
        setUpToolbar();

    }

    private void init() {
        context = this;
        save = findViewById(R.id.attendance_save);
        searchLayout = findViewById(R.id.attandence_search_layout);
        searchLayout.setVisibility(View.VISIBLE);
        klass = findViewById(R.id.attendance_class);
        division = findViewById(R.id.attendance_division);
        period = findViewById(R.id.attendance_period);
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

    private void setUpToolbar(){

        toolbar = findViewById(R.id.attandance_toolbar);
        toolbar.setTitle("Upload attendance");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void setupSpinners() {
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constant.classList);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        klass.setAdapter(classAdapter);
        ArrayAdapter<String> divisionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constant.sectionList);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        division.setAdapter(divisionAdapter);
        ArrayAdapter<String> periodAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constant.periodList);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        period.setAdapter(periodAdapter);
    }


    private void setupRecyclerView() {
        attandanceUploadStudentList = findViewById(R.id.upload_attandance_student_list);
        uploadAttandanceStudentListAdapter = new UploadAttandanceStudentListAdapter(this, uploadAttandanceStudentListarray);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        attandanceUploadStudentList.setLayoutManager(mLayoutManager);
        attandanceUploadStudentList.setAdapter(uploadAttandanceStudentListAdapter);
//        attandanceData();
    }

    private void setupVolleyCallback() {
        studentSearchCallback = new VolleyCallback() {
            @Override
            public void onSuccess(Object result) {
                loading.dismiss();
                searchLayout.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
                JSONObject jsonObject = (JSONObject) result;
                UploadAttandanceStudentListData studentData;
                try {//class_id, section_id
                    JSONArray jsonArray = jsonObject.getJSONArray("details");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject post = jsonArray.getJSONObject(i);
                        String studentId = post.getString("id");
                        String studentRoll = post.getString("roll");
                        String studentName = post.getString("name");
                        classId = post.getString("class_id");
                        sectionId = post.getString("section_id");
                        studentData = new UploadAttandanceStudentListData(1,studentName,classString,divisionString,
                                studentRoll,studentId, Constant.ATTENDENCE_PRESENT);
                        uploadAttandanceStudentListarray.add(studentData);
                    }
                    uploadAttandanceStudentListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Utils.logPrint(getClass(),"response error1", Log.getStackTraceString(e));
                    errorDialog.setMessage(getString(R.string.error_occurred));
                    errorDialog.show();
                }

            }

            @Override
            public void noDataFound() {
                loading.dismiss();
                errorDialog.setMessage(getString(R.string.no_student)+" "+classString+"("+divisionString+")");
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

        uploadAttendenceCallback = new VolleyCallback() {
            @Override
            public void onSuccess(Object result) {
                loading.dismiss();
                errorDialog.setMessage(getString(R.string.attendance_uploaded));
                errorDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                        onBackPressed();
                    }
                });
                errorDialog.show();
            }

            @Override
            public void noDataFound() {
                loading.dismiss();
                errorDialog.setMessage(getString(R.string.error_occurred));
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
    }

    public void searchPresses(View view) {
        uploadAttandanceStudentListarray.clear();
        uploadAttandanceStudentListAdapter.notifyDataSetChanged();
        save.setVisibility(View.GONE);
        classString = klass.getSelectedItem().toString();
        divisionString = division.getSelectedItem().toString();
        periodString = period.getSelectedItem().toString();
        institutionId = Utils.getString(context, Constant.KEY_INSTITUTION_ID, Constant.DUMMY);
        session = Utils.getString(context, Constant.KEY_SESSION, Constant.DUMMY);
        loading.show();
        Perform.searchStudentList(context, studentSearchCallback, institutionId, classString, divisionString, session);
    }

    public void savePressed(View view) {
        ArrayList<String> presentStudentList = new ArrayList<>();
        ArrayList<String> absentStudentList = new ArrayList<>();
        for (UploadAttandanceStudentListData singleStudentData: uploadAttandanceStudentListarray) {
            if(singleStudentData.getAttendence() == Constant.ATTENDENCE_PRESENT){
                presentStudentList.add(singleStudentData.getStudentId());
            }
            if(singleStudentData.getAttendence() == Constant.ATTENDENCE_ABSENT){
                absentStudentList.add(singleStudentData.getStudentId());
            }
        }
        String date = Utils.getTimestampInFormat(System.currentTimeMillis(),Constant.SERVER_DATE_FORMAT);
        String teacherId = Utils.getString(context, Constant.KEY_USER_ID, Constant.DUMMY);
        String presentList = Utils.makeCommaSeparated(presentStudentList);
        String absentList = Utils.makeCommaSeparated(absentStudentList);
        Perform.uploadAttendence(context, uploadAttendenceCallback, institutionId, classId, sectionId, session, teacherId
        ,date,periodString,presentList,absentList);
        loading.show();
    }

    @Override
    public void onBackPressed() {
        if(searchLayout.getVisibility() == View.GONE){
            searchLayout.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }
    }

}
