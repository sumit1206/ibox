package com.sumit.ibox.ui.teacher.teacher_schedule;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import com.sumit.ibox.controller.TeacherRoutineAdapter;
import com.sumit.ibox.local.CookiesAdapter;
import com.sumit.ibox.local.CookiesAttribute;
import com.sumit.ibox.model.StudentRoutineData;
import com.sumit.ibox.model.TeacherRoutineData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.sumit.ibox.common.RoutineConstant.DAY_FRIDAY;
import static com.sumit.ibox.common.RoutineConstant.DAY_MONDAY;
import static com.sumit.ibox.common.RoutineConstant.DAY_SATURDAY;
import static com.sumit.ibox.common.RoutineConstant.DAY_SUNDAY;
import static com.sumit.ibox.common.RoutineConstant.DAY_THURSDAY;
import static com.sumit.ibox.common.RoutineConstant.DAY_TUESDAY;
import static com.sumit.ibox.common.RoutineConstant.DAY_WEDNESDAY;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherRoutineFragment extends Fragment {

    Context context;
    RecyclerView teacherRoutineRecyclerView;
    TeacherRoutineAdapter teacherRoutineAdapter;
//    private List<TeacherRoutineData> teacherRoutineDataList = new ArrayList<>();
    private List<TeacherRoutineData> sundayList, mondayList, tuesdayList, wednesdayList, thursdayList, fridayList, saturdayList;
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
                case R.id.teacher_routine_day_sun:
                    setupRecyclerView(sundayList);
                    break;
                case R.id.teacher_routine_day_mon:
                    setupRecyclerView(mondayList);
                    break;
                case R.id.teacher_routine_day_tue:
                    setupRecyclerView(tuesdayList);
                    break;
                case R.id.teacher_routine_day_wed:
                    setupRecyclerView(wednesdayList);
                    break;
                case R.id.teacher_routine_day_thu:
                    setupRecyclerView(thursdayList);
                    break;
                case R.id.teacher_routine_day_fri:
                    setupRecyclerView(fridayList);
                    break;
                case R.id.teacher_routine_day_sat:
                    setupRecyclerView(saturdayList);
                    break;
                default:
            }
        }
    };

    public TeacherRoutineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//         Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_routine, container, false);

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
        sun = view.findViewById(R.id.teacher_routine_day_sun);
        sun.setOnClickListener(dayClicked);
        mon = view.findViewById(R.id.teacher_routine_day_mon);
        mon.setOnClickListener(dayClicked);
        tue = view.findViewById(R.id.teacher_routine_day_tue);
        tue.setOnClickListener(dayClicked);
        wed = view.findViewById(R.id.teacher_routine_day_wed);
        wed.setOnClickListener(dayClicked);
        thu = view.findViewById(R.id.teacher_routine_day_thu);
        thu.setOnClickListener(dayClicked);
        fri = view.findViewById(R.id.teacher_routine_day_fri);
        fri.setOnClickListener(dayClicked);
        sat = view.findViewById(R.id.teacher_routine_day_sat);
        sat.setOnClickListener(dayClicked);
        teacherRoutineRecyclerView = view.findViewById(R.id.teacher_routine_recycler_view);
        sundayList = new ArrayList<>();
        mondayList = new ArrayList<>();
        tuesdayList = new ArrayList<>();
        wednesdayList = new ArrayList<>();
        thursdayList = new ArrayList<>();
        fridayList = new ArrayList<>();
        saturdayList = new ArrayList<>();
    }

    private void setupRecyclerView(List<TeacherRoutineData> teacherRoutineDataList){
        teacherRoutineRecyclerView.setHasFixedSize(true);
        teacherRoutineAdapter = new TeacherRoutineAdapter(getContext(), teacherRoutineDataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
        teacherRoutineRecyclerView.setLayoutManager(linearLayoutManager);
        teacherRoutineRecyclerView.setAdapter(teacherRoutineAdapter);
        teacherRoutineRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private int today(){
        Calendar c = Calendar.getInstance();
        Date today = new Date(System.currentTimeMillis());
        c.setTime(today);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    private void readFromFile(){
        String response = null;
        try {
            response = Utils.readFromCache(Constant.FILE_NAME_TEACHER_ROUTINE);
            Utils.logPrint(getClass(),"routine", response);
        } catch (IOException e) {
            Utils.logPrint(getClass(),"error", Log.getStackTraceString(e));
            return;
        }
        try {
            TeacherRoutineData subjectData;
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("details");
            for(int i = 0; i < jsonArray.length(); i++){
                //success, details, class_name, section_name, subject_name, teacher_name, start_time, end_time, day
                JSONObject object = jsonArray.getJSONObject(i);
                String klass = object.getString("class_name");
                String section = object.getString("section_name");
                String subject = object.getString("subject_name");
                String teacher = object.getString("teacher_name");
                String startTime = object.getString("start_time");
                String endTime = object.getString("end_time");
//                String time = startTime + " \n " + endTime;
                String day = object.getString("day");
                subjectData = new TeacherRoutineData(startTime,subject,klass,section,endTime);
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
            Utils.logPrint(getClass(),"sundayList",sundayList.toString());
            Utils.logPrint(getClass(),"mondayList",mondayList.toString());
            Utils.logPrint(getClass(),"tuesdayList",tuesdayList.toString());
            Utils.logPrint(getClass(),"wednesdayList",wednesdayList.toString());
            Utils.logPrint(getClass(),"thursdayList",thursdayList.toString());
            Utils.logPrint(getClass(),"fridayList",fridayList.toString());
            Utils.logPrint(getClass(),"saturdayList",saturdayList.toString());
//

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

//    private void RoutineData() {
//        TeacherRoutineData teacherRoutineData = new TeacherRoutineData("10:00","Math","1","A","11:00");
//        teacherRoutineDataList.add(teacherRoutineData);
//        teacherRoutineData = new TeacherRoutineData("11:00","Bengali","2","A","12:00");
//        teacherRoutineDataList.add(teacherRoutineData);
//        teacherRoutineData = new TeacherRoutineData("12:00","English","3","B","01:00");
//        teacherRoutineDataList.add(teacherRoutineData);
//        teacherRoutineData = new TeacherRoutineData("01:00","Math","1","A","02:00");
//        teacherRoutineDataList.add(teacherRoutineData);
//        teacherRoutineData = new TeacherRoutineData("02:00","Geography","5","A","03:00");
//        teacherRoutineDataList.add(teacherRoutineData);
//        teacherRoutineData = new TeacherRoutineData("03:00","Math","1","A","04:00");
//        teacherRoutineDataList.add(teacherRoutineData);
//        teacherRoutineData = new TeacherRoutineData("05:00","Math","1","A","06:00");
//        teacherRoutineDataList.add(teacherRoutineData);
//        teacherRoutineAdapter.notifyDataSetChanged();
//    }
}
