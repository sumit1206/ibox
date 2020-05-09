package com.sumit.ibox.ui.changenumber;

import androidx.appcompat.app.AppCompatActivity;

import com.sumit.ibox.model.AlertDialog;
import com.sumit.ibox.model.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;

import org.json.JSONException;

public class ChangeContactNo extends AppCompatActivity {

    private Context context;
    private EditText etContactNo;
    ProgressDialog loading;
    AlertDialog errorDialog;

    VolleyCallback contactCallback = new VolleyCallback() {
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
        setContentView(R.layout.change_contact_no);

        init();

    }

    private void init() {
        context = this;
        etContactNo = findViewById(R.id.change_contact_number);
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

    public void savePressed(View view) {
        String contactNo = etContactNo.getText().toString().trim();
        if(contactNo.length() != 10){
            Utils.showShortToast(context, "Invalid contact number");
            return;
        }
        String username = Utils.getString(context, Constant.KEY_USER_ID, Constant.DUMMY);
        String usertype = Utils.getInt(context, Constant.KEY_USER_TYPE, Constant.TYPE_BOTH)+"";
        loading.show();
        Perform.updateNumber(context, contactCallback, username, usertype, contactNo);
    }
}
