package com.sumit.ibox.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sumit.ibox.R;
import com.sumit.ibox.model.ResultSubjectWiseData;

import java.util.List;

public class ResultSubjectWiseAdapter extends RecyclerView.Adapter<ResultSubjectWiseAdapter.SubjectWiseViewHolder> {

    private Context context;
    private List<ResultSubjectWiseData> resultSubjectWiseDataList;

    public ResultSubjectWiseAdapter(Context context, List<ResultSubjectWiseData> resultSubjectWiseDataList) {
        this.context = context;
        this.resultSubjectWiseDataList = resultSubjectWiseDataList;
    }

    @NonNull
    @Override
    public SubjectWiseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_result_details_card, parent, false);
        return new ResultSubjectWiseAdapter.SubjectWiseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectWiseViewHolder holder, int position) {

        ResultSubjectWiseData resultSubjectWiseData = resultSubjectWiseDataList.get(position);
        holder.subjectNameResult.setText(resultSubjectWiseData.getSubject_name_result());
        holder.totalMarksResult.setText(resultSubjectWiseData.getTotal_marks_result());
        holder.obtainedMarksResult.setText(resultSubjectWiseData.getObtained_marks_result());
        holder.studentsGradeResult.setText(resultSubjectWiseData.getStudents_grade_result());
    }

    @Override
    public int getItemCount() {
        return resultSubjectWiseDataList.size();
    }

    public class SubjectWiseViewHolder extends RecyclerView.ViewHolder {
        TextView subjectNameResult, totalMarksResult, obtainedMarksResult, studentsGradeResult;

        public SubjectWiseViewHolder(@NonNull View itemView) {
            super(itemView);

            subjectNameResult = itemView.findViewById(R.id.subject_name_result);
            totalMarksResult = itemView.findViewById(R.id.total_marks_result);
            obtainedMarksResult = itemView.findViewById(R.id.obtained_marks_result);
            studentsGradeResult = itemView.findViewById(R.id.grade_result);
        }
    }
}
