package com.sumit.ibox.ui;

import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.local.CookiesAdapter;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;
import com.sumit.ibox.ui.student.*;
import com.sumit.ibox.ui.teacher.*;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    /**
     * 2 PARENT
     * 3 TEACHER
     * 4 BOTH
     * */

    Context context;
    EditText etUsername;
    EditText etPassword;
    String username, password;
    Intent mainIntent;
    ProgressDialog loading;
    AlertDialog errorDialog;

    VolleyCallback loginCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            JSONObject jsonObject = (JSONObject) result;
            try {//success(type), student_list
                int type = jsonObject.getInt("success");
                if(type == Constant.TYPE_PARENT || type == Constant.TYPE_BOTH) {
                    String[] ids = jsonObject.getString("student_list").split(Constant.DELIMETER);
                    if(ids.length == 0){
                        errorDialog.setMessage(getString(R.string.no_student_here));
                        errorDialog.show();
                        return;
                    }
                    CookiesAdapter cookies = new CookiesAdapter(context);
                    cookies.openWritable();
                    for(String id : ids){
                        cookies.addStudentData(id,null,null,null,null);
                        Utils.saveString(context, Constant.KEY_STUDENT_ID, id);
                    }
                    cookies.close();
                    mainIntent = new Intent(context, StudentMain.class);
                }else if(type == Constant.TYPE_TEACHER){
                    mainIntent = new Intent(context, TeacherMain.class);
                }
                Utils.saveInt(context, Constant.KEY_USER_TYPE, type);
                Utils.saveBoolean(context, Constant.KEY_LOGGED_IN, true);
                Utils.saveString(context, Constant.KEY_USER_ID, username);
                startActivity(mainIntent);
            } catch (Exception e) {
                Utils.logPrint(this.getClass(), "onSuccess Catch", Log.getStackTraceString(e));
                errorDialog.setMessage(getString(R.string.error_occurred));
                errorDialog.show();
            }
        }

        @Override
        public void noDataFound() {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.login_error));
            errorDialog.show();
        }

        @Override
        public void onCatch(JSONException e) {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.error_occurred));
            errorDialog.show();
            Utils.logPrint(this.getClass(), "onCatch", Log.getStackTraceString(e));
        }

        @Override
        public void onError(VolleyError e) {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.error_occurred));
            errorDialog.show();
            Utils.logPrint(this.getClass(), "onError", Log.getStackTraceString(e));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        Intent intent = getIntent();
        if(intent.hasExtra(Constant.KEY_ACTIVITY_TO_LOAD)){
            int type = intent.getIntExtra(Constant.KEY_ACTIVITY_TO_LOAD,99);
            if(type == Constant.TYPE_PARENT){
                mainIntent = new Intent(context, StudentMain.class);
                startActivity(mainIntent);
            }else if(type == Constant.TYPE_TEACHER){
                mainIntent = new Intent(context, TeacherMain.class);
                startActivity(mainIntent);
            }else {
                //do nothing
            }
        }
    }

    private void init() {
        context = this;
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
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

    public void loginPressed(View view) {
        username = etUsername.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if(username.length() == 0){
            etUsername.setError(getString(R.string.empty_error));
            return;
        }else if(password.length() == 0){
            etPassword.setError(getString(R.string.empty_error));
            return;
        }
        loading.show();
        Perform.validateLogin(context, loginCallback, username, password);
    }
}
