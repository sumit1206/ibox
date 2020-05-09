package com.sumit.ibox.ui.student.schedule;


import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.model.CalendarView;
import com.sumit.ibox.model.CalendarView.Event;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceFragment extends Fragment {


    public AttendanceFragment() {

    }

    Context context;
    CalendarView cv;
    HashSet<Event> events = new HashSet<>();
    ProgressDialog loading;
    AlertDialog errorDialog;
    TextView absent, present;
    static int totalPresent = 0, totalAbsent = 0;

    CalendarView.EventHandler calenderCallbacks = new CalendarView.EventHandler()
    {
        @Override
        public void onPrevPressed(int year, int month) {
            fetchAttendence(year, month+1);
        }

        @Override
        public void onNextPressed(int year, int month) {
            fetchAttendence(year, month+1);
        }

        @Override
        public void onDayShortPress(Date date, Event event) {
            super.onDayShortPress(date, event);
            if(event == null)
                return;
            if(event.getType() == Constant.ATTENDENCE_ABSENT) {
                showActivityDialog(date);
            }
        }
    };

    VolleyCallback attendenceCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            JSONObject jsonObject = (JSONObject) result;
            try {//details, timestamp, status
                JSONArray jsonArray = jsonObject.getJSONArray("details");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject post = jsonArray.getJSONObject(i);
                    String date = post.getString("timestamp");
                    String[] datePart = date.split(" ")[0].split("-");
                    int year = Integer.parseInt(datePart[0]);
                    int month = Integer.parseInt(datePart[1]);
                    int day = Integer.parseInt(datePart[2]);
                    int presentStatus = post.getInt("status");
                    String eventName;
                    if(presentStatus == Constant.ATTENDENCE_PRESENT){
                        eventName = "Present";
                        totalPresent++;
                    }else {
                        eventName = "Absent";
                        totalAbsent++;
                    }
                    events.add(new Event(new Date(year-1900,month-1,day),presentStatus,eventName));
                }
                cv.updateCalendar(events);
                updateUi();
            } catch (Exception e) {
                Utils.logPrint(getClass(),"response error1", Log.getStackTraceString(e));
                errorDialog.setMessage(getString(R.string.error_occurred));
                errorDialog.show();
            }
        }

        @Override
        public void noDataFound() {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.no_attendence));
            errorDialog.show();
            cv.updateCalendar(events);
            updateUi();
        }

        @Override
        public void onCatch(JSONException e) {
            loading.dismiss();
            Utils.logPrint(getClass(),"response error2", Log.getStackTraceString(e));
            errorDialog.setMessage(getString(R.string.error_occurred));
            errorDialog.show();
            cv.updateCalendar(events);
            updateUi();
        }

        @Override
        public void onError(VolleyError e) {
            loading.dismiss();
            Utils.logPrint(getClass(),"no response error", Log.getStackTraceString(e));
            errorDialog.setMessage(getString(R.string.error_occurred));
            errorDialog.show();
            cv.updateCalendar(events);
            updateUi();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attandance, container, false);

        init(view);

        cv.setEventHandler(calenderCallbacks);
        cv.setShortPressOnEventEnabled(true);
        setCalenderEvents();
        Calendar currentDate = cv.getCurrentDate();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        fetchAttendence(year, month+1);

