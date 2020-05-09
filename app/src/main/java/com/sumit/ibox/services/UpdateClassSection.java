package com.sumit.ibox.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.sumit.ibox.IBoxApp;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class UpdateClassSection extends AsyncTask<Void, Void, String> {

    private int version;

    public UpdateClassSection() {
        this.version = Utils.getInt(IBoxApp.context, Constant.KEY_CLASS_SECTION_VERSION,0);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        updateClassSectionList();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            String u = Constant.URL_FETCH_CLASS_SECTION+"?v="+version;
            URL url = new URL(u);

            Log.println(Log.ASSERT,"hitting",url.toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(Constant.SERVER_TIMEOUT);

            Log.println(Log.ASSERT,"getResponseMessage",connection.getResponseMessage());
            Log.println(Log.ASSERT,"getResponseCode",connection.getResponseCode()+"");

            int rCode = connection.getResponseCode();
            if(rCode != 200){
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = in.readLine();

            Log.println(Log.ASSERT, "ApiResponse",result+"");

            in.close();
            connection.disconnect();
            return result;

        } catch (Exception e) {
            Log.println(Log.ASSERT,"error",Log.getStackTraceString(e));
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s == null) {
//            return;
        }else {
            try {
                JSONObject jsonObject = new JSONObject(s);
                int success = jsonObject.getInt("success");
                if (success == 0) {
//                    return;
                }else if (success == 1) {
                    boolean savedSuccessfully = Utils.saveInCache(Constant.FILE_NAME_CLASS_SECTION, s);
                    if (savedSuccessfully) {
                        int newVersion = jsonObject.getInt("version");
                        Utils.saveInt(IBoxApp.context, Constant.KEY_CLASS_SECTION_VERSION, newVersion);
                    }
                }
            } catch (JSONException e) {
                Utils.errorLog(getClass(),"error parsing json",e);
            }
        }
        updateClassSectionList();
    }

    private void updateClassSectionList() {
        String response = null;
        try {//success, version, class_list, section_list
            response = Utils.readFromCache(Constant.FILE_NAME_CLASS_SECTION);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray classArray = jsonObject.getJSONArray("class_list");
            JSONArray sectionArray = jsonObject.getJSONArray("section_list");
            ArrayList<String> classes = new ArrayList<>();
            ArrayList<String> sections = new ArrayList<>();
            int i;
            for (i = 0; i < classArray.length(); i++){
                String klass = classArray.getString(i);
                classes.add(klass);
            }
            for (i = 0; i < sectionArray.length(); i++){
                String section = sectionArray.getString(i);
                sections.add(section);
            }
            Constant.classList = classes.toArray(new String[0]);
            Constant.sectionList = sections.toArray(new String[0]);
        } catch (Exception e) {
            Utils.errorLog(getClass(),"reading class section",e);
        }
    }
}