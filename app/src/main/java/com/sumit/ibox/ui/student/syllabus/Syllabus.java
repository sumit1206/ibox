package com.sumit.ibox.ui.student.syllabus;

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
import com.sumit.ibox.controller.SyllabusAdapter;
import com.sumit.ibox.local.CookiesAdapter;
import com.sumit.ibox.local.CookiesAttribute;
import com.sumit.ibox.model.SyllabusData;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Syllabus extends AppCompatActivity {

    Context context;
    SyllabusAdapter syllabusAdapter;
    RecyclerView syllabusRecyclerView;
    ArrayList<SyllabusData> syllabusDataArrayList = new ArrayList<>();
    ProgressDialog loading;
    AlertDialog errorDialog;

    VolleyCallback syllabusCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            SyllabusData syllabusData;
            JSONObject jsonObject = (JSONObject) result;
            try {//success, details, examName, subjectName, teacherName, publichDate, syllabusLink
                JSONArray jsonArray = jsonObject.getJSONArray("details");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject post = jsonArray.getJSONObject(i);
                    String examName = post.getString("examName");
                    String subjectName = post.getString("subjectName");
                    String teacherName = post.getString("teacherName");
                    String publichDate = post.getString("publichDate");
                    String syllabusLink = post.getString("syllabusLink");
                    publichDate = Utils.getTimestampInFormat(Long.valueOf(publichDate),Constant.APP_DATE_FORMAT);
                    syllabusData = new SyllabusData(examName, subjectName, teacherName, publichDate, syllabusLink);
                    syllabusDataArrayList.add(syllabusData);
                }
                syllabusAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Utils.logPrint(getClass(),"response error1", Log.getStackTraceString(e));
                errorDialog.setMessage(getString(R.string.error_occurred));
                errorDialog.show();
            }

        }

        @Override
        public void noDataFound() {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.no_syllabus));
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
        setContentView(R.layout.activity_syllabus);

        init();
        setUpToolbar();
        setUpRecyclerView();
    }

    private void init() {
        context = this;
        loading = new ProgressDialog(context);
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        errorDialog = new AlertDialog.Builder(context)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        errorDialog.dismiss();
                    }
                })
                .create();
    }

    private void setUpToolbar() {
        Toolbar syllabusToolbar;
        syllabusToolbar = findViewById(R.id.syllabusToolbar);
        syllabusToolbar.setTitle(R.string.syllabus);
        syllabusToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        syllabusToolbar.setNavigationIcon(R.drawable.arrow_left);
        syllabusToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setUpRecyclerView(){

        syllabusRecyclerView = findViewById(R.id.syllabus_recyclerView);
        syllabusAdapter = new SyllabusAdapter(syllabusDataArrayList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        syllabusRecyclerView.setLayoutManager(mLayoutManager);
        syllabusRecyclerView.setAdapter(syllabusAdapter);
        loadSyllabus();
    }

    private void loadSyllabus() {
        String institutionId = Utils.getString(context, Constant.KEY_INSTITUTION_ID, Constant.DUMMY);
        String studentId = Utils.getString(context, Constant.KEY_STUDENT_ID, Constant.DUMMY);
        CookiesAdapter cookiesAdapter = new CookiesAdapter(context);
        cookiesAdapter.openReadable();
        String klass = cookiesAdapter.getFromStudent(studentId, CookiesAttribute.student_class);//"1";//Utils.getString(context, Constant.KEY_STUDENT_CLASS, Constant.DUMMY);
        String division = cookiesAdapter.getFromStudent(studentId, CookiesAttribute.student_section);//"A";//Utils.getString(context, Constant.KEY_STUDENT_DIVISION, Constant.DUMMY);
        cookiesAdapter.close();
        String session = Utils.getString(context, Constant.KEY_SESSION, Constant.DUMMY);
        loading.show();
        Perform.syllabusFetch(context, syllabusCallback,institutionId,klass,division,session,
                String.valueOf(Constant.ACADEMICS_SYLLABUS));
    }

}
