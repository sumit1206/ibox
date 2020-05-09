package com.sumit.ibox.remote;

import com.android.volley.VolleyError;

import org.json.JSONException;

public interface VolleyCallback {
    void onSuccess(Object result);
    void noDataFound();
    void onCatch(JSONException e);
    void onError(VolleyError e);
}