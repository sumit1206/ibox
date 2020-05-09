package com.sumit.ibox.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.model.StudentHomeWorkListViewData;
import com.sumit.ibox.ui.student.homework.HomeWorkDetailsView;

import java.util.List;
import java.util.Random;

public class StudentsHomeWorkListViewAdapter extends RecyclerView.Adapter<StudentsHomeWorkListViewAdapter.StudentViewHolder> {

    private Context context;
    private List<StudentHomeWorkListViewData> homeWorkListViewData;

    public StudentsHomeWorkListViewAdapter(Context context, List<StudentHomeWorkListViewData> homeWorkListViewData) {
        this.context = context;
        this.homeWorkListViewData = homeWorkListViewData;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_home_work_card, parent, false);
        return new StudentsHomeWorkListViewAdapter.StudentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, final int position) {

        StudentHomeWorkListViewData studentHomeWorkListViewData = homeWorkListViewData.get(position);
//        holder.homeWorkImage.setImageResource(studentHomeWorkListViewData.getHomeWorkImage());

        int randomAndroidColor = holder.androidColors[new Random().nextInt(holder.androidColors.length)];
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(randomAndroidColor);
        holder.hwIcon.setBackground(shape);

        holder.hwIcon.setText(studentHomeWorkListViewData.getTitle());
        holder.title.setText(studentHomeWorkListViewData.getTitle());
        holder.description.setText(studentHomeWorkListViewData.getDescription());
        holder.publishDate.setText(studentHomeWorkListViewData.getHwDate());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(context, HomeWorkDetailsView.class);
                detailIntent.putExtra(Constant.KEY_HOMEWORK_DATA, homeWorkListViewData.get(position));
                context.startActivity(detailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeWorkListViewData.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {

        ImageView homeWorkImage;
        TextView title, description, publishDate, hwIcon;
        int[] androidColors;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            androidColors = itemView.getResources().getIntArray(R.array.androidcolors);
            hwIcon = itemView.findViewById(R.id.hw_icon);
            homeWorkImage = itemView.findViewById(R.id.homework_image);
            title = itemView.findViewById(R.id.homework_title);
            description = itemView.findViewById(R.id.homework_description);
            publishDate = itemView.findViewById(R.id.homework_publish_date);
        }
    }
}
