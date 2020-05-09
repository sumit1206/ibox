package com.sumit.ibox.ui.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sumit.ibox.model.AlertDialog;
import android.app.DatePickerDialog;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.model.CalendarView;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class TeacherLeaveActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_START = 1;
    private static final int REQUEST_CODE_END = 2;

    CalendarView cv;
    Toolbar leaveToolar;
    Context context;
    LinearLayout llStartDate, llEndDate;
    TextView tvStartDay, tvStartMonthYear, tvEndDay, tvEndMonthYear;
    EditText etDescription;
    String startDate;
    String endDate;
    TextView tvTotal, tvTaken, tvPending;
    ProgressDialog loading;
    AlertDialog errorDialog;

    VolleyCallback leaveCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.uploded));
            errorDialog.show();
        }

        @Override
        public void noDataFound() {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.upload_fail));
            errorDialog.show();
        }

        @Override
        public void onCatch(JSONException e) {
            loading.dismiss();
            Utils.logPrint(getClass(),"response error2", Log.getStackTraceString(e));
            errorDialog.setMessage(getString(R.string.error_occurred));
            errorDialog.show();
        }

        @Override
        public void onError(VolleyError e) {
            loading.dismiss();
            Utils.logPrint(getClass(),"no response error", Log.getStackTraceString(e));
            errorDialog.setMessage(getString(R.string.error_occurred));
            errorDialog.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_leave);

        setUpToolbar();
        init();
        setCalenderEvents();
        addHolidayEvents();
        fetchLeaveStatus();
        autosetCurrentDate();
    }

    void setUpToolbar(){
        leaveToolar = findViewById(R.id.teacher_leave_toolbar);
        leaveToolar.setTitle(R.string.leave);
        leaveToolar.setTitleTextColor(getResources().getColor(R.color.white));
        leaveToolar.setNavigationIcon(R.drawable.arrow_left);
        leaveToolar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        context = this;
        cv = findViewById(R.id.teacher_leave_calender);
        llStartDate = findViewById(R.id.teacher_leave_start_date);
        tvStartDay = findViewById(R.id.teacher_leave_start_date_day);
        tvStartMonthYear = findViewById(R.id.teacher_leave_start_date_month_year);
        llEndDate = findViewById(R.id.teacher_leave_end_date);
        tvEndDay = findViewById(R.id.teacher_leave_end_date_day);
        tvEndMonthYear = findViewById(R.id.teacher_leave_end_date_month_year);
        etDescription = findViewById(R.id.teacher_leave_description);

        tvTotal = findViewById(R.id.teacher_leave_total);
        tvTaken = findViewById(R.id.teacher_leave_taken);
        tvPending = findViewById(R.id.teacher_leave_pending);

        llStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(REQUEST_CODE_START);
            }
        });
        llEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(REQUEST_CODE_END);
            }
        });
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

    private void showDatePicker(final int requestCode) {
        DatePickerDialog datePickerDialog;
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        datePickerDialog  = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String selectedDate = year+"-"+String.format("%02d", monthOfYear+1)+"-"+
                                String.format("%02d", dayOfMonth);
                        if(requestCode == REQUEST_CODE_START){
                            startDate = selectedDate;
                            String startDay, startMonthYear;
                            startDay = String.format("%02d", dayOfMonth);
                            startMonthYear = Utils.getMonthFromNo(monthOfYear+1) + " " + year;
                            tvStartDay.setText(startDay);
                            tvStartMonthYear.setText(startMonthYear);
                        } else if(requestCode == REQUEST_CODE_END){
                            endDate = selectedDate;
                            String endDay, endMonthYear;
                            endDay = String.format("%02d", dayOfMonth);
                            endMonthYear = Utils.getMonthFromNo(monthOfYear+1) + " " + year;
                            tvEndDay.setText(endDay);
                            tvEndMonthYear.setText(endMonthYear);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void autosetCurrentDate() {
        final int ONE_DAY = 1000 * 60 * 60 * 24;
        long today = System.currentTimeMillis();
        long tomorrow = today + ONE_DAY;
        startDate = Utils.getTimestampInFormat(today, "yyyy-MM-dd");
        tvStartDay.setText(Utils.getTimestampInFormat(today, "dd"));
        tvStartMonthYear.setText(Utils.getTimestampInFormat(today, "MMM yyyy"));

        endDate = Utils.getTimestampInFormat(tomorrow, "yyyy-MM-dd");
        tvEndDay.setText(Utils.getTimestampInFormat(tomorrow, "dd"));
        tvEndMonthYear.setText(Utils.getTimestampInFormat(tomorrow, "MMM yyyy"));
    }

    private void fetchLeaveStatus() {
        VolleyCallback leaveStateCallback = new VolleyCallback() {
            @Override
            public void onSuccess(Object result) {
                JSONObject jsonObject = (JSONObject) result;
                try {//success, details, total_leave, leave_taken
                    JSONArray jsonArray = jsonObject.getJSONArray("details");
                    int total = jsonArray.getJSONObject(0).getInt("total_leave");
                    int taken = jsonArray.getJSONObject(0).getInt("leave_taken");
                    int pending = total - taken;
                    tvTotal.setText(total+"");
                    tvTaken.setText(taken+"");
                    tvPending.setText(pending+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void noDataFound() {

            }

            @Override
            public void onCatch(JSONException e) {

            }

            @Override
            public void onError(VolleyError e) {

            }
        };
        String username = Utils.getString(context, Constant.KEY_USER_ID, Constant.DUMMY);
        Perform.fetchTeacherLeaveState(context, leaveStateCallback, username);
    }

    public void applyClick(View view) {
        String description = etDescription.getText().toString().trim();
        if(description.equals("")){
            Utils.showShortToast(context, "Description needed");
            return;
        }
        String username = Utils.getString(context, Constant.KEY_USER_ID, Constant.DUMMY);
        String title = "";
        loading.show();
        Perform.uploadLeave(context, leaveCallback, username, startDate, endDate, description, title);
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

    private void addHolidayEvents() {
        try {
            HashSet<CalendarView.Event> events = new HashSet<>();
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
                    events.add(new CalendarView.Event(new Date(date.getTime()),Constant.ATTENDENCE_HOLIDAY,title));
                    Utils.logPrint(getClass(),"adding",date+""+date.getDate());
                }
            }
            cv.updateCalendar(events);
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

}
