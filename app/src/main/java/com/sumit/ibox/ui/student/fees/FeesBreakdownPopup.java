package com.sumit.ibox.ui.student.fees;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.FeesBreakupAdapter;
import com.sumit.ibox.model.FeeStructure;
import com.sumit.ibox.model.FeesBreakupData;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class FeesBreakdownPopup {

    Dialog alertDialog;
    Context context;
    FeeStructure feeStructure;
    TouchEvents touchEvents = null;
    ListView lvBreakupList;
    TextView tvDiscount, tvTotal;
    TextView btnPayNow;
    ProgressBar loading;
    FeesBreakupAdapter feesBreakupAdapter;
    ArrayList<FeesBreakupData> feesBreakupDataList = new ArrayList<>();

    VolleyCallback breakupCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.setVisibility(View.GONE);
            JSONObject jsonObject = (JSONObject) result;
            try {
                FeesBreakupData feesBreakupData;
                int success = jsonObject.getInt("success");
                if(success == 1){//success, details, type, amount
                    JSONArray jsonArray = jsonObject.getJSONArray("details");
                    for(int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String name = jsonObject1.getString("type");
                        String amount = jsonObject1.getString("amount");
                        feesBreakupData = new FeesBreakupData(name, amount);
                        feesBreakupDataList.add(feesBreakupData);
                    }
                    Utils.logPrint(getClass(),"list",feesBreakupDataList.toString());
                    feesBreakupAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                Utils.logPrint(getClass(),"2","caca");
                e.printStackTrace();
            }
        }

        @Override
        public void noDataFound() {
            Utils.logPrint(getClass(),"3","caca");
            loading.setVisibility(View.GONE);
        }

        @Override
        public void onCatch(JSONException e) {
            Utils.logPrint(getClass(),"4","caca");
            loading.setVisibility(View.GONE);
        }

        @Override
        public void onError(VolleyError e) {
            Utils.logPrint(getClass(),"5","caca");
            loading.setVisibility(View.GONE);
        }
    };

    public FeesBreakdownPopup(Context context, final FeeStructure feeStructure, final TouchEvents touchEvents) {
        this.context = context;
        alertDialog = new Dialog(context);
        alertDialog.setContentView(R.layout.fees_preview_diaog);
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.feeStructure = feeStructure;
        this.touchEvents = touchEvents;
        init();
        fetchFeesBreakup();
        showRestFields();
        showPopup();
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(touchEvents != null){
                    touchEvents.onPayClick(feeStructure);
                }
            }
        });
    }

    private void init() {
        loading = alertDialog.findViewById(R.id.fees_popup_progress_bar);
        lvBreakupList = alertDialog.findViewById(R.id.fees_popup_breakup_list);
        tvDiscount = alertDialog.findViewById(R.id.fees_popup_discount);
        tvTotal = alertDialog.findViewById(R.id.fees_popup_total);
        btnPayNow = alertDialog.findViewById(R.id.fees_popup_pay_now);

//        feesBreakupDataList = new ArrayList<>();
        feesBreakupAdapter = new FeesBreakupAdapter(context, R.layout.fees_preview_card, feesBreakupDataList);
        lvBreakupList.setAdapter(feesBreakupAdapter);
    }

    private void fetchFeesBreakup() {
        loading.setVisibility(View.VISIBLE);
        Perform.fetchFeesBreakup(context, breakupCallback, feeStructure.getClassId(), feeStructure.getMonthId());
    }

    private void showRestFields() {
        tvDiscount.setText(feeStructure.getDiscount()+"");
        tvTotal.setText(feeStructure.getAmount()+"");
    }

    private void showPopup(){
        alertDialog.show();
    }

    public interface TouchEvents{
        void onPayClick(FeeStructure feeStructure);
    }

}
