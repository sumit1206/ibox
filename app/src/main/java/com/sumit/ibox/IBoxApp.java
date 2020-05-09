package com.sumit.ibox;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.RoutineConstant;
import com.sumit.ibox.local.CookiesAdapter;
import com.sumit.ibox.services.ProfileUpdateService;
import com.sumit.ibox.services.UpdateClassSection;
import com.sumit.ibox.services.UpdateStudentRoutine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class IBoxApp extends Application {

    public static File STORAGE_DIR;
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        ContextWrapper cw = new ContextWrapper(this);
        STORAGE_DIR = cw.getDir(context.getPackageName(), Context.MODE_PRIVATE);
        if (!STORAGE_DIR.exists()) {
            try {
                STORAGE_DIR.mkdir();
                Log.println(Log.ASSERT, "ARapp","directory created");
            }catch (Exception e){
                Log.println(Log.ASSERT, "ARapp","error creating directory: "+Log.getStackTraceString(e));
            }
        }else{
            Log.println(Log.ASSERT, "ARapp","directory exists");
        }
        createLocalDb();
//        createStudentRoutine();
        updateClassSectionRoutine();
    }

    private void createLocalDb() {
        CookiesAdapter cookiesAdapter = new CookiesAdapter(context);
        cookiesAdapter.createDatabase();
    }

    public static void updateProfiles(){
        context.startService(new Intent(context, ProfileUpdateService.class));
    }

    void createStudentRoutine(){
        String path = STORAGE_DIR + "";
        String fileName = Constant.FILE_NAME_STUDENT_ROUTINE;
        File dir = new File(path, Constant.SAVE_PATH_CACHE_DUMP);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(path +"/"+ Constant.SAVE_PATH_CACHE_DUMP+"/"+ fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
                String data =
                        RoutineConstant.DAY_MONDAY + Constant.DELIMETER + "1" + Constant.DELIMETER + "A" + Constant.DELIMETER + "Maths" + Constant.DELIMETER + "11:00 AM" + Constant.DELIMETER + "Mr. Diptesh" + Constant.LINE_SEPARATOR +
                                RoutineConstant.DAY_MONDAY + Constant.DELIMETER + "1" + Constant.DELIMETER + "A" + Constant.DELIMETER + "Bengali" + Constant.DELIMETER + "12:00 AM" + Constant.DELIMETER + "Mr. Sumit" + Constant.LINE_SEPARATOR +
                                RoutineConstant.DAY_MONDAY + Constant.DELIMETER + "1" + Constant.DELIMETER + "A" + Constant.DELIMETER + "History" + Constant.DELIMETER + "01:00 AM" + Constant.DELIMETER + "Mr. Soumo" + Constant.LINE_SEPARATOR +
                                RoutineConstant.DAY_TUESDAY + Constant.DELIMETER + "1" + Constant.DELIMETER + "A" + Constant.DELIMETER + "Physics" + Constant.DELIMETER + "11:00 AM" + Constant.DELIMETER + "Mr. Diptesh" + Constant.LINE_SEPARATOR +
                                RoutineConstant.DAY_TUESDAY + Constant.DELIMETER + "1" + Constant.DELIMETER + "A" + Constant.DELIMETER + "Chemistry" + Constant.DELIMETER + "12:00 AM" + Constant.DELIMETER + "Mr. Sumit" + Constant.LINE_SEPARATOR +
                                RoutineConstant.DAY_TUESDAY + Constant.DELIMETER + "1" + Constant.DELIMETER + "A" + Constant.DELIMETER + "Biology" + Constant.DELIMETER + "01:00 AM" + Constant.DELIMETER + "Mr. Soumo" + Constant.LINE_SEPARATOR +
                                RoutineConstant.DAY_WEDNESDAY + Constant.DELIMETER + "1" + Constant.DELIMETER + "A" + Constant.DELIMETER + "Computer" + Constant.DELIMETER + "11:00 AM" + Constant.DELIMETER + "Mr. Diptesh" + Constant.LINE_SEPARATOR +
                                RoutineConstant.DAY_WEDNESDAY + Constant.DELIMETER + "1" + Constant.DELIMETER + "A" + Constant.DELIMETER + "Psychology" + Constant.DELIMETER + "12:00 AM" + Constant.DELIMETER + "Mr. Sumit" + Constant.LINE_SEPARATOR +
                                RoutineConstant.DAY_WEDNESDAY + Constant.DELIMETER + "1" + Constant.DELIMETER + "A" + Constant.DELIMETER + "Astronomy" + Constant.DELIMETER + "01:00 AM" + Constant.DELIMETER + "Mr. Soumo" + Constant.LINE_SEPARATOR;

                FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                outputStreamWriter.write(data);
                fileOutputStream.flush();
                outputStreamWriter.flush();
                outputStreamWriter.close();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            Log.println(Log.ASSERT,"Error creating csv",Log.getStackTraceString(e));
        }
    }

    private void updateClassSectionRoutine() {
        new UpdateClassSection().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
