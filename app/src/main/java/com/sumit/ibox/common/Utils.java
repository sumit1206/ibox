package com.sumit.ibox.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sumit.ibox.IBoxApp;
import com.sumit.ibox.R;
import com.sumit.ibox.model.StudentRoutineData;
import com.sumit.ibox.ui.LoginActivity;
import com.sumit.ibox.local.CookiesAdapter;
import com.sumit.ibox.local.CookiesAttribute;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import static com.sumit.ibox.IBoxApp.STORAGE_DIR;
import static com.sumit.ibox.common.RoutineConstant.DAY_FRIDAY;
import static com.sumit.ibox.common.RoutineConstant.DAY_MONDAY;
import static com.sumit.ibox.common.RoutineConstant.DAY_SATURDAY;
import static com.sumit.ibox.common.RoutineConstant.DAY_SUNDAY;
import static com.sumit.ibox.common.RoutineConstant.DAY_THURSDAY;
import static com.sumit.ibox.common.RoutineConstant.DAY_TUESDAY;
import static com.sumit.ibox.common.RoutineConstant.DAY_WEDNESDAY;
import static com.sumit.ibox.common.RoutineConstant.INDEX_CLASS;
import static com.sumit.ibox.common.RoutineConstant.INDEX_DAY;
import static com.sumit.ibox.common.RoutineConstant.INDEX_SECTION;
import static com.sumit.ibox.common.RoutineConstant.INDEX_SUBJECT;
import static com.sumit.ibox.common.RoutineConstant.INDEX_TEACHER;
import static com.sumit.ibox.common.RoutineConstant.INDEX_TIME;

public class Utils {

//    static class Log{
//        public static int ASSERT = 1;
//        public static void println(int type, String tag, String message){
//
//        }
//    }

    public static void showLongToast(Context c, String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(Context c, String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    public static void logPrint(Class klass, String tag, String message){
        Log.println(Log.ASSERT,klass.getName(),tag+": "+message);
    }

    public static void errorLog(Class klass, String tag, Exception e){
        Log.println(Log.ASSERT,klass.getName(),tag+": "+Log.getStackTraceString(e));
    }

    public static void saveBoolean(Context mContext, String name, boolean value) {

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static Boolean getBoolean(Context mContext, String name, boolean defaultvalue) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        return pref.getBoolean(name, defaultvalue);
    }

    public static void saveInt(Context mContext, String name, int value) {

        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public static int getInt(Context mContext, String name, int defaultvalue) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        return pref.getInt(name, defaultvalue);
    }

    public static void saveString(Context mContext, String name, String value) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static void removeString(Context mContext, String name) {
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(name);
        editor.commit();
    }

    public static String getString(Context mContext, String name, String defaultvalue) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String val = pref.getString(name, defaultvalue);
        //Log.d(TAG, "val = " + val);
        return val;
    }

    public static String getTimestampInFormat(long millisecond, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);//,Locale.US
        return formatter.format(new Date(millisecond)).toString();
    }

    public static String getDateInFormat(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);//,Locale.US
        return formatter.format(date);
    }

    public static void setupToolbar(Context context, View view, int id, int stringId, int colourId){

    }

    public static void logout(Context context, Activity activity) {
        activity.finish();
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        CookiesAdapter cookiesAdapter = new CookiesAdapter(context);
        cookiesAdapter.openWritable();
        cookiesAdapter.delete(CookiesAttribute.TABLE_STUDENT);
        cookiesAdapter.delete(CookiesAttribute.TABLE_TEACHER);
        cookiesAdapter.close();
        context.startActivity(new Intent(context, LoginActivity.class));
        String ss = getString(context, Constant.KEY_USER_ID, "deleted");
        logPrint(Utils.class,"user",ss);
    }

    public static String getMonthFromNo(int i) {
        String m = "";
        switch (i){
            case 1:
                m = "Jan";
                break;
            case 2:
                m = "Feb";
                break;
            case 3:
                m = "Mar";
                break;
            case 4:
                m = "Apr";
                break;
            case 5:
                m = "May";
                break;
            case 6:
                m = "Jun";
                break;
            case 7:
                m = "Jul";
                break;
            case 8:
                m = "Aug";
                break;
            case 9:
                m = "Sep";
                break;
            case 10:
                m = "Oct";
                break;
            case 11:
                m = "Nov";
                break;
            case 12:
                m = "Dec";
                break;
            default:
                break;
        }
        return m;
    }

    public static boolean saveInCache(String fileName, String data) {
        String path = IBoxApp.STORAGE_DIR + "";
//        String fileName = Constant.FILE_NAME_CLASS_SECTION;
        File dir = new File(path, Constant.SAVE_PATH_CACHE_DUMP);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(path +"/"+ Constant.SAVE_PATH_CACHE_DUMP+"/"+ fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }else {
                file.delete();
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

            outputStreamWriter.write(data);

            fileOutputStream.flush();
            outputStreamWriter.flush();

            outputStreamWriter.close();
            fileOutputStream.close();

            return true;
        } catch (Exception e) {
            Utils.logPrint(Utils.class,"Error creating "+fileName, Log.getStackTraceString(e));
        }
        return false;
    }

    public static String readFromCache(String fileName) throws IOException {
        StudentRoutineData subjectData;
        String path = STORAGE_DIR + "";
        File file = new File(path +"/"+ Constant.SAVE_PATH_CACHE_DUMP+"/"+ fileName);
        try {
            Scanner scanner = new Scanner(file);
            String data = scanner.nextLine();
            scanner.close();
            return data;
        }catch (Exception e){
            Utils.logPrint(Utils.class,"Error reading "+fileName, Log.getStackTraceString(e));
        }
        return null;
    }

    public static String makeCommaSeparated(ArrayList<String> list){
        String data = list.toString().replace('[',' ').replace(']',' ').replaceAll(" ", "");
        return data;
    }

    public static void setHtmlText(TextView textView, String text){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(text));
        }
    }

    public static double getLatency(){
        String ipAddress = "8.8.8.8";
        String pingCommand = "/system/bin/ping -c 1 "+ ipAddress;
        String inputLine;
        double avgRtt = 0.0;

        try {
            // execute the command on the environment interface
            Process process = Runtime.getRuntime().exec(pingCommand);
            // gets the input stream to get the output of the executed command
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            inputLine = bufferedReader.readLine();
            while ((inputLine != null)) {
                Utils.logPrint(Utils.class,"inputline",inputLine);
                if (inputLine.length() > 0 && inputLine.contains("time=")) {  // when we get to the last line of executed ping command
                    String ping = inputLine.substring(inputLine.lastIndexOf('=')+1,inputLine.lastIndexOf(' '));
                    Utils.logPrint(Utils.class,"ping",ping);
                    avgRtt = Double.parseDouble(ping);
                    break;
                }
                inputLine = bufferedReader.readLine();
            }
        } catch (IOException e){
            Utils.logPrint(Utils.class,"error ping", Log.getStackTraceString(e));
        }
        return avgRtt;
    }

    public static int getIconId(int type) {
        return R.drawable.avater;
    }
}

