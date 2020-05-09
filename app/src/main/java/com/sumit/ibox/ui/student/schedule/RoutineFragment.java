package com.sumit.ibox.ui.student.schedule;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.RoutineAdapterStudent;
import com.sumit.ibox.local.CookiesAdapter;
import com.sumit.ibox.local.CookiesAttribute;
import com.sumit.ibox.model.StudentRoutineData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static com.sumit.ibox.IBoxApp.STORAGE_DIR;
import static com.sumit.ibox.common.RoutineConstant.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoutineFragment extends Fragment {

    Context context;
    private RecyclerView routineRecyclerView;
//    private ;
    private List<StudentRoutineData> sundayList, mondayList, tuesdayList, wednesdayList, thursdayList, fridayList, saturdayList;
    private RoutineAdapterStudent routineAdapterStudent;
    private TextView sun, mon, tue, wed, thu, fri, sat;
    View.OnClickListener dayClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sun.setBackground(null);
            mon.setBackground(null);
            tue.setBackground(null);
            wed.setBackground(null);
            thu.setBackground(null);
            fri.setBackground(null);
            sat.setBackground(null);
            v.setBackground(getResources().getDrawable(R.drawable.round_button));

            int id = v.getId();
            switch (id){
                case R.id.student_routine_sunday:
                    setupRecyclerView(sundayList);
                    break;
                case R.id.student_routine_monday:
                    setupRecyclerView(mondayList);
                    break;
                case R.id.student_routine_tuesday:
                    setupRecyclerView(tuesdayList);
                    break;
                case R.id.student_routine_wednesday:
                    setupRecyclerView(wednesdayList);
                    break;
                case R.id.student_routine_thursday:
                    setupRecyclerView(thursdayList);
                    break;
                case R.id.student_routine_friday:
                    setupRecyclerView(fridayList);
                    break;
                case R.id.student_routine_saturday:
                    setupRecyclerView(saturdayList);
                    break;
                default:
            }
        }
    };

    public RoutineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_routine, container, false);

        init(view);
        readFromFile();
        switch (today()){
            case 1:
                sun.performClick();
                break;
            case 2:
                mon.performClick();
                break;
            case 3:
                tue.performClick();
                break;
            case 4:
                wed.performClick();
                break;
            case 5:
                thu.performClick();
                break;
            case 6:
                fri.performClick();
                break;
            case 7:
                sat.performClick();
                break;

        }
        return view;
    }

    private void init(View view) {
        context = getContext();
        sun = view.findViewById(R.id.student_routine_sunday);
        sun.setOnClickListener(dayClicked);
        mon = view.findViewById(R.id.student_routine_monday);
        mon.setOnClickListener(dayClicked);
        tue = view.findViewById(R.id.student_routine_tuesday);
        tue.setOnClickListener(dayClicked);
        wed = view.findViewById(R.id.student_routine_wednesday);
        wed.setOnClickListener(dayClicked);
        thu = view.findViewById(R.id.student_routine_thursday);
        thu.setOnClickListener(dayClicked);
        fri = view.findViewById(R.id.student_routine_friday);
        fri.setOnClickListener(dayClicked);
        sat = view.findViewById(R.id.student_routine_saturday);
        sat.setOnClickListener(dayClicked);
        routineRecyclerView = view.findViewById(R.id.routine_recycler_view);
        sundayList = new ArrayList<>();
        mondayList = new ArrayList<>();
        tuesdayList = new ArrayList<>();
        wednesdayList = new ArrayList<>();
        thursdayList = new ArrayList<>();
        fridayList = new ArrayList<>();
        saturdayList = new ArrayList<>();

    }

//    private void readFromFile() {
//        StudentRoutineData subjectData;
//        String path = STORAGE_DIR + "";
//        String fileName = Constant.FILE_NAME_STUDENT_ROUTINE;
//        File file = new File(path +"/"+ Constant.SAVE_PATH_CACHE_DUMP+"/"+ fileName);
//        try {
//            Scanner scanner = new Scanner(file);
//            while (scanner.hasNextLine()) {
//                String[] data = scanner.nextLine().split(",");
//                String day = data[INDEX_DAY];
//                String klass = data[INDEX_CLASS];
//                String section = data[INDEX_SECTION];
//                if(!klass.equals("1") || !section.equals("A")){
//                    continue;
//                }
//                subjectData = new StudentRoutineData(data[INDEX_TIME],data[INDEX_SUBJECT],data[INDEX_TEACHER]);
//                Utils.logPrint(getClass(),"day",day);
//                switch (day){
//                    case DAY_SUNDAY:
//                        Utils.logPrint(getClass(),"DAY_SUNDAY",day);
//                        sundayList.add(subjectData);
//                        break;
//                    case DAY_MONDAY:
//                        Utils.logPrint(getClass(),"DAY_MONDAY",day);
//                        mondayList.add(subjectData);
//                        break;
//                    case DAY_TUESDAY:
//                        Utils.logPrint(getClass(),"DAY_TUESDAY",day);
//                        tuesdayList.add(subjectData);
//                        break;
//                    case DAY_WEDNESDAY:
//                        Utils.logPrint(getClass(),"DAY_WEDNESDAY",day);
//                        wednesdayList.add(subjectData);
//                        break;
//                    case DAY_THURSDAY:
//                        Utils.logPrint(getClass(),"DAY_THURSDAY",day);
//                        thursdayList.add(subjectData);
//                        break;
//                    case DAY_FRIDAY:
//                        Utils.logPrint(getClass(),"DAY_FRIDAY",day);
//                        fridayList.add(subjectData);
//                        break;
//                    case DAY_SATURDAY:
//                        Utils.logPrint(getClass(),"DAY_SATURDAY",day);
//                        saturdayList.add(subjectData);
//                        break;
//                    default:
//                }
//            }
//            Utils.logPrint(getClass(),"sundayList",sundayList.toString());
//            Utils.logPrint(getClass(),"mondayList",mondayList.toString());
//            Utils.logPrint(getClass(),"tuesdayList",tuesdayList.toString());
//            Utils.logPrint(getClass(),"wednesdayList",wednesdayList.toString());
//            Utils.logPrint(getClass(),"thursdayList",thursdayList.toString());
//            Utils.logPrint(getClass(),"fridayList",fridayList.toString());
//            Utils.logPrint(getClass(),"saturdayList",saturdayList.toString());
//            scanner.close();
//        }catch (Exception e){
//            Utils.logPrint(getClass(),"Error reading routine", Log.getStackTraceString(e));
//        }

