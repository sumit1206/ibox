package com.sumit.ibox.services;

import android.os.AsyncTask;
import android.util.Log;

import com.sumit.ibox.IBoxApp;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateHolidayList extends AsyncTask<Void, Void, String> {

    public UpdateHolidayList(){
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            String u = Constant.URL_FETCH_HOLIDAY_LIST;
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
        if(s == null){
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(s);
            int success = jsonObject.getInt("success");
            if(success == 1){
                Utils.saveInCache(Constant.FILE_NAME_HOLIDAY, s);
            }
        } catch (JSONException e) {
            Utils.errorLog(getClass(),"error parsing json",e);
        }
    }
}

