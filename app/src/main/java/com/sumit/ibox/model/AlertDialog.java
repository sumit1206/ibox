package com.sumit.ibox.model;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sumit.ibox.R;
public class AlertDialog{

    private android.app.AlertDialog alertDialog;
    private UserInterface userInterface;
    public AlertDialog(android.app.AlertDialog alertDialog, UserInterface userInterface) {
        this.alertDialog = alertDialog;
        this.userInterface = userInterface;
    }

    public void setMessage(CharSequence message) {
        try {
            userInterface.getTvMessage().setText(message);
        }catch (Exception ignored){}
    }

    public void show() {
        try {
            alertDialog.show();
        }catch (Exception ignored){}
    }

    public void cancel() {
        try {
            alertDialog.cancel();
        }catch (Exception ignored){}
    }

    public void dismiss() {
        try {
            alertDialog.dismiss();
        }catch (Exception ignored){}
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        alertDialog.setOnCancelListener(onCancelListener);
    }

//    private void showActivityDialog(Date dat){
//        LayoutInflater li = LayoutInflater.from(context);
//        View view = li.inflate(R.layout.custom_popup_attandance, null);
//        final com.sumit.ibox.model.AlertDialog.Builder alertDialogBuilder = new com.sumit.ibox.model.AlertDialog.Builder(context);
//        alertDialogBuilder.setView(view);
//        final com.sumit.ibox.model.AlertDialog popup = alertDialogBuilder.create();
//        ImageView close = view.findViewById(R.id.application_popup_close);
//        TextView date = view.findViewById(R.id.application_popup_date);
//        TextView monthYear = view.findViewById(R.id.application_popup_month_year);
//        final TextView rectify = view.findViewById(R.id.application_popup_rectify);
//        final TextView leave = view.findViewById(R.id.application_popup_leave);
//        final TextView submit = view.findViewById(R.id.application_popup_submit);
//        final LinearLayout reaonLayout = view.findViewById(R.id.application_popup_reason_layout);
//        final LinearLayout progressLayout = view.findViewById(R.id.application_popup_progress_layout);
//        date.setText(Utils.getDateInFormat(dat,"dd"));
//        monthYear.setText(Utils.getDateInFormat(dat,"MMM yy"));
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popup.dismiss();
//            }
//        });
//        rectify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reaonLayout.setVisibility(View.GONE);
//                submit.setVisibility(View.VISIBLE);
//                submit.setText(R.string.conform);
//            }
//        });
//        leave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reaonLayout.setVisibility(View.VISIBLE);
//                submit.setVisibility(View.VISIBLE);
//                submit.setText(R.string.submit);
//            }
//        });
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reaonLayout.setVisibility(View.GONE);
//                progressLayout.setVisibility(View.VISIBLE);
//                submit.setVisibility(View.INVISIBLE);
//                rectify.setClickable(false);
//                leave.setClickable(false);
//            }
//        });
//        popup.show();
//    }

    public static class Builder{

        private android.app.AlertDialog.Builder alertDialogBuilder;
        private UserInterface userInterface;
        private DialogInterface.OnClickListener dPosClickListener;
        private DialogInterface.OnClickListener dNegClickListener;
        private android.app.AlertDialog alertDialog;
        private View.OnClickListener vPosClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                dPosClickListener.onClick(null, Integer.MAX_VALUE);
            }
        };
        private View.OnClickListener vNegClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                dNegClickListener.onClick(null, Integer.MAX_VALUE);
            }
        };

        public Builder(Context context) {
            alertDialogBuilder = new android.app.AlertDialog.Builder(context);
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(context);
            View view;
            view = layoutInflater.inflate(R.layout.cuatom_alert_dialog, null);
            TextView tvMessage, tvPosBtn, tvNegBtn;
            tvMessage = view.findViewById(R.id.alert_dialog_message);
            tvPosBtn = view.findViewById(R.id.alert_dialog_positive_button);
            tvNegBtn = view.findViewById(R.id.alert_dialog_negative_button);
            userInterface = new UserInterface(tvMessage, tvPosBtn, tvNegBtn);
            alertDialogBuilder.setView(view);
        }

        public Builder(Context context, int themeResId) {
            new Builder(context);
        }

        public Builder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
            this.dPosClickListener = listener;
            userInterface.getTvPosBtn().setText(text);
            userInterface.getTvPosBtn().setVisibility(View.VISIBLE);
            if(listener != null)
                userInterface.getTvPosBtn().setOnClickListener(vPosClickListener);
            return this;
        }

        public Builder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
            this.dNegClickListener = listener;
            userInterface.getTvNegBtn().setText(text);
            userInterface.getTvNegBtn().setVisibility(View.VISIBLE);
            if(listener != null)
                userInterface.getTvNegBtn().setOnClickListener(vNegClickListener);
            return this;
        }

        public Builder setMessage(String string) {
            userInterface.getTvMessage().setText(string);
            return this;
        }

        public AlertDialog create() {
            alertDialog = alertDialogBuilder.create();
            return new AlertDialog(alertDialog, userInterface);
        }

        public Builder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            alertDialogBuilder.setOnCancelListener(onCancelListener);
            return this;
        }
    }

    private static class UserInterface{
        private TextView tvMessage, tvPosBtn, tvNegBtn;

        UserInterface(TextView tvMessage, TextView tvPosBtn, TextView tvNegBtn) {
            this.tvMessage = tvMessage;
            this.tvPosBtn = tvPosBtn;
            this.tvNegBtn = tvNegBtn;
        }

        TextView getTvMessage() {
            return tvMessage;
        }

        TextView getTvPosBtn() {
            return tvPosBtn;
        }

        TextView getTvNegBtn() {
            return tvNegBtn;
        }
    }
}
