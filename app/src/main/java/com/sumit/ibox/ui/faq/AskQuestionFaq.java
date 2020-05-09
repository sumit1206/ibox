package com.sumit.ibox.ui.faq;

import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONException;

public class AskQuestionFaq extends AppCompatActivity {

    private Context context;
    private EditText etQuestion;
    ProgressDialog loading;
    AlertDialog errorDialog;

    VolleyCallback questionCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.uploded));
            errorDialog.show();
        }

        @Override
        public void noDataFound() {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.upload_fail));
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
        setContentView(R.layout.activity_ask_question_faq);

        init();
        setupToolbar();

    }

    private void init() {
        context = this;
        etQuestion = findViewById(R.id.ask_question_question);
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
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ask_question);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Post Your Question");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void submitClicked(View view) {
        String question = etQuestion.getText().toString().trim();
        if(question.equals("")){
            etQuestion.setError("Question needed");
            return;
        }
        String username = Utils.getString(context, Constant.KEY_USER_ID, Constant.DUMMY);
        loading.show();
        Perform.uploadQuestion(context, questionCallback, username, question);
    }
}
