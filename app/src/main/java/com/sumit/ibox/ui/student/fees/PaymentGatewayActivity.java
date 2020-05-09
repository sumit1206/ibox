package com.sumit.ibox.ui.student.fees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.paytm.pgsdk.PaytmConstants;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Hashids;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.paytm.CRED;
import com.sumit.ibox.paytm.JSONParserPaytm;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class PaymentGatewayActivity extends AppCompatActivity  implements PaytmPaymentTransactionCallback {

    Context context;
    private static final int THRESHOLD_PING = 500;//ms
    int amount, fine, discount;
    String monthList, classId, monthNameList;
    String studentId, txnId;
    String username;
    ProgressDialog loading;
    AlertDialog errorDialog;
    Button btnPay;

    LinearLayout llInvoice;
    TextView tvDate, tvCustomerId, tvOrderId, tvTransactionId, tvBankName, tvPaidAgainst,
            tvCurrency, tvTotal, tvFine, tvDiscount, tvAmountPaid;

    VolleyCallback paymentCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            loading.dismiss();
            String msg = getString(R.string.update_success);
            showAlert(msg);
//            errorDialog.setMessage(getString(R.string.update_success));
//            errorDialog.show();
        }

        @Override
        public void noDataFound() {
            loading.dismiss();
            String msg = "Payment SUCCESSFUL\nBut not updated due to some issue\nProvide a copy of the invoice in School";
            showAlert(msg);
//            errorDialog.setMessage(getString(R.string.update_failed));
//            errorDialog.show();
        }

        @Override
        public void onCatch(JSONException e) {
            loading.dismiss();
            Utils.logPrint(getClass(),"response error2", Log.getStackTraceString(e));
            String msg = "Payment SUCCESSFUL\nBut not updated due to some issue\nProvide a copy of the invoice in School";
            showAlert(msg);
//            errorDialog.setMessage(getString(R.string.error_occurred));
//            errorDialog.show();
        }

        @Override
        public void onError(VolleyError e) {
            loading.dismiss();
            Utils.logPrint(getClass(),"no response error", Log.getStackTraceString(e));
            String msg = "Payment SUCCESSFUL\nBut not updated due to some issue\nProvide a copy of the invoice in School";
            showAlert(msg);
//            errorDialog.setMessage(getString(R.string.error_occurred));
//            errorDialog.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        init();
        setupToolbar();
        catchExtraData();
        createOtherData();
//        TextView textView = findViewById(R.id.amount);
//        textView.setText(amount + ":" + fine + ":" + discount);
    }

    private void init() {
        context = this;
        username = Utils.getString(context, Constant.KEY_USER_ID, Constant.DUMMY);
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
        btnPay = findViewById(R.id.pay_button);

        llInvoice = findViewById(R.id.invoice_layout);
        tvDate = findViewById(R.id.invoice_date);
        tvCustomerId = findViewById(R.id.invoice_customer_id);
        tvOrderId = findViewById(R.id.invoice_order_id);
        tvTransactionId = findViewById(R.id.invoice_transaction_id);
        tvBankName = findViewById(R.id.invoice_bank_name);
        tvCurrency = findViewById(R.id.invoice_currency);
        tvPaidAgainst = findViewById(R.id.invoice_paid_against);
        tvTotal = findViewById(R.id.invoice_total_amount);
        tvFine = findViewById(R.id.invoice_fine);
        tvDiscount = findViewById(R.id.invoice_discount);
        tvAmountPaid = findViewById(R.id.invoice_amount_paid);
    }

    private void setupToolbar(){
        Toolbar feesToolbar;
        feesToolbar = findViewById(R.id.fees_toolbar);
        feesToolbar.setTitle(R.string.pay);
        feesToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        feesToolbar.setNavigationIcon(R.drawable.arrow_left);
        feesToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void catchExtraData() {
        Intent intent = getIntent();
        amount = intent.getIntExtra(Constant.KEY_FEES_AMOUNT, 0);
//        amount = 1;
        fine = intent.getIntExtra(Constant.KEY_FEES_FINE, 0);
        discount = intent.getIntExtra(Constant.KEY_FEES_DISCOUNT, 0);
        classId = intent.getStringExtra(Constant.KEY_FEES_CLASS_ID);
        monthList = intent.getStringExtra(Constant.KEY_FEES_MONTH_LIST);
        monthNameList = intent.getStringExtra(Constant.KEY_FEES_MONTH_NAME_LIST);
        Utils.logPrint(getClass(),"monthList2",monthList);
    }

    private void createOtherData() {
        studentId = Utils.getString(context, Constant.KEY_STUDENT_ID, Constant.DUMMY);
        long now = System.currentTimeMillis();
        txnId = Hashids.hash12(studentId + now);
        txnId = "IBOX_" + txnId;
    }

    private boolean isInternetConnectionStrong() {
        loading.show();
        Utils.logPrint(getClass(), "checking", "latency");
        double latency = Utils.getLatency();
        loading.dismiss();
        Utils.logPrint(getClass(), "latency", latency+" ms");
        return latency < THRESHOLD_PING;
    }

    public void payNowClick(View view) {
        boolean internetConnectionStrong = isInternetConnectionStrong();
        if(internetConnectionStrong) {
            view.setVisibility(View.GONE);
            PaytmTransactionService paytmTransactionService = new PaytmTransactionService();
            paytmTransactionService.execute();
        }else {
            String msg = "Your internet is too weak to enter payment gateway";
            showAlert(msg);
        }
    }

    private class PaytmTransactionService extends AsyncTask<ArrayList<String>, Void, String> {
        private android.app.ProgressDialog dialog = new android.app.ProgressDialog(context);
        String url = Constant.URL_GENERATE_CHECKSUM;
        String mid = CRED.PAYTM_MID;
        String orderId = txnId;
        String callBackUrl = CRED.PAYTM_CALLBACK_URL+orderId;
        String customerId = username;
        String CHECKSUMHASH ="";
        String amt = amount + "";

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {
            JSONParserPaytm jsonParser = new JSONParserPaytm(context);
            String param=
                    "MID="+mid+
                            "&ORDER_ID=" + orderId+
                            "&CUST_ID="+customerId+
                            "&CHANNEL_ID=WAP&TXN_AMOUNT="+amount+
                            "&WEBSITE=WEBSTAGING"+
                            "&CALLBACK_URL="+ callBackUrl+"&INDUSTRY_TYPE_ID=Retail";
            JSONObject jsonObject = jsonParser.makeHttpRequest(url,"POST",param);

            if(jsonObject != null){
                Utils.logPrint(getClass(), "JSON RESPONSE", jsonObject.toString());
                try {
                    CHECKSUMHASH=jsonObject.has("CHECKSUMHASH")?jsonObject.getString("CHECKSUMHASH"):"";
                    Utils.logPrint(getClass(), "CHECKSUMHASH", CHECKSUMHASH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                Utils.logPrint(getClass(), "JSON RESPONSE", "null");
            }
            return CHECKSUMHASH;
        }

        @Override
        protected void onPostExecute(String result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            PaytmPGService Service = PaytmPGService.getProductionService();
            HashMap<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("MID", mid); //MID provided by paytm
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", customerId);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", amt);
            paramMap.put("WEBSITE", "WEBSTAGING");
            paramMap.put("CALLBACK_URL" ,callBackUrl);
            paramMap.put("CHECKSUMHASH" ,CHECKSUMHASH);
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
            PaytmOrder Order = new PaytmOrder(paramMap);

            Utils.logPrint(getClass(), "PARAMS", paramMap.toString());
            Service.initialize(Order,null);
            // start payment service call here
            Service.startPaymentTransaction(context, true, true,
                    PaymentGatewayActivity.this  );
        }
    }

    @Override
    public void onTransactionResponse(Bundle bundle) {
        //[{STATUS=TXN_SUCCESS, CHECKSUMHASH=mR677r7VcXpEsQ7FNMlxqp0ziRYT5JM3adIPnKq83hKqvfa35NnTAu48rbDpTPfSiHUe68oRTZDVCHtlo8gSXvFdVM891H9iTJtDVsK4LmA=,
        // BANKNAME=State Bank of India, ORDERID=APP_31xWwhBiWxWQ, TXNAMOUNT=1.00, TXNDATE=2020-03-06 16:14:18.0, MID=TreGqr85522609535577,
        // TXNID=20200306111212800110168142717664110, RESPCODE=01, PAYMENTMODE=DC, BANKTXNID=68653443410, CURRENCY=INR, GATEWAYNAME=ICICIPAY, RESPMSG=Txn Success}]
        Utils.logPrint(getClass(), "Bundle", bundle.toString());
        String STATUS = bundle.getString(PaytmConstants.STATUS);
        Utils.logPrint(getClass(), "STATUS", STATUS);
        if(STATUS.equalsIgnoreCase("TXN_SUCCESS")){
            turnOffPaid();
            afterPayment();
            showInvoice(bundle);
        } else if(STATUS.equalsIgnoreCase("TXN_FAILURE")){
            turnOnRetry();
            String msg = "Payment failed";
            showAlert(msg);
        } else{
            turnOnRetry();
            String msg = "Unexpected gateway response";
            showAlert(msg);
        }
    }

    @Override
    public void networkNotAvailable() {
        turnOnRetry();
        String msg = "Internet unavailable";
        showAlert(msg);
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        turnOnRetry();
        String msg = "School authentication failed";
        showAlert(msg);
    }

    @Override
    public void someUIErrorOccurred(String s) {
        turnOnRetry();
        String msg = "Unexpected UI error";
        showAlert(msg);
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        turnOnRetry();
        String msg = "Error loading gateway";
        showAlert(msg);
    }

    @Override
    public void onBackPressedCancelTransaction() {
        turnOnRetry();
        String msg = "Transaction cancelled by you";
        showAlert(msg);
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        turnOnRetry();
        String msg = "Transaction cancelled by you";
        showAlert(msg);
    }

    private void showAlert(String msg) {
        errorDialog.setMessage(msg);
        errorDialog.show();
    }

    private void turnOffPaid() {
//        btnPay.setText(getString(R.string.paid));
//        btnPay.setClickable(false);
        btnPay.setVisibility(View.GONE);
    }

    private void turnOnRetry() {
        btnPay.setText(getString(R.string.retry));
        btnPay.setVisibility(View.VISIBLE);
    }

    private void afterPayment() {
        loading.show();
        Perform.paymentUpdate(context, paymentCallback, studentId, monthList,classId, txnId,
                amount+"", fine+"", discount+"");
    }

    private void showInvoice(Bundle bundle) {
        llInvoice.setVisibility(View.VISIBLE);
        tvDate.setText(bundle.getString(PaytmConstants.TRANSACTION_DATE));
        tvOrderId.setText(bundle.getString(PaytmConstants.ORDER_ID));
        tvTransactionId.setText(bundle.getString(PaytmConstants.TRANSACTION_ID));
        tvBankName.setText(bundle.getString(PaytmConstants.BANK_NAME));
        tvAmountPaid.setText(bundle.getString(PaytmConstants.TRANSACTION_AMOUNT));
        tvCurrency.setText("INR");

        tvCustomerId.setText(username);
        int total = (amount + fine) - discount;
        tvTotal.setText(total+".00");
        tvFine.setText(fine+".00");
        tvDiscount.setText(discount+".00");
        tvPaidAgainst.setText(monthNameList.replaceAll(",","\n"));
    }

    public void shareClick(View view) {
        shareScreenshot(llInvoice);
    }

    public void saveClick(View view) {
        saveScreenshot(llInvoice);
    }

    public void saveScreenshot(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        String name = "Invoice_"+System.currentTimeMillis()+".jpg";
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,
                    name, "Your payment invoice");
            Utils.showShortToast(context, "saved");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void shareScreenshot(View view){
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        try {
            long now = System.currentTimeMillis();
            File file = new File(this.getExternalCacheDir(),now+".png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share result via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}