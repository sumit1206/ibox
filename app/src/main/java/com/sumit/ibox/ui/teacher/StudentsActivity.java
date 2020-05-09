package com.sumit.ibox.ui.teacher;

import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ChatListData;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.StudentAdapter;
import com.sumit.ibox.model.StudentsData;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;
import com.sumit.ibox.ui.chat.ChatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.sumit.ibox.ui.chat.ChatActivity.TIME_FORMAT;

public class StudentsActivity extends AppCompatActivity {

    Context context;
    private RecyclerView studentsRecyclerView;
    private List<StudentsData> studentsDataArray;
    StudentAdapter studentAdapter;
    String classString, divisionString;
    Toolbar toolbar;
    Spinner klass, section;
    ProgressDialog loading;
    AlertDialog errorDialog;
    VolleyCallback studentSearchCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            JSONObject jsonObject = (JSONObject) result;
            StudentsData studentData;
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("details");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject post = jsonArray.getJSONObject(i);
//                    "name":"Raghvendra Singh","class":"Class 1","section":"A","roll":"111","id":"1","parent_user_name":"Raghvendra Singh"
                    String studentName = post.getString("name");
                    String studentClass = post.getString("class");
                    String studentSection = post.getString("section");
                    String studentRoll = post.getString("roll");
                    String studentId = post.getString("id");
                    String parentId = post.getString("parent_user_name");
                    studentData = new StudentsData(studentName, studentClass, studentSection, studentRoll, parentId, studentId);
                    studentsDataArray.add(studentData);
                }
                studentAdapter.notifyDataSetChanged();
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

    private VolleyCallback chatInitiateCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            JSONObject jsonObject = (JSONObject) result;
            try {
                String threadId = jsonObject.getString("message_thread_code");
                ChatListData chatListData = new ChatListData(getString(R.string.chat), threadId, "", Constant.TYPE_TEACHER, Constant.TYPE_PARENT);
                Intent chatIntent = new Intent(context, ChatActivity.class);
                chatIntent.putExtra(Constant.KEY_EXTRA_CHAT_DATA, chatListData);
                startActivity(chatIntent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void noDataFound() {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.not_sent));
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

    StudentAdapter.TouchHandler touchHandler = new StudentAdapter.TouchHandler() {
        @Override
        public void onClick(StudentsData studentsData) {
            showPopup(studentsData);
        }
    };

    private void showPopup(final StudentsData studentsData) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        errorDialog.cancel();
                    }
                })
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initiateChat(studentsData);
                    }
                })
                .create();
        alertDialog.setMessage("Initiate chat with "+studentsData.getStudentsName()+"'s parent?");
        alertDialog.show();
    }

    private void initiateChat(StudentsData studentsData) {
        loading.show();
        String username = Utils.getString(context, Constant.KEY_USER_ID, Constant.DUMMY);
        String message = "Beginning of new chat with "+studentsData.getStudentsName()+"s parent";
        long now = System.currentTimeMillis();
        String time = Utils.getTimestampInFormat(now, TIME_FORMAT);
        Perform.initiateChat(context, chatInitiateCallback, Constant.TYPE_TEACHER+"", username, Constant.TYPE_PARENT+"", studentsData.getParentId(), message, time);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        init();
        setupRecyclerView();
        setupToolbar();
        setupSpinner();
    }

    private void init() {
        context = this;
        klass = findViewById(R.id.student_class_spinner);
        section = findViewById(R.id.student_section_spinner);
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

    private void setupToolbar(){
        toolbar = findViewById(R.id.students_toolbar);
        toolbar.setTitle(R.string.students);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupSpinner() {
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, Constant.classList);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        klass.setAdapter(classAdapter);
        ArrayAdapter<String> divisionAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, Constant.sectionList);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        section.setAdapter(divisionAdapter);
    }

    private void setupRecyclerView(){
        studentsRecyclerView = findViewById(R.id.students_recyclerView);
        studentsDataArray = new ArrayList<>();
        studentAdapter = new StudentAdapter(context, studentsDataArray, touchHandler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        studentsRecyclerView.setLayoutManager(mLayoutManager);
        studentsRecyclerView.setAdapter(studentAdapter);
    }

    public void searchPressed(View view) {
        studentsDataArray.clear();
        studentAdapter.notifyDataSetChanged();
        classString = klass.getSelectedItem().toString();
        divisionString = section.getSelectedItem().toString();
        String institutionId = Utils.getString(context, Constant.KEY_INSTITUTION_ID, Constant.DUMMY);
        String session = Utils.getString(context, Constant.KEY_SESSION, Constant.DUMMY);
        loading.show();
        Perform.searchStudentList(context, studentSearchCallback, institutionId, classString, divisionString, session);
    }
}
