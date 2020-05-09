package com.sumit.ibox.controller;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.model.FeeStructure;
import java.util.List;
import java.util.Random;

public class FeesDewListAdapter extends RecyclerView.Adapter<FeesDewListAdapter.MyViewHolder> {

    TouchEvents touchEvents = null;
    private Context studentContext;
    private List<FeeStructure> feeStructureArray;

    public FeesDewListAdapter(Context studentContext, List<FeeStructure> feeStructureArray, TouchEvents touchEvents) {
        this.studentContext = studentContext;
        this.feeStructureArray = feeStructureArray;
        this.touchEvents = touchEvents;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_fees_dew_card, parent, false);
        return new FeesDewListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final FeeStructure feeStructure = feeStructureArray.get(position);

        int randomAndroidColor = holder.androidColors[new Random().nextInt(holder.androidColors.length)];
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(randomAndroidColor);

        holder.feesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeeStructure feeStructure = feeStructureArray.get(position);
                if(touchEvents != null){
                    touchEvents.onClick(feeStructure);
                }
            }
        });
        holder.viewLine.setBackgroundColor(randomAndroidColor);
        holder.feesType.setText(feeStructure.getMonth());
        holder.feesAmount.setText(feeStructure.getAmount()+"");
        holder.dewLastDate.setText(feeStructure.getLastDate());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeeStructure feeStructure = feeStructureArray.get(position);
                feeStructure.setSelected(holder.checkBox.isChecked());
//                Utils.logPrint(getClass(),feeStructureArray.get(position).getMonth(),feeStructureArray.get(position).isSelected()+"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return feeStructureArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout feesLayout;
        TextView feesType, feesAmount, dewLastDate;
        CheckBox checkBox;
        View viewLine;
        int[] androidColors;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            feesLayout = itemView.findViewById(R.id.fees_layout);
            androidColors = itemView.getResources().getIntArray(R.array.androidcolors);
            feesType = itemView.findViewById(R.id.fees_type);
            feesAmount = itemView.findViewById(R.id.dew_fees_amount);
            dewLastDate = itemView.findViewById(R.id.fees_payment_last_date);
            viewLine = itemView.findViewById(R.id.view_line_color);
            checkBox = itemView.findViewById(R.id.fees_checkbox);
        }
    }

    public  interface TouchEvents{
        void onClick(FeeStructure feeStructure);
    }
}
