package com.sumit.ibox.ui.faq;

import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.FaqAdapter;
import com.sumit.ibox.model.FaqData;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FaqActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView faqRecyclerView;
    private List<FaqData> faqDataArrayList = new ArrayList<>();
    private FaqAdapter faqAdapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressDialog loading;
    AlertDialog errorDialog;

    TextView faq_question_text;
    VolleyCallback faqCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            try {
                FaqData faqData;
                JSONObject jsonObject = (JSONObject) result;
                int success = jsonObject.getInt("success");
                if(success == 1){
                    JSONArray jsonArray = jsonObject.getJSONArray("details");
                    for(int i = 0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                       success, question, answer, asked_by
                        String question = jsonObject1.getString("question");
                        String answer = jsonObject1.getString("answer");
                        String name = jsonObject1.getString("asked_by");
                        faqData = new FaqData(question, answer, name, "0", "0");
                        faqDataArrayList.add(faqData);
                    }
                }
                faqAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Utils.errorLog(getClass(),"Error parsing json",e);
                errorDialog.setMessage(getString(R.string.error_occurred));
                errorDialog.show();
            }
        }

        @Override
        public void noDataFound() {
            loading.dismiss();
//            errorDialog.setMessage(getString(R.string.no_fees));
//            errorDialog.show();
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
        setContentView(R.layout.activity_faq);

        init();
        setupToolbar();
        setupRecyclerView();
        fetchFaqData();
        faq_question_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent faqIntent = new Intent (FaqActivity.this,AskQuestionFaq.class);
                startActivity(faqIntent);
            }
        });

    }

    private void init() {
        context = this;
        faq_question_text  = findViewById(R.id.have_question_btn);
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
        final Toolbar toolbar = (Toolbar) findViewById(R.id.faq_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("FAQ");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView() {
        faqRecyclerView = findViewById(R.id.faq_recycler_view);
        faqRecyclerView.setHasFixedSize(true);
        faqAdapter = new FaqAdapter(FaqActivity.this,faqDataArrayList);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        faqRecyclerView.setLayoutManager(layoutManager);
        faqRecyclerView.setAdapter(faqAdapter);
        faqRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void fetchFaqData() {
        loading.show();
        Perform.faqFetch(context, faqCallback);
    }
}
