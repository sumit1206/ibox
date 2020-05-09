package com.sumit.ibox.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.model.StudentResultListData;
import com.sumit.ibox.ui.student.result.ResultDetailsView;

import java.util.List;
import java.util.Random;

public class StudentResultListAdapter extends RecyclerView.Adapter <StudentResultListAdapter.MyViewHolder> {

    private Context studentContext;
    private List<StudentResultListData> studentResultListDataArray;

    public StudentResultListAdapter(Context studentContext, List<StudentResultListData> studentResultListDataArray) {
        this.studentContext = studentContext;
        this.studentResultListDataArray = studentResultListDataArray;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_result_card, parent, false);
        return new StudentResultListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        StudentResultListData studentResultListData = studentResultListDataArray.get(position);

        int randomAndroidColor = holder.androidColors[new Random().nextInt(holder.androidColors.length)];
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(randomAndroidColor);
        holder.percentageResult.setBackground(shape);
        holder.viewLine.setBackgroundColor(randomAndroidColor);

        holder.percentageResult.setText(studentResultListData.getPercent()+"%");
        holder.testNameResult.setText(studentResultListData.getExamName());
        holder.publishDateResult.setText(studentResultListData.getDate());
        holder.testNameResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(studentContext, ResultDetailsView.class);
                detailIntent.putExtra(Constant.KEY_RESULT_DATA, studentResultListDataArray.get(position));
                studentContext.startActivity(detailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentResultListDataArray.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView percentageResult, testNameResult, publishDateResult;
        View viewLine;
        int[] androidColors;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            androidColors = itemView.getResources().getIntArray(R.array.androidcolors);
            viewLine = itemView.findViewById(R.id.view_line_color);
            percentageResult = itemView.findViewById(R.id.percentage_of_marks);
            testNameResult = itemView.findViewById(R.id.test_name_result);
            publishDateResult = itemView.findViewById(R.id.publish_date);
        }
    }
}
