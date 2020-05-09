package com.sumit.ibox.ui.articles_for_you;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.ExpandedArticlesAdapter;
import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ArticlesForYouData;
import com.sumit.ibox.model.FeeStructure;
import com.sumit.ibox.model.ProgressDialog;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExpandedArticlesForYou extends AppCompatActivity {

    private Context context;
    private List<ArticlesForYouData> articles = new ArrayList<>();
    private ExpandedArticlesAdapter expandedArticlesAdapter;
    private ProgressDialog loading;
    private AlertDialog errorDialog;

    VolleyCallback articlesCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            try {
                ArticlesForYouData article;
                JSONObject jsonObject = (JSONObject) result;
                int success = jsonObject.getInt("success");
                if(success == 1){//details":[{"title":"School is closed for coronavirus.","image_link":"https:\/\/externas-six.jpg&f=1&nofb=1","date":"03 MAR 2020"}]}
                    JSONArray jsonArray = jsonObject.getJSONArray("details");
                    for(int i = 0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String title = jsonObject1.getString("title");
                        String date = jsonObject1.getString("date");
                        String url = jsonObject1.getString("image_link");
                        article = new ArticlesForYouData(title, getString(R.string.ibox), R.drawable.logo_demo, url, date);
                        articles.add(article);
                    }
                }
                expandedArticlesAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Utils.errorLog(getClass(),"Error parsing json",e);
                errorDialog.setMessage(getString(R.string.error_occurred));
                errorDialog.show();
            }
        }

        @Override
        public void noDataFound() {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.no_articles));
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
        setContentView(R.layout.activity_articles_for_you);

        init();
        setupToolbar();
        setUpRecyclerView();
        fetchArticles();
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

    private void setupToolbar(){
        Toolbar articlesToolbar;
        articlesToolbar  = findViewById(R.id.articles_toolbar);
        articlesToolbar.setTitle(R.string.articles);
        articlesToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        articlesToolbar.setNavigationIcon(R.drawable.arrow_left);
        articlesToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setUpRecyclerView(){
        RecyclerView expanded_articles_recycler_view;
        expanded_articles_recycler_view = findViewById(R.id.expanded_articles_recycler_view);
        articles = new ArrayList<>();
        expandedArticlesAdapter = new ExpandedArticlesAdapter(context, articles);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        expanded_articles_recycler_view.setLayoutManager(mLayoutManager);
        expanded_articles_recycler_view.setAdapter(expandedArticlesAdapter);
    }

    private void fetchArticles() {
        loading.show();
        Perform.fetchArticles(context, articlesCallback);
    }
}
