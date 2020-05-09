package com.sumit.ibox.ui.student.result;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.StudentResultListAdapter;
import com.sumit.ibox.model.StudentResultListData;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultList extends AppCompatActivity {

    private RecyclerView studentResultListRecyclerView;
    private List<StudentResultListData> studentResultListDataArray;
    StudentResultListAdapter studentResultListAdapter;

    Toolbar toolbar;
    Context context;
    VolleyCallback resultCallback;
    ProgressDialog loading;
    AlertDialog errorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        init();
        setupRecyclerView();
        setupVolleyCallback();
        fetchResult();
//        setup();
    }

    private void init() {
        context = this;
        toolbar = findViewById(R.id.result_toolbar);
        toolbar.setTitle("Result");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

    private void setupVolleyCallback() {
        resultCallback = new VolleyCallback() {
            @Override
            public void onSuccess(Object result) {
                loading.dismiss();
                JSONObject jsonObject = (JSONObject) result;
                StudentResultListData resultData;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("details");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject post = jsonArray.getJSONObject(i);
                        //"exam_name":"test","percent":"90","grade":"a","total":"90","session":"1","date":"91919191"}]}
                        String examName = post.getString("exam_name");
                        String percent = post.getString("percent");
                        String grade = post.getString("grade");
                        String total = post.getString("total");
                        String date = post.getString("date");
                        date = Utils.getTimestampInFormat(Long.valueOf(date), "dd MMM,yy");
                        resultData = new StudentResultListData(examName, percent, grade, total, date);
                        studentResultListDataArray.add(resultData);
                    }
                    studentResultListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Utils.logPrint(getClass(),"response error1", Log.getStackTraceString(e));
                    errorDialog.setMessage(getString(R.string.error_occurred));
                    errorDialog.show();
                }
            }

            @Override
            public void noDataFound() {
                loading.dismiss();
                errorDialog.setMessage(getString(R.string.no_result));
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

    private void setupRecyclerView() {
        studentResultListRecyclerView = findViewById(R.id.student_result_list_recycler_view);
        studentResultListDataArray = new ArrayList<>();
        studentResultListAdapter = new StudentResultListAdapter(this,studentResultListDataArray);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        studentResultListRecyclerView.setLayoutManager(mLayoutManager);
        studentResultListRecyclerView.setAdapter(studentResultListAdapter);
    }

    private void fetchResult(){
        String studentId = Utils.getString(context, Constant.KEY_STUDENT_ID, Constant.DUMMY); //"1111"
        String session = Utils.getString(context, Constant.KEY_SESSION, Constant.DUMMY); //"11"
        loading.show();
        Perform.resultFetch(context, resultCallback, studentId, session);
    }
}