//    }

    private void readFromFile(){
        String response = null;
        try {
            response = Utils.readFromCache(Constant.FILE_NAME_STUDENT_ROUTINE);
        } catch (IOException e) {
            Utils.logPrint(getClass(),"error",Log.getStackTraceString(e));
            return;
        }
        CookiesAdapter cookiesAdapter = new CookiesAdapter(context);
        cookiesAdapter.openReadable();
        String studentId = Utils.getString(context, Constant.KEY_STUDENT_ID, Constant.DUMMY);
        String myClass = cookiesAdapter.getFromStudent(studentId, CookiesAttribute.student_class);
        String mySection = cookiesAdapter.getFromStudent(studentId, CookiesAttribute.student_section);
        cookiesAdapter.close();
        try {//success, details, class_name, section_name, subject_name, teacher_name, start_time, end_time, day
            StudentRoutineData subjectData;
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("details");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String klass = object.getString("class_name");
                String section = object.getString("section_name");
                if(!myClass.equals(klass) || !mySection.equals(section)){
//                    Utils.logPrint(getClass(),"continue",sundayList.toString());
                    continue;
                }
                String subject = object.getString("subject_name");
                String teacher = object.getString("teacher_name");
                String startTime = object.getString("start_time");
                String endTime = object.getString("end_time");
                String time = startTime + " \n " + endTime;
                String day = object.getString("day");
                subjectData = new StudentRoutineData(time, subject, teacher);
                if(isSame(DAY_MONDAY, day)){
                    mondayList.add(subjectData);
                }else if(isSame(DAY_TUESDAY, day)){
                    tuesdayList.add(subjectData);
                }else if(isSame(DAY_WEDNESDAY, day)){
                    wednesdayList.add(subjectData);
                }else if(isSame(DAY_THURSDAY, day)){
                    thursdayList.add(subjectData);
                }else if(isSame(DAY_FRIDAY, day)){
                    fridayList.add(subjectData);
                }else if(isSame(DAY_SATURDAY, day)){
                    saturdayList.add(subjectData);
                }else if(isSame(DAY_SUNDAY, day)){
                    sundayList.add(subjectData);
                }
            }

        } catch (JSONException e) {
            Utils.logPrint(getClass(),"error",Log.getStackTraceString(e));
        }
    }

    private boolean isSame(String constant, String day){
        String big = day.toUpperCase();
        String small = constant.toUpperCase();
        boolean isSame = big.contains(small);
//        Utils.logPrint(getClass(),"returning", constant +","+small +","+ day +","+big +","+isSame);
        return isSame;
    }

    private void setupRecyclerView(List<StudentRoutineData> studentRoutineDataAray){
        routineAdapterStudent = new RoutineAdapterStudent(context,studentRoutineDataAray);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        routineRecyclerView.setLayoutManager(mLayoutManager);
        routineRecyclerView.setAdapter(routineAdapterStudent);
    }

    private int today(){
        Calendar c = Calendar.getInstance();
        Date today = new Date(System.currentTimeMillis());
        c.setTime(today);
        return c.get(Calendar.DAY_OF_WEEK);
    }

//    private void RoutineData() {
//
//        StudentRoutineData studentRoutineData = new StudentRoutineData("10:00 AM","History",R.drawable.geography,"Mr.diptesh");
//        studentRoutineDataAray.add(studentRoutineData);
//        studentRoutineData = new StudentRoutineData("11:00 AM","Geography",R.drawable.geography,"Mr.shivam");
//        studentRoutineDataAray.add(studentRtooutineData);
//        studentRoutineData = new StudentRoutineData("12:00 PM","Math",R.drawable.geography,"Mr.Soumo");
//        studentRoutineDataAray.add(studentRoutineData);
//        studentRoutineData = new StudentRoutineData("01:00 PM","Science",R.drawable.geography,"Mr.amit");
//        studentRoutineDataAray.add(studentRoutineData);
//        studentRoutineData = new StudentRoutineData("02:00 PM","English",R.drawable.geography,"Mr.Soumo");
//        studentRoutineDataAray.add(studentRoutineData);
//        studentRoutineData = new StudentRoutineData("03:00 PM","biology",R.drawable.geography,"Mr.Soumo");
//        studentRoutineDataAray.add(studentRoutineData);
//        studentRoutineData = new StudentRoutineData("04:00 PM","Sports",R.drawable.geography,"Mr.Soumo");
//        studentRoutineDataAray.add(studentRoutineData);
//
//    }

}
