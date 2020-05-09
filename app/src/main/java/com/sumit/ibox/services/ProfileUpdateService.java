package com.sumit.ibox.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.local.CookiesAdapter;
import com.sumit.ibox.local.CookiesAttribute;
import com.sumit.ibox.model.Student;
import com.sumit.ibox.model.Teacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileUpdateService extends Service {
    public ProfileUpdateService() {
    }

    Context context;
    private static int totalServices = 0;
    private static int studentsToUpdate = 0;
    private static int studentsUpdated = 0;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utils.logPrint(getClass(),"ProfileUpdateService","onStartCommand");
        init();
        int type = Utils.getInt(context, Constant.KEY_USER_TYPE, 99);
        Utils.logPrint(getClass(),"type", String.valueOf(type));
        if(type == 99){
            return super.onStartCommand(intent, flags, startId);
        }
        if(type == Constant.TYPE_PARENT || type == Constant.TYPE_BOTH){
            accessStudentProfile();
        }
        if(type == Constant.TYPE_TEACHER || type == Constant.TYPE_BOTH){
            accessTeacherProfile();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void accessStudentProfile() {
        Utils.logPrint(getClass(),"accessStudentProfile","executed");
        CookiesAdapter cookiesAdapter = new CookiesAdapter(context);
        cookiesAdapter.openReadable();
        ArrayList<Student> students = cookiesAdapter.getStudentList();
        cookiesAdapter.close();
        if(students == null){
            Utils.logPrint(getClass(),"students","null");
            return;
        }
        Utils.logPrint(getClass(),"students",students.toString());
        for(Student student : students){
            if(student.getKlass().equals("null") || student.getName().equals("null") || student.getRoll().equals("null") || student.getSection().equals("null")){
                studentsToUpdate++;
                Utils.logPrint(getClass(),"student",student.getId());
                Map<String, String> params = new HashMap<>();
                params.put("student_id", student.getId());
                new UpdateStudent(params).execute();
            }
        }
    }

    private void accessTeacherProfile() {
        Utils.logPrint(getClass(),"accessTeacherProfile","executed");
        CookiesAdapter cookiesAdapter = new CookiesAdapter(context);
        cookiesAdapter.openReadable();
        Teacher teacher = cookiesAdapter.getTeacher();
        cookiesAdapter.close();
        if(teacher == null){
            Utils.logPrint(getClass(),"teacher","null");
            String userName = Utils.getString(context, Constant.KEY_USER_ID,null);
            Map<String, String> params = new HashMap<>();
            params.put("username", userName);
            new UpdateTeacher(params).execute();
            return;
        }
        if(teacher.getEmail() == null || teacher.getName() == null || teacher.getPhone() == null){
            Map<String, String> params = new HashMap<>();
            params.put("username", teacher.getUserName());
            new UpdateTeacher(params).execute();
        }
    }

    private void init() {
        context = this;
    }

    public class UpdateStudent extends AsyncTask<Void, Void, String> {

        Map<String, String> paramss;

        public UpdateStudent(Map<String, String> paramss) {
            this.paramss = paramss;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            String upLoadServerUri = Constant.URL_UPDATE_STUDENT_PROFILE;
            int serverResponseCode = 0;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "apiclient-" + System.currentTimeMillis();// = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 10 * 1024 * 1024;
            try {
                // open a URL connection to the Servlet
                Utils.logPrint(getClass(), "hitting", upLoadServerUri);
                Utils.logPrint(getClass(), "sending", paramss.toString());
                URL url = new URL(upLoadServerUri);
                conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                //conn.setRequestProperty("pid", "4");
                dos = new DataOutputStream(conn.getOutputStream());

                for (Map.Entry<String, String> param : paramss.entrySet()) {
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + param.getKey() + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(param.getValue());
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = bufferedReader.readLine();

                Utils.logPrint(this.getClass(), "httpResponse", String.valueOf(serverResponseCode) + serverResponseMessage);
                Utils.logPrint(this.getClass(), "apiResponse", response);
                if (serverResponseCode != 200) {
                    return null;
                }
                //close the streams //
                dos.flush();
                dos.close();

            } catch (Exception ex) {
                Log.println(Log.ASSERT, "uploading error", String.valueOf(serverResponseCode) + Log.getStackTraceString(ex));
                return null;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            studentsUpdated++;
            if (response != null) {
                try {
                    Utils.logPrint(getClass(), "student response", response);
//                    {"success":1,"message":"success","details":[{"first_name":"ROHAN","last_name":"SAHA","class_id":"1","section_id":"1","roll":"11"}]}
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject post = jsonArray.getJSONObject(i);
                            String first_name = post.getString("first_name");
                            String last_name = post.getString("last_name");
                            String class_id = post.getString("class_name");
                            String section_id = post.getString("section_name");
                            String roll = post.getString("roll");
                            Student student = new Student(paramss.get("student_id"),first_name+" "+last_name,roll,class_id,section_id,null);
                            addStudent(student);
                        }
                    }
                } catch (Exception e) {
                    Utils.logPrint(getClass(), "error fetching data", Log.getStackTraceString(e));
                }

            }
            if(studentsUpdated == studentsToUpdate){
                sendBroadcast(Constant.ACTION_STUDENT_UPDATED,Constant.ACTION_STUDENT_UPDATED,Constant.ACTION_STUDENT_UPDATED);
            }
        }

        private void addStudent(Student student) {
            CookiesAdapter cookiesAdapter = new CookiesAdapter(context);
            cookiesAdapter.openWritable();
            cookiesAdapter.updateStudent(CookiesAttribute.student_name,student.getName(),student.getId());
            cookiesAdapter.updateStudent(CookiesAttribute.student_roll,student.getRoll(),student.getId());
            cookiesAdapter.updateStudent(CookiesAttribute.student_class,student.getKlass(),student.getId());
            cookiesAdapter.updateStudent(CookiesAttribute.student_section,student.getSection(),student.getId());
            cookiesAdapter.close();
        }
    }


    public class UpdateTeacher extends AsyncTask<Void, Void, String> {
        Map<String, String> paramss;

        public UpdateTeacher(Map<String, String> params) {
            this.paramss = params;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            String upLoadServerUri = Constant.URL_UPDATE_TEACHER_PROFILE;
            int serverResponseCode = 0;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "apiclient-" + System.currentTimeMillis();// = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 10 * 1024 * 1024;
            try {
                // open a URL connection to the Servlet
                Utils.logPrint(getClass(), "hitting", upLoadServerUri);
                Utils.logPrint(getClass(), "sending", paramss.toString());
                URL url = new URL(upLoadServerUri);
                conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                //conn.setRequestProperty("pid", "4");
                dos = new DataOutputStream(conn.getOutputStream());

                for (Map.Entry<String, String> param : paramss.entrySet()) {
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + param.getKey() + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(param.getValue());
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                response = bufferedReader.readLine();

                Utils.logPrint(this.getClass(), "httpResponse", String.valueOf(serverResponseCode) + serverResponseMessage);
                Utils.logPrint(this.getClass(), "apiResponse", response);
                if (serverResponseCode != 200) {
                    return null;
                }
                //close the streams //
                dos.flush();
                dos.close();

            } catch (Exception ex) {
                Log.println(Log.ASSERT, "uploading error", String.valueOf(serverResponseCode) + Log.getStackTraceString(ex));
                return null;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject post = jsonArray.getJSONObject(i);
                            String first_name = post.getString("first_name");
                            String last_name = post.getString("last_name");
                            String email = post.getString("email");
                            String phone = post.getString("phone");

                            Teacher teacher = new Teacher(null,first_name+" "+last_name,email,phone,null);
                            addTeacher(teacher);
                        }
                    }
                } catch (Exception e) {
                    Utils.logPrint(getClass(), "error fetching data", Log.getStackTraceString(e));
                }

            }
            sendBroadcast(Constant.ACTION_TEACHER_UPDATED,Constant.ACTION_TEACHER_UPDATED,Constant.ACTION_TEACHER_UPDATED);
        }

        private void addTeacher(Teacher teacher) {
            CookiesAdapter cookiesAdapter = new CookiesAdapter(context);
            cookiesAdapter.openWritable();
            cookiesAdapter.addTeacherData(teacher.getName(),teacher.getEmail(),teacher.getPhone());
        }
    }

        void sendBroadcast(String action, String key, String message){
        Intent intent = new Intent(action);    //action: "msg"
        intent.setPackage(getPackageName());
        intent.putExtra(key, message);
        getApplicationContext().sendBroadcast(intent);
        Utils.logPrint(getClass(),key, message);
    }
}
