package com.sumit.ibox.ui.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.sumit.ibox.local.CookiesAdapter;
import com.sumit.ibox.local.CookiesAttribute;
import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ChatListData;
import com.sumit.ibox.model.ProgressDialog;
import com.sumit.ibox.model.StudentsData;
import com.sumit.ibox.model.Teacher;
import com.sumit.ibox.model.TeacherAdapter;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;
import com.sumit.ibox.ui.chat.ChatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.sumit.ibox.ui.chat.ChatActivity.TIME_FORMAT;

public class TeacherActivity extends AppCompatActivity {

    Context context;
    ArrayList<Teacher> teachers = new ArrayList<>();
    ProgressDialog loading;
    AlertDialog errorDialog;
    TeacherAdapter teacherAdapter;
    TeacherAdapter.TouchHandler touchHandler = new TeacherAdapter.TouchHandler() {
        @Override
        public void onClick(Teacher teacher) {
            showPopup(teacher);
        }
    };

    private void showPopup(final Teacher teacher) {
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
                        initiateChat(teacher);
                    }
                })
                .create();
        alertDialog.setMessage("Initiate chat with "+teacher.getName()+"?");
        alertDialog.show();
    }

    private VolleyCallback chatInitiateCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            JSONObject jsonObject = (JSONObject) result;
            try {
                String threadId = jsonObject.getString("message_thread_code");
                ChatListData chatListData = new ChatListData(getString(R.string.chat), threadId, "", Constant.TYPE_PARENT, Constant.TYPE_TEACHER);
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

    private void initiateChat(Teacher teacher) {
        loading.show();
        String username = Utils.getString(context, Constant.KEY_USER_ID, Constant.DUMMY);
        String message = "Beginning of new chat with "+teacher.getName();
        long now = System.currentTimeMillis();
        String time = Utils.getTimestampInFormat(now, TIME_FORMAT);
        Perform.initiateChat(context, chatInitiateCallback, Constant.TYPE_PARENT+"", username, Constant.TYPE_TEACHER+"", teacher.getUserName(), message, time);
    }

    VolleyCallback teacherCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            //{"teacher_id":"teacher-1","name":"teacher test","email":"teacher@ibox.in","username":"teacher"}
            loading.dismiss();
            Teacher teacher;
            JSONObject jsonObject = (JSONObject) result;
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("details");
                for(int i = 0; i<jsonArray.length(); i++){
                    JSONObject data = jsonArray.getJSONObject(i);
                    String name = data.getString("name");
                    String mail = data.getString("email");
                    String username = data.getString("username");
                    teacher = new Teacher(username, name, mail, "", "");
                    teachers.add(teacher);
                }
                teacherAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void noDataFound() {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.no_teacher));
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
        setContentView(R.layout.activity_teacher);

        init();
        setupToolbar();
        setupRecyclerView();
        fetchTeachers();
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

    private void setupToolbar() {
        Toolbar teacherToolbar;
        teacherToolbar = findViewById(R.id.teacher_toolbar);
        teacherToolbar.setTitle(R.string.teacher);
        teacherToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        teacherToolbar.setNavigationIcon(R.drawable.arrow_left);
        teacherToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupRecyclerView(){
        RecyclerView teacherRecyclerView;
        teacherRecyclerView = findViewById(R.id.teachers_recyclerView);
        teacherAdapter = new TeacherAdapter(context, teachers, touchHandler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        teacherRecyclerView.setLayoutManager(mLayoutManager);
        teacherRecyclerView.setAdapter(teacherAdapter);
    }

    private void fetchTeachers() {
        String studentId = Utils.getString(context, Constant.KEY_STUDENT_ID, Constant.DUMMY);
        CookiesAdapter cookiesAdapter = new CookiesAdapter(context);
        cookiesAdapter.openReadable();
        String className = cookiesAdapter.getFromStudent(studentId, CookiesAttribute.student_class);
        String sectionName = cookiesAdapter.getFromStudent(studentId, CookiesAttribute.student_section);
        cookiesAdapter.close();
        loading.show();
        Perform.fetchTeachers(context, teacherCallback, className, sectionName);
    }
}
