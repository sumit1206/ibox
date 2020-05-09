package com.sumit.ibox.model;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.sumit.ibox.R;

public class ProgressDialog{

    public static final int STYLE_SPINNER = R.id.progress_bar;

    private View rootView;
    private ProgressBar progressBar;

    public ProgressDialog(Context context) {
        this.rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
    }

    public void setProgressStyle(int id) {
        progressBar = rootView.findViewById(id);
    }

    public void setMessage(String message) {

    }

    public boolean isShowing() {
        return progressBar.getVisibility() == View.VISIBLE;
    }

    public void dismiss() {
        progressBar.setVisibility(View.GONE);
    }

    public void show() {
        progressBar.setVisibility(View.VISIBLE);
    }
}
