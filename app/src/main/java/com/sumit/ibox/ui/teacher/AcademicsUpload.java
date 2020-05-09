package com.sumit.ibox.ui.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.sumit.ibox.model.AlertDialog;
import android.app.DatePickerDialog;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AcademicsUpload extends AppCompatActivity {

    Context context;
    Toolbar homeWorkToolbarUpload;
    Spinner type;
    LinearLayout homeWorkLayout, syllabusLayout, notesLayout;

    EditText title, description;
    Spinner klass, division;
    LinearLayout lastDateLayout, addImageLayout;
    TextView lastDate, lastMonthYear;
    ImageView image;
    DatePickerDialog datePickerDialog;

    EditText etExamName, etSubject;
    Spinner syllabusClass, syllabusDiv;
    LinearLayout addSyllabus;
    TextView tvSyllabusFileName;
    static File syllabus = null;
    static File homework = null;

    static int academicsType = Constant.ACADEMICS_HOMEWORK;

    String selectedDate;
    AlertDialog errorDialog, responseDialog;
    ProgressDialog loading;
    String institutionId;
    String teacherId;
    String session;
    String klassString;
    String divisionString;
    String subject;
    String titleString;
    String descriptionString;
    String homeworkDateString;
    String submissionDateString;
    Bitmap imageBitmap;

    String examName;
    RequestQueue rQueue;

    AdapterView.OnItemSelectedListener typeSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            academicsType = position;
            if(position == Constant.ACADEMICS_HOMEWORK || position == Constant.ACADEMICS_ASSIGNMENT){
                homeWorkLayout.setVisibility(View.VISIBLE);
                syllabusLayout.setVisibility(View.GONE);
                notesLayout.setVisibility(View.GONE);
            }else if(position == Constant.ACADEMICS_SYLLABUS || position == Constant.ACADEMICS_NOTES){
                homeWorkLayout.setVisibility(View.GONE);
                syllabusLayout.setVisibility(View.VISIBLE);
                notesLayout.setVisibility(View.GONE);
            }
//            else if(position == Constant.ACADEMICS_NOTES){
//                homeWorkLayout.setVisibility(View.GONE);
//                syllabusLayout.setVisibility(View.GONE);
//                notesLayout.setVisibility(View.VISIBLE);
//            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_upload);

        init();
        setupSpinner();
        setupDatePicker();

        homeWorkLayout.setVisibility(View.VISIBLE);
        type.setOnItemSelectedListener(typeSelectListener);

        //homework
        lastDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        addImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        //syllabus
        addSyllabus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void init() {
        context = this;
        type = findViewById(R.id.academics_type);
        homeWorkLayout = findViewById(R.id.academics_homework_layout);
        syllabusLayout = findViewById(R.id.academics_syllabus_layout);
        notesLayout = findViewById(R.id.academics_notes_layout);
       /**toolbar setup*/
        homeWorkToolbarUpload = findViewById(R.id.upload_homework_toolbar);
        homeWorkToolbarUpload.setTitle(R.string.upload_homework_upload);
        homeWorkToolbarUpload.setTitleTextColor(getResources().getColor(R.color.white));
        homeWorkToolbarUpload.setNavigationIcon(R.drawable.arrow_left);
        homeWorkToolbarUpload.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        title = findViewById(R.id.homework_title);
        description = findViewById(R.id.homework_description);
        klass = findViewById(R.id.homework_class);
        division = findViewById(R.id.homework_division);
        lastDateLayout = findViewById(R.id.homework_last_date_layout);
        addImageLayout = findViewById(R.id.homework_image_layout);
        lastDate = findViewById(R.id.homework_last_date);
        lastMonthYear = findViewById(R.id.homework_last_month_year);
        image = findViewById(R.id.homework_image);

        etExamName = findViewById(R.id.syllabus_exam_name);
        etSubject  = findViewById(R.id.syllabus_subject);
        syllabusClass = findViewById(R.id.syllabus_class_spinner);
        syllabusDiv = findViewById(R.id.syllabus_division_spinner);
        addSyllabus = findViewById(R.id.add_syllabus);
        tvSyllabusFileName = findViewById(R.id.syllabus_file_name);

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
        responseDialog = new AlertDialog.Builder(context).create();
    }

    private void setupSpinner() {
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constant.classList);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        klass.setAdapter(classAdapter);
        syllabusClass.setAdapter(classAdapter);
        ArrayAdapter<String> divisionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constant.sectionList);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        division.setAdapter(divisionAdapter);
        syllabusDiv.setAdapter(divisionAdapter);
    }

    private void setupDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        lastDate.setText(mDay+"");
        String month = Utils.getMonthFromNo(mMonth+1);
        lastMonthYear.setText(month + " " + mYear);
        datePickerDialog  = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        selectedDate = year+"-"+String.format("%02d", monthOfYear+1)+"-"+
                                String.format("%02d", dayOfMonth);
                        lastDate.setText(dayOfMonth+"");
                        String month = Utils.getMonthFromNo(monthOfYear+1);
                        lastMonthYear.setText(month + " " + year);
                    }
                }, mYear, mMonth, mDay);

    }

    private void showPictureDialog(){
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, Constant.CODE_GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, Constant.CODE_CAMERA);
    }

    private void openFileChooser(){
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("*/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(intent, Constant.CODE_FILES);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String [] proj      = {MediaStore.Images.Media.DATA};
        Cursor cursor       = getContentResolver().query( contentUri, proj, null, null,null);
        if (cursor == null) return null;
        int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public File getFileFromBitmap(Bitmap bitmap, String filename){
        loading.show();
        File f = new File(context.getCacheDir(), filename);
        Utils.logPrint(getClass(), "FILE_1", f.toString());
        try {
            f.createNewFile();

            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            Utils.logPrint(getClass(), "FILE_2", f.toString());
        }catch (Exception e){
            Log.println(Log.ASSERT,"Image compression error",Log.getStackTraceString(e));
        }
        loading.dismiss();
        return f;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == Constant.CODE_GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                String path = getRealPathFromURI(contentURI);
                homework = new File(path);
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    image.setImageBitmap(imageBitmap);
                } catch (IOException e) {
                    Utils.logPrint(getClass(),"Image error",Log.getStackTraceString(e));
                }
            }

        } else if (requestCode == Constant.CODE_CAMERA) {
            imageBitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(imageBitmap);
            long timeMillis = System.currentTimeMillis();
            teacherId = Utils.getString(context, Constant.KEY_USER_ID, Constant.DUMMY);
            String fileName = teacherId + timeMillis + "_homework.png";
            homework = getFileFromBitmap(imageBitmap,fileName);
        } else if(requestCode == Constant.CODE_FILES){
            Utils.logPrint(getClass(), "data", data.toString());
            Uri fileUri = data.getData();
            Utils.logPrint(getClass(), "getEncodedPath", fileUri.getEncodedPath());
            Utils.logPrint(getClass(), "getPath", fileUri.getPath());
            try {
                String filePath = getRealPathFromURI(fileUri);
                syllabus = new File(filePath);
            }catch(Exception e){
                syllabus = new File(Environment.getExternalStorageDirectory()+fileUri.getPath());
            }
            Utils.logPrint(getClass(), "getAbsolutePath", syllabus.getAbsolutePath());
            Utils.logPrint(getClass(), "toString", syllabus.toString());

            tvSyllabusFileName.setText(syllabus.getName());
        }
    }

    public void submitClicked(View view) {
        institutionId = Utils.getString(context, Constant.KEY_INSTITUTION_ID, Constant.DUMMY);
        teacherId = Utils.getString(context, Constant.KEY_USER_ID, Constant.DUMMY);
        session = Utils.getString(context, Constant.KEY_SESSION, Constant.DUMMY);
        if(academicsType == Constant.ACADEMICS_HOMEWORK || academicsType == Constant.ACADEMICS_ASSIGNMENT){
            validateHomeWork();
        }else if(academicsType == Constant.ACADEMICS_SYLLABUS || academicsType == Constant.ACADEMICS_NOTES){
            validateSyllabus();
        }
    }

    private void validateSyllabus() {
        klassString = syllabusClass.getSelectedItem().toString();
        divisionString = syllabusDiv.getSelectedItem().toString();
        subject = etSubject.getText().toString().trim();
        examName = etExamName.getText().toString().trim();
        if(syllabus == null){
            errorDialog.setMessage(getString(R.string.no_syllabus_attached));
            errorDialog.show();
            return;
        }
        uploadSyllabus();
    }

    private void uploadSyllabus() {
        long timeMillis = System.currentTimeMillis();
//        String fileName = teacherId + timeMillis + "_homework.png";
        String fileName = syllabus.getName();
        File dir = Environment.getExternalStorageDirectory();
//        if(dir.exists()){
//            File to = new File(dir,fileName);
//            if(syllabus.exists())
//                syllabus.renameTo(to);
//        }
        Map<String, String> params = new HashMap<>();
        params.put("institute_id", institutionId);
        params.put("session", session);
        params.put("class", klassString);
        params.put("section", divisionString);
        params.put("teacher_id", teacherId);
        params.put("subject_name", subject);
        params.put("exam_name", examName);
        params.put("publish_date", String.valueOf(timeMillis));
        params.put("type", String.valueOf(academicsType));
        Utils.logPrint(getClass(), "SENDING", params.toString());

        new UploadFile2(syllabus, fileName, params).execute();
    }

    private void validateHomeWork() {
        klassString = klass.getSelectedItem().toString();
        divisionString = division.getSelectedItem().toString();
        subject = Constant.DUMMY;
        titleString = title.getText().toString().trim();
        descriptionString = description.getText().toString().trim();
        homeworkDateString = String.valueOf(System.currentTimeMillis());
        submissionDateString = selectedDate;
        if(submissionDateString.equals(getString(R.string.submission_date))){
            errorDialog.setMessage(getString(R.string.submission_date_needed));
            errorDialog.show();
            return;
        }
        if(imageBitmap == null){
            errorDialog.setMessage(getString(R.string.photo_needed));
            errorDialog.show();
            return;
        }
        uploadHomework();
    }

    public void uploadHomework(){
        long timeMillis = System.currentTimeMillis();
        String fileName = teacherId + timeMillis + "_homework.png";
        Map<String, String> params = new HashMap<>();
        params.put("status", "1");
        params.put("class_id", klassString);
        params.put("section_id", divisionString);
        params.put("uploader_id", teacherId);
        params.put("subject_id", "0");//hardcoded to all
        params.put("title", titleString);
        params.put("description", descriptionString);
        params.put("publish_date", String.valueOf(timeMillis));//Utils.getTimestampInFormat(timeMillis,Constant.SERVER_DATE_FORMAT));
        params.put("date_end", submissionDateString);
        params.put("time_end", "24:00");//hardcoded to day end
        params.put("type", String.valueOf(academicsType));
        params.put("year", session);
        Utils.logPrint(getClass(), "SENDING", params.toString());

        new UploadFile(homework, params).execute();
    }

    //for hw/assgnment
    public class UploadFile extends AsyncTask<Void, Void, Boolean>
    {
        File bitmap;
//        String name;
        Map<String, String> paramss;

        public UploadFile(File bitmap, Map<String, String> params) {
            this.bitmap = bitmap;
//            this.name = name;
            this.paramss = params;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.show();
        }

        @Override
        protected Boolean doInBackground(Void ...params) {
            File sourceFile = bitmap;
            Utils.logPrint(this.getClass(),"uploading",sourceFile.getName());
            String upLoadServerUri = Constant.URL_HOMEWORK_UPLOAD;
            int serverResponseCode = 0;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "apiclient-" + System.currentTimeMillis();// = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1024 * 1024 * 1024;
            if (!sourceFile.isFile())
            {
                Log.e("uploadFile", "Source File Does not exist");
                return false;
            }
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);
                conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("image", sourceFile.getName());
                //conn.setRequestProperty("pid", "4");
                dos = new DataOutputStream(conn.getOutputStream());

                for (Map.Entry<String, String> param : paramss.entrySet()) {
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + param.getKey() + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(param.getValue() + lineEnd);
                }

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""+ sourceFile.getName() + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String response = bufferedReader.readLine();

                Utils.logPrint(this.getClass(),"serverResponse", String.valueOf(serverResponseCode) + serverResponseMessage);
                Utils.logPrint(this.getClass(),"Response", response);
                if(serverResponseCode != 200 || !response.contains("1"))
                {
                    return false;
                }
                JSONObject jsonObject = new JSONObject(response);
                int success = jsonObject.getInt("success");
                if(success != 1){
                    return false;
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (Exception ex) {
                Log.println(Log.ASSERT,"uploading error",String.valueOf(serverResponseCode)+Log.getStackTraceString(ex));
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            Utils.logPrint(this.getClass(),"success", String.valueOf(success));
            loading.dismiss();
            if(success){
                new AlertDialog.Builder(context)
                        .setMessage(getString(R.string.uploded))
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancel(true);
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            onBackPressed();
                        }
                    }).create().show();
            }else {
                new AlertDialog.Builder(context)
                        .setMessage(getString(R.string.upload_fail))
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadHomework();
                            }
                        }).create().show();
            }
        }
    }


    public class UploadFile2 extends AsyncTask<Void, Void, Boolean>
    {
        File file;
        String name;
        Map<String, String> paramss;

        public UploadFile2(File file,String name, Map<String, String> params) {
            this.file = file;
            this.name = name;
            this.paramss = params;
        }

        public File getFileFromBitmap(Bitmap bitmap, String filename){
            File f = new File(context.getCacheDir(), filename);
            Utils.logPrint(getClass(), "FILE_1", f.toString());
            try {
                f.createNewFile();

                //Convert bitmap to byte array
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 80 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
                Utils.logPrint(getClass(), "FILE_2", f.toString());
            }catch (Exception e){
                Log.println(Log.ASSERT,"Image compression error",Log.getStackTraceString(e));
            }
            return f;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.show();
        }

        @Override
        protected Boolean doInBackground(Void ...params) {
            File sourceFile = file;
            Utils.logPrint(this.getClass(),"uploading",sourceFile.getName());
            String upLoadServerUri = Constant.URL_SYLLABUS_UPLOAD;
            int serverResponseCode = 0;
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "apiclient-" + System.currentTimeMillis();// = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 10 * 1024 * 1024;
            if (!sourceFile.isFile())
            {
                Utils.logPrint(getClass(),"source file", "file does not exist");
                return false;
            }
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);
                conn = (HttpURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("image", sourceFile.getName());
                //conn.setRequestProperty("pid", "4");
                dos = new DataOutputStream(conn.getOutputStream());

                for (Map.Entry<String, String> param : paramss.entrySet()) {
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + param.getKey() + "\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(param.getValue() + lineEnd);
                }

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""+ sourceFile.getName() + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available(); // create a buffer of  maximum size

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String response = bufferedReader.readLine();

                Utils.logPrint(this.getClass(),"serverResponse", String.valueOf(serverResponseCode) + serverResponseMessage);
                Utils.logPrint(this.getClass(),"Response", response);
                if(serverResponseCode != 200 || !response.contains("1"))
                {
                    return false;
                }

                JSONObject jsonObject = new JSONObject(response);
                int success = jsonObject.getInt("success");
                if(success != 1){
                    return false;
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (Exception ex) {
                Log.println(Log.ASSERT,"uploading error",String.valueOf(serverResponseCode)+Log.getStackTraceString(ex));
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            Utils.logPrint(this.getClass(),"success", String.valueOf(success));
            loading.dismiss();
            if(success){
                new AlertDialog.Builder(context)
                        .setMessage(getString(R.string.uploded))
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancel(true);
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                onBackPressed();
                            }
                        }).create().show();
            }else {
                new AlertDialog.Builder(context)
                        .setMessage(getString(R.string.upload_fail))
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadSyllabus();
                            }
                        }).create().show();
            }
        }
    }


}
