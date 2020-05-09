package com.sumit.ibox.ui.student.fees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.FeesDewListAdapter;
import com.sumit.ibox.model.FeeStructure;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeesActivity extends AppCompatActivity {

    Context context;
    private RecyclerView feesDewListRecyclerView;
    private List<FeeStructure> feeStructureArray;
    FeesDewListAdapter feesDewListAdapter;
    Toolbar toolbar;
    ProgressDialog loading;
    AlertDialog errorDialog;

    VolleyCallback feesCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            try {
                FeeStructure feeStructure;
                JSONObject jsonObject = (JSONObject) result;
                int success = jsonObject.getInt("success");
                if(success == 1){//success, data, month, amount, fine_amount, discount, last_date, class_id, month_id
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i = 0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String month = jsonObject1.getString("month");
                        int amount = jsonObject1.getInt("amount");
                        int fine = jsonObject1.getInt("fine_amount");
                        int discount;
                        try{
                            discount = jsonObject1.getInt("discount");
                        }catch (Exception e){
                            discount = 0;
                        }
                        String lastDate = jsonObject1.getString("last_date");
                        lastDate = Utils.getTimestampInFormat(Long.parseLong(lastDate), Constant.APP_DATE_FORMAT);
                        String classId = jsonObject1.getString("class_id");
                        String monthId = jsonObject1.getString("month_id");
                        feeStructure = new FeeStructure(month, amount, fine, discount, lastDate, classId, monthId, false);
                        feeStructureArray.add(feeStructure);
                    }
                }
                feesDewListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                Utils.errorLog(getClass(),"Error parsing json",e);
                errorDialog.setMessage(getString(R.string.error_occurred));
                errorDialog.show();
            }
        }

        @Override
        public void noDataFound() {
            loading.dismiss();
            errorDialog.setMessage(getString(R.string.no_fees));
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

    FeesBreakdownPopup.TouchEvents popupTouchEvents= new FeesBreakdownPopup.TouchEvents(){
        @Override
        public void onPayClick(FeeStructure feeStructure) {
            Intent gatewayIntent = new Intent(context, PaymentGatewayActivity.class);
            gatewayIntent.putExtra(Constant.KEY_FEES_AMOUNT, feeStructure.getAmount());
            gatewayIntent.putExtra(Constant.KEY_FEES_FINE, feeStructure.getFine());
            gatewayIntent.putExtra(Constant.KEY_FEES_DISCOUNT, feeStructure.getDiscount());
            gatewayIntent.putExtra(Constant.KEY_FEES_MONTH_LIST, feeStructure.getMonthId());
            gatewayIntent.putExtra(Constant.KEY_FEES_CLASS_ID, feeStructure.getClassId());
            startActivity(gatewayIntent);
        }
    };

    FeesDewListAdapter.TouchEvents touchEvents = new FeesDewListAdapter.TouchEvents() {
        @Override
        public void onClick(FeeStructure feeStructure) {
            new FeesBreakdownPopup(context, feeStructure, popupTouchEvents);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees_dew_list);

        init();
        setupToolbar();
        setupRecyclerView();

        fetchFeeList();
    }

    private void init() {
        context = this;
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

    private void setupToolbar() {
        toolbar = findViewById(R.id.fees_dew_toolbar);
        toolbar.setTitle("Due List");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupRecyclerView() {
        feesDewListRecyclerView = findViewById(R.id.dew_fees_list_recycler_view);
        feeStructureArray = new ArrayList<>();
        feesDewListAdapter = new FeesDewListAdapter(this, feeStructureArray, touchEvents);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        feesDewListRecyclerView.setLayoutManager(mLayoutManager);
        feesDewListRecyclerView.setAdapter(feesDewListAdapter);
    }

    private void fetchFeeList() {
        feeStructureArray.clear();
        String studentId = Utils.getString(context, Constant.KEY_STUDENT_ID, Constant.DUMMY);
        Utils.logPrint(getClass(),"studentId", studentId);
        loading.show();
        Perform.feesFetch(context, feesCallback, studentId);
    }

    public void payNowClick(View view) {
        if(feeStructureArray == null || feeStructureArray.isEmpty()){
            return;
        }
        ArrayList<String> monthList = new ArrayList<>();
        ArrayList<String> monthNameList = new ArrayList<>();
        int amount = 0, fine = 0, discout = 0;
        String classId = "";
        for(FeeStructure feeStructure : feeStructureArray){
            Utils.logPrint(getClass(),"isSelected",feeStructure.isSelected()+"");
            if(feeStructure.isSelected()){
                amount = amount + feeStructure.getAmount();
                discout = discout + feeStructure.getDiscount();
                fine = fine + feeStructure.getFine();
                monthList.add(feeStructure.getMonthId());
                monthNameList.add(feeStructure.getMonth());
                classId = feeStructure.getClassId();
            }
        }
        if(amount == 0){
            Utils.showShortToast(context, getString(R.string.no_amount));
        }else{
            Intent gatewayIntent = new Intent(context, PaymentGatewayActivity.class);
            gatewayIntent.putExtra(Constant.KEY_FEES_AMOUNT, amount);
            gatewayIntent.putExtra(Constant.KEY_FEES_FINE, fine);
            gatewayIntent.putExtra(Constant.KEY_FEES_DISCOUNT, discout);
            String ml = Utils.makeCommaSeparated(monthList);
            gatewayIntent.putExtra(Constant.KEY_FEES_MONTH_LIST, ml);
            String mnl = Utils.makeCommaSeparated(monthNameList);
            gatewayIntent.putExtra(Constant.KEY_FEES_MONTH_NAME_LIST, mnl);
            gatewayIntent.putExtra(Constant.KEY_FEES_CLASS_ID, classId);
            startActivity(gatewayIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
