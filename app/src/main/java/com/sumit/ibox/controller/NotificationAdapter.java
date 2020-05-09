package com.sumit.ibox.controller;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.model.NotificationData;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    public List<NotificationData> notificationDataList;
    Context context;

    public NotificationAdapter(List<NotificationData> notificationDataList, Context context) {
        this.notificationDataList = notificationDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_notification_card, parent, false);
        return new NotificationAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationData notificationData = notificationDataList.get(position);
//        holder.notificationTitle.setText(notificationData.getNotificationTitle());
        Utils.setHtmlText(holder.notificationTitle, notificationData.getNotificationTitle());
        holder.notificationDateTime.setText(notificationData.getNotificationDate());
        holder.notificationThumb.setImageResource(getNotificationThumb(notificationData.getNotificationSender()));
    }

    private int getNotificationThumb(int notificationSender) {
        if(notificationSender == Constant.TYPE_ADMIN) {
            return R.drawable.ic_notifications_black_24dp;
        }else if(notificationSender == Constant.TYPE_PARENT) {
            return R.drawable.ic_notifications_black_24dp;
        }else if(notificationSender == Constant.TYPE_TEACHER) {
            return R.drawable.ic_notifications_black_24dp;
        }else if(notificationSender == Constant.TYPE_OTHERS) {
            return R.drawable.ic_notifications_black_24dp;
        }else{
            return R.drawable.ic_notifications_black_24dp;
        }
    }

    @Override
    public int getItemCount() {
        return notificationDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView notificationTitle, notificationDateTime;
        ImageView notificationThumb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            notificationTitle = itemView.findViewById(R.id.notification_title);
            notificationDateTime = itemView.findViewById(R.id.notification_date_time);
            notificationThumb = itemView.findViewById(R.id.notification_thumb);
        }
    }
}
