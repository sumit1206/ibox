package com.sumit.ibox.controller;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sumit.ibox.R;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.model.FeesBreakupData;

import java.util.List;

public class FeesBreakupAdapter extends ArrayAdapter<FeesBreakupData> {

    Context context;
    int resource;
    List<FeesBreakupData> objects;

    public FeesBreakupAdapter(@NonNull Context context, int resource, @NonNull List<FeesBreakupData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }


    public static class ViewHolder{
        TextView tvName, tvAmount;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);
        ViewHolder viewHolder = new ViewHolder();
        FeesBreakupData data = objects.get(position);
        Utils.logPrint(getClass(),"adapter",data.getAmount()+data.getName());
        viewHolder.tvName = convertView.findViewById(R.id.fees_popup_list_name);
        viewHolder.tvName.setText(data.getName());
        viewHolder.tvAmount = convertView.findViewById(R.id.fees_popup_list_amount);
        viewHolder.tvAmount.setText(data.getAmount());
        return convertView;
    }
}
