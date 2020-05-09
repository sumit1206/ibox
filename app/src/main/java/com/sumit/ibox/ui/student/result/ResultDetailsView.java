package com.sumit.ibox.ui.student.result;

import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.ResultSubjectWiseAdapter;
import com.sumit.ibox.local.CookiesAdapter;
import com.sumit.ibox.local.CookiesAttribute;
import com.sumit.ibox.model.ResultSubjectWiseData;
import com.sumit.ibox.model.StudentResultListData;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ResultDetailsView extends AppCompatActivity {

    private RecyclerView detailsResultRecyclerView;
    private List<ResultSubjectWiseData> resultSubjectWiseDataArray;
    TextView tvSchoolName, tvName, tvRoll, tvClass, tvSection, tvGrade;
    ResultSubjectWiseAdapter resultSubjectWiseAdapter;
    Context context;
    ScrollView svResult;
    StudentResultListData resultData;
    ProgressDialog loading;
    AlertDialog errorDialog;
    ImageView cancel;
    VolleyCallback resultDetailCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_result_view);

        init();
        showNameCard();
        setupRecyclerView();
        setupVolleyCallback();
        fetchResult();
//        setup();
    }

    private void init() {
        context = this;
        svResult = findViewById(R.id.result_layout);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        resultData = (StudentResultListData) getIntent().getSerializableExtra(Constant.KEY_RESULT_DATA);
        tvSchoolName = findViewById(R.id.result_school_name);
        tvName = findViewById(R.id.result_student_name);
        tvRoll = findViewById(R.id.result_student_roll);
        tvClass = findViewById(R.id.result_student_class);
        tvSection = findViewById(R.id.result_student_section);
        tvGrade = findViewById(R.id.result_grade);
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

    private void showNameCard() {
        tvSchoolName.setText("Modern Academy");
        String studentId = Utils.getString(context,Constant.KEY_STUDENT_ID,"");
        CookiesAdapter cookiesAdapter = new CookiesAdapter(context);
        cookiesAdapter.openReadable();
        String name = cookiesAdapter.getFromStudent(studentId, CookiesAttribute.student_name);
        String roll = cookiesAdapter.getFromStudent(studentId, CookiesAttribute.student_roll);
        String klass = cookiesAdapter.getFromStudent(studentId, CookiesAttribute.student_class);
        String section = cookiesAdapter.getFromStudent(studentId, CookiesAttribute.student_section);
        cookiesAdapter.close();
        tvName.setText(name);
        tvClass.setText(klass);
        tvSection.setText(section);
        tvRoll.setText(roll);
    }

    private void setupRecyclerView() {
        detailsResultRecyclerView = findViewById(R.id.details_result_recycler_view);
        resultSubjectWiseDataArray = new ArrayList<>();
        resultSubjectWiseAdapter = new ResultSubjectWiseAdapter(this,resultSubjectWiseDataArray);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        detailsResultRecyclerView.setLayoutManager(mLayoutManager);
        detailsResultRecyclerView.setAdapter(resultSubjectWiseAdapter);
    }

    private void setupVolleyCallback() {
        resultDetailCallback = new VolleyCallback() {
            @Override
            public void onSuccess(Object result) {
                loading.dismiss();
                JSONObject jsonObject = (JSONObject) result;
                ResultSubjectWiseData resultDetailData;
                try {//success, details, subject, total, obtain, grade
                    JSONArray jsonArray = jsonObject.getJSONArray("details");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject post = jsonArray.getJSONObject(i);
                        String subject = post.getString("subject");
                        String total = post.getString("total");
                        String obtain = post.getString("obtain");
                        String grade = post.getString("grade");
                        resultDetailData = new ResultSubjectWiseData(subject,total,obtain,grade);
                        resultSubjectWiseDataArray.add(resultDetailData);
                    }
                    resultSubjectWiseAdapter.notifyDataSetChanged();

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

    private void fetchResult(){
        String studentId = Utils.getString(context, Constant.KEY_STUDENT_ID, Constant.DUMMY);
        String session = Utils.getString(context, Constant.KEY_SESSION, Constant.DUMMY);
        loading.show();
        Perform.resultDetailFetch(context, resultDetailCallback, studentId, session, resultData.getExamName());
    }


    public void shareClick(View view) {
        shareScreenshot(svResult);
    }

    public void saveClick(View view) {
        saveScreenshot(svResult);
    }

    public void saveScreenshot(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
//        String extr = Environment.getExternalStorageDirectory().toString();
        String name = "Result_"+System.currentTimeMillis()+".jpg";
//        File myPath = new File(extr, name);
//        FileOutputStream fos = null;

        try {
//            fos = new FileOutputStream(myPath);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,
                    name, "Your students result");
            Utils.showShortToast(context, "saved");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void shareScreenshot(View view){
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        try {
            long now = System.currentTimeMillis();
            File file = new File(this.getExternalCacheDir(),now+".png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share result via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}