package com.sumit.ibox.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sumit.ibox.R;

import java.util.List;
import java.util.Random;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.MyViewHolder> {

    private Context context;
    private List<Teacher> teachers;
    private TouchHandler touchHandler = null;

    public TeacherAdapter(Context context, List<Teacher> teachers, TouchHandler touchHandler) {
        this.context = context;
        this.teachers = teachers;
        this.touchHandler = touchHandler;
    }

    @NonNull
    @Override
    public TeacherAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_teacher_card, parent, false);
        return new TeacherAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherAdapter.MyViewHolder holder, final int position) {
        Teacher teacher = teachers.get(position);
        int randomAndroidColor = holder.androidColors[new Random().nextInt(holder.androidColors.length)];
        holder.teacher_card_view_line.setBackgroundColor(randomAndroidColor);
        holder.teacherMail.setText(teacher.getEmail());
        holder.teacherName.setText(teacher.getName());
        holder.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teacher teacher = teachers.get(position);
                touchHandler.onClick(teacher);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView teacherThumbnail, sendMessage;
        TextView teacherName, teacherMail;
        int[] androidColors;
        View teacher_card_view_line;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            androidColors = itemView.getResources().getIntArray(R.array.androidcolors);
            teacherThumbnail = itemView.findViewById(R.id.teacher_thumbnail);
            teacherName = itemView.findViewById(R.id.teacher_name);
            teacherMail = itemView.findViewById(R.id.teacher_mail_id);
            teacher_card_view_line = itemView.findViewById(R.id.teacher_card_view_line);
            sendMessage = itemView.findViewById(R.id.teacher_message);
        }
    }

    public interface TouchHandler{
        void onClick(Teacher teacher);
    }
}

