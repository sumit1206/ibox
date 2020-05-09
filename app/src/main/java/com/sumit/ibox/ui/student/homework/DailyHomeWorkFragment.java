package com.sumit.ibox.ui.student.homework;


import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.StudentsHomeWorkListViewAdapter;
import com.sumit.ibox.local.CookiesAdapter;
import com.sumit.ibox.local.CookiesAttribute;
import com.sumit.ibox.model.StudentHomeWorkListViewData;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyHomeWorkFragment extends Fragment {


    private RecyclerView dailyHomeWorkRecyclerView;
    private List<StudentHomeWorkListViewData> studentHomeWorkListArray;
    StudentsHomeWorkListViewAdapter studentsHomeWorkListViewAdapter;

    Context context;
    ProgressDialog loading;
    AlertDialog errorDialog;
    VolleyCallback homeworkCallback;

    public DailyHomeWorkFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_daily_home_work, container, false);

        init();
        setupRecyclerView(inflate);
        setupVolleyCallback();
        fetchHomework();
        return inflate;
    }

    private void init() {
        context = getContext();
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

    private void setupRecyclerView(View inflate) {
        dailyHomeWorkRecyclerView = inflate.findViewById(R.id.home_work_recycler_view_daily);
        studentHomeWorkListArray = new ArrayList<>();
        studentsHomeWorkListViewAdapter = new StudentsHomeWorkListViewAdapter(getContext(),studentHomeWorkListArray);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        dailyHomeWorkRecyclerView.setLayoutManager(mLayoutManager);
        dailyHomeWorkRecyclerView.setAdapter(studentsHomeWorkListViewAdapter);
    }

    private void setupVolleyCallback(){
        homeworkCallback = new VolleyCallback() {
            @Override
            public void onSuccess(Object result) {
                loading.dismiss();
                StudentHomeWorkListViewData homeworkData;
                JSONObject jsonObject = (JSONObject) result;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("details");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject post = jsonArray.getJSONObject(i);
                        String teacherId = post.getString("name");
                        String subject = post.getString("subject_name");
                        String title = post.getString("title");
                        String description = post.getString("description");
                        String hwDate = post.getString("upload_date");
//                        hwDate = Utils.getTimestampInFormat(Long.valueOf(hwDate),"dd MMM,yy");
                        String submissionDate = post.getString("date_end");
                        String imageLink = post.getString("file_name");
                        homeworkData = new StudentHomeWorkListViewData(teacherId, subject, title, description, hwDate,
                                submissionDate,imageLink);
                        studentHomeWorkListArray.add(homeworkData);
                    }
                    studentsHomeWorkListViewAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Utils.logPrint(getClass(),"response error1", Log.getStackTraceString(e));
                    errorDialog.setMessage(getString(R.string.error_occurred));
                    errorDialog.show();
                }

            }

            @Override
            public void noDataFound() {
                loading.dismiss();
                errorDialog.setMessage(getString(R.string.no_homework));
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

    private void fetchHomework(){
//        String institutionId = Utils.getString(context, Constant.KEY_INSTITUTION_ID, Constant.DUMMY);
        String studentId = Utils.getString(context, Constant.KEY_STUDENT_ID, Constant.DUMMY);
        Utils.logPrint(getClass(),"studentId",studentId);
        CookiesAdapter cookiesAdapter = new CookiesAdapter(context);
        cookiesAdapter.openReadable();
        String klass = cookiesAdapter.getFromStudent(studentId, CookiesAttribute.student_class);
        String division = cookiesAdapter.getFromStudent(studentId, CookiesAttribute.student_section);
        cookiesAdapter.close();
        loading.show();
        Perform.homeworkFetch(context, homeworkCallback,klass,division);
    }
}
