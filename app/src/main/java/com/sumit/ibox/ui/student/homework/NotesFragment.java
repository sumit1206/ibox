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
import com.sumit.ibox.controller.NotesAdapter;
import com.sumit.ibox.local.CookiesAdapter;
import com.sumit.ibox.local.CookiesAttribute;
import com.sumit.ibox.model.NotesData;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {

    RecyclerView notesRecyclerView;
    ArrayList <NotesData> notesDataList = new ArrayList<>();
    NotesAdapter notesAdapter;
    Context context;
    ProgressDialog loading;
    AlertDialog errorDialog;

    VolleyCallback notesCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            NotesData notesData;
            JSONObject jsonObject = (JSONObject) result;
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("details");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject post = jsonArray.getJSONObject(i);
                    String examName = post.getString("examName");
                    String subjectName = post.getString("subjectName");
                    String teacherName = post.getString("teacherName");
                    String publichDate = post.getString("publichDate");
                    String syllabusLink = post.getString("syllabusLink");
                    publichDate = Utils.getTimestampInFormat(Long.valueOf(publichDate),Constant.APP_DATE_FORMAT);
                    notesData = new NotesData(subjectName,examName,teacherName,publichDate,syllabusLink);
                    notesDataList.add(notesData);
                }
                notesAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Utils.logPrint(getClass(),"response error1", Log.getStackTraceString(e));
                errorDialog.setMessage(getString(R.string.error_occurred));
                errorDialog.show();
            }

        }

        @Override
        public void noDataFound() {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.no_notes));
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


    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        init();
        setupRecyclerView(view);
        loadSyllabus();
        return view;
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
        notesRecyclerView = inflate.findViewById(R.id.notes_recycler_view);
        notesAdapter = new NotesAdapter(getContext(),notesDataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        notesRecyclerView.setLayoutManager(mLayoutManager);
        notesRecyclerView.setAdapter(notesAdapter);
//        notesData();
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
        Perform.syllabusFetch(context, notesCallback,institutionId,klass,division,session,
                String.valueOf(Constant.ACADEMICS_NOTES));
    }
}
