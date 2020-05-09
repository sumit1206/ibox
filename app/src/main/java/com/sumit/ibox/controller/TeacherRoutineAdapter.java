package com.sumit.ibox.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sumit.ibox.R;
import com.sumit.ibox.model.TeacherRoutineData;

import java.util.List;

public class TeacherRoutineAdapter extends RecyclerView.Adapter<TeacherRoutineAdapter.MyViewHolder> {

    private Context teacherContext;
    private List<TeacherRoutineData> teacherRoutineDataList;

    public TeacherRoutineAdapter(Context teacherContext, List<TeacherRoutineData> teacherRoutineDataList) {
        this.teacherContext = teacherContext;
        this.teacherRoutineDataList = teacherRoutineDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teacher_custom_routine_card, parent, false);
        return new TeacherRoutineAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TeacherRoutineData teacherRoutineData = teacherRoutineDataList.get(position);
        holder.class_routine_start_time.setText(teacherRoutineData.getClass_routine_start_time());
        holder.teacher_subject_name_routine.setText(teacherRoutineData.getTeacher_subject_name_routine());
        holder.teacher_class_routine.setText(teacherRoutineData.getTeacher_class_routine());
        holder.teacher_section_routine.setText(teacherRoutineData.getTeacher_section_routine());
        holder.class_routine_end_time.setText(teacherRoutineData.getClass_routine_end_time());
    }

    @Override
    public int getItemCount() {
        return teacherRoutineDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView class_routine_start_time,teacher_subject_name_routine,teacher_class_routine,teacher_section_routine,class_routine_end_time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            class_routine_start_time = itemView.findViewById(R.id.class_routine_start_time);
            teacher_subject_name_routine = itemView.findViewById(R.id.teacher_subject_name_routine);
            teacher_class_routine = itemView.findViewById(R.id.teacher_class_routine);
            teacher_section_routine = itemView.findViewById(R.id.teacher_section_routine);
            class_routine_end_time = itemView.findViewById(R.id.class_routine_end_time);
        }
    }
}
