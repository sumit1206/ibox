package com.sumit.ibox.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.model.UploadAttandanceStudentListData;
import com.sumit.ibox.ui.teacher.AttendanceActivity;

import java.util.List;

public class UploadAttandanceStudentListAdapter extends RecyclerView.Adapter<UploadAttandanceStudentListAdapter.StudentViewHolder> {

    private Context context;
    private List<UploadAttandanceStudentListData> uploadAttandanceStudentListArray;

    public UploadAttandanceStudentListAdapter(Context context, List<UploadAttandanceStudentListData> uploadAttandanceStudentList) {
        this.context = context;
        this.uploadAttandanceStudentListArray = AttendanceActivity.uploadAttandanceStudentListarray;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_attendance_card, parent, false);
        return new UploadAttandanceStudentListAdapter.StudentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentViewHolder holder, int position) {
        final UploadAttandanceStudentListData uploadAttandanceStudentListData = uploadAttandanceStudentListArray.get(position);
//        holder.stdentImageAttandanceUpload.setImageResource(uploadAttandanceStudentListData.getStudentImageAttandance());
        holder.stdentNameAttandanceUpload.setText(uploadAttandanceStudentListData.getStudentName());
        holder.stdentclassAttandanceUpload.setText(context.getString(R.string.roll));
        holder.stdentDivAttandanceUpload.setText(uploadAttandanceStudentListData.getStudentRoll());
        holder.attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.attendence.isChecked()){
                    uploadAttandanceStudentListData.setAttendence(Constant.ATTENDENCE_PRESENT);
                }else {
                    uploadAttandanceStudentListData.setAttendence(Constant.ATTENDENCE_ABSENT);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return uploadAttandanceStudentListArray.size();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {

        ImageView stdentImageAttandanceUpload;
        TextView stdentNameAttandanceUpload,stdentclassAttandanceUpload,stdentDivAttandanceUpload;
        CheckBox attendence;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            stdentImageAttandanceUpload =  itemView.findViewById(R.id.upload_attandance_student_image);
            stdentNameAttandanceUpload = itemView.findViewById(R.id.upload_attandance_student_name);
            stdentclassAttandanceUpload = itemView.findViewById(R.id.upload_attandance_student_class);
            stdentDivAttandanceUpload = itemView.findViewById(R.id.upload_attandance_student_div);
            attendence = itemView.findViewById(R.id.attandence_checkbox);
        }
    }
}