//        cv.updateCalendar(events);
        return  view;
    }

    private void init(View view) {
        context = getContext();
        cv = view.findViewById(R.id.calendar_view);
        absent = view.findViewById(R.id.absent);
        present = view.findViewById(R.id.present);
        loading = new ProgressDialog(context);
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        errorDialog = new AlertDialog.Builder(context)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        errorDialog.cancel();
                    }
                })
                .create();
    }

    private void setCalenderEvents(){
        ArrayList<CalendarView.EventSet> calenderEvents = new ArrayList<>();
        calenderEvents.add(new CalendarView.EventSet(Constant.ATTENDENCE_ABSENT, R.drawable.yellow_round_button));
        calenderEvents.add(new CalendarView.EventSet(Constant.ATTENDENCE_PRESENT, R.drawable.green_round_button));
        calenderEvents.add(new CalendarView.EventSet(Constant.ATTENDENCE_WEEKEND, R.drawable.red_round_button));
        calenderEvents.add(new CalendarView.EventSet(Constant.ATTENDENCE_HOLIDAY, R.drawable.blue_round_button));
        cv.setEventSets(calenderEvents);

        ArrayList<Integer> weekendList = new ArrayList<>();
        weekendList.add(Calendar.SATURDAY);
        weekendList.add(Calendar.SUNDAY);
        cv.setWeekEndList(weekendList);
    }

    private void fetchAttendence(int year, int month) {
        String studentId = Utils.getString(context, Constant.KEY_STUDENT_ID, Constant.DUMMY);
        String session = Utils.getString(context, Constant.KEY_SESSION, Constant.DUMMY);
        events.clear();
        loading.show();
        Perform.attendenceFetch(context, attendenceCallback, studentId,session,String.valueOf(year), String.format("%02d", month));
        addHolidayEvents();
    }

    private void addHolidayEvents() {
        try {
            //success, details, title, start, end
            String response = Utils.readFromCache(Constant.FILE_NAME_HOLIDAY);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("details");
            for (int i = 0; i<jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String title = object.getString("title");
                String start = object.getString("start");
                String end = object.getString("end");
                Date date1 = makeDate(start);
                Date date2 = makeDate(end);
                long oneDay = 1000*60*60*24;
                for(Date date = date1; date.before(date2); date.setTime(date.getTime() + oneDay)){
                    events.add(new Event(new Date(date.getTime()),Constant.ATTENDENCE_HOLIDAY,title));
                    Utils.logPrint(getClass(),"adding",date+""+date.getDate());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Date makeDate(String s){
        String[] sep = s.split(" ");
        String[] parts = sep[0].split("-");
        int yyyy = Integer.parseInt(parts[0]);
        yyyy = yyyy - 1900;
        int MM = Integer.parseInt(parts[1]);
        MM = MM - 1;
        int dd = Integer.parseInt(parts[2]);
        return new Date(yyyy, MM, dd);
    }

    private void updateUi() {
        absent.setText(getString(R.string.absent)+" :"+totalAbsent);
        present.setText(getString(R.string.present)+" :"+totalPresent);
        totalAbsent = 0;
        totalPresent = 0;
    }

    private void showActivityDialog(Date dat){
        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.custom_popup_attandance, null);
        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);
        final android.app.AlertDialog popup = alertDialogBuilder.create();
        ImageView close = view.findViewById(R.id.application_popup_close);
        TextView date = view.findViewById(R.id.application_popup_date);
        TextView monthYear = view.findViewById(R.id.application_popup_month_year);
        final TextView rectify = view.findViewById(R.id.application_popup_rectify);
        final TextView leave = view.findViewById(R.id.application_popup_leave);
        final TextView submit = view.findViewById(R.id.application_popup_submit);
        final LinearLayout reaonLayout = view.findViewById(R.id.application_popup_reason_layout);
        final LinearLayout progressLayout = view.findViewById(R.id.application_popup_progress_layout);
        date.setText(Utils.getDateInFormat(dat,"dd"));
        monthYear.setText(Utils.getDateInFormat(dat,"MMM yy"));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
        rectify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reaonLayout.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                submit.setText(R.string.conform);
            }
        });
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reaonLayout.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                submit.setText(R.string.submit);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reaonLayout.setVisibility(View.GONE);
                progressLayout.setVisibility(View.VISIBLE);
                submit.setVisibility(View.INVISIBLE);
                rectify.setClickable(false);
                leave.setClickable(false);
            }
        });
        popup.show();
    }
}
