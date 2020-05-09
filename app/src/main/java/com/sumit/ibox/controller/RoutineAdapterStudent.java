package com.sumit.ibox.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sumit.ibox.R;
import com.sumit.ibox.model.StudentRoutineData;

import java.util.List;

public class RoutineAdapterStudent extends RecyclerView.Adapter<RoutineAdapterStudent.ViewHolder>
{
    private Context context;
    private List <StudentRoutineData> studentRoutineDataArray;

    public RoutineAdapterStudent(Context context, List<StudentRoutineData> studentRoutineDataArray) {
        this.context = context;
        this.studentRoutineDataArray = studentRoutineDataArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_custom_routine_card, parent, false);
        return new RoutineAdapterStudent.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentRoutineData studentRoutineData = studentRoutineDataArray.get(position);
        holder.routineTime.setText(studentRoutineData.getRoutineTimes());
        holder.classSubjectName.setText(studentRoutineData.getClassSubectName());
        holder.sujectTeacherName.setText(studentRoutineData.getClassTeacherName());
        holder.subjectThumbnal.setImageResource(R.drawable.geography);
    }

    @Override
    public int getItemCount() {
        return studentRoutineDataArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView routineTime, classSubjectName, sujectTeacherName;
        ImageView subjectThumbnal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            routineTime = itemView.findViewById(R.id.routine_time_student);
            classSubjectName = itemView.findViewById(R.id.subject_name_routine_student);
            sujectTeacherName = itemView.findViewById(R.id.teacher_name_student_routine);
            subjectThumbnal = itemView.findViewById(R.id.class_thumb_student_routine);
        }
    }
}
