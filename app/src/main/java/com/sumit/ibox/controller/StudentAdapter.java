package com.sumit.ibox.controller;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sumit.ibox.R;
import com.sumit.ibox.model.StudentsData;

import java.util.List;
import java.util.Random;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    private Context context;
    private List<StudentsData>studentsDataList;
    private TouchHandler touchHandler = null;

    public StudentAdapter(Context context, List<StudentsData> studentsDataList, TouchHandler touchHandler) {
        this.context = context;
        this.studentsDataList = studentsDataList;
        this.touchHandler = touchHandler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_students_card, parent, false);
        return new StudentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        StudentsData studentsData = studentsDataList.get(position);
        int randomAndroidColor = holder.androidColors[new Random().nextInt(holder.androidColors.length)];
        holder.students_card_view_line.setBackgroundColor(randomAndroidColor);
//        holder.studentThumbnail.setImageResource(R.drawable.avater);
//        holder.studentClass.setText(studentsData.getStudentClass());
        holder.studentSection.setText(studentsData.getStudentsRoll());
        holder.studentName.setText(studentsData.getStudentsName());
        holder.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentsData studentsData = studentsDataList.get(position);
                touchHandler.onClick(studentsData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentsDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView studentThumbnail, sendMessage;
        TextView studentName,studentSection,studentClass;
        int[] androidColors;
        View students_card_view_line;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            androidColors = itemView.getResources().getIntArray(R.array.androidcolors);
            studentThumbnail = itemView.findViewById(R.id.students_thumbnail);
            studentName = itemView.findViewById(R.id.student_name);
            studentSection = itemView.findViewById(R.id.students_section);
            studentClass = itemView.findViewById(R.id.student_class);
            students_card_view_line = itemView.findViewById(R.id.students_card_view_line);
            sendMessage = itemView.findViewById(R.id.student_message);
        }
    }

    public interface TouchHandler{
        void onClick(StudentsData studentsData);
    }
}
