package com.sumit.ibox.controller;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.model.SyllabusData;

import java.util.ArrayList;
import java.util.Random;

public class SyllabusAdapter extends RecyclerView.Adapter<SyllabusAdapter.ViewHolder> {

    private ArrayList<SyllabusData> syllabusDataList;
    Context context;

    public SyllabusAdapter(ArrayList<SyllabusData> syllabusData, Context context) {
        this.syllabusDataList = syllabusData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_syllabus_card, parent, false);
        return new SyllabusAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final SyllabusData syllabusData = syllabusDataList.get(position);
        int randomAndroidColor = holder.androidColors[new Random().nextInt(holder.androidColors.length)];
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(randomAndroidColor);
        holder.syllabusThumb.setBackground(shape);

        holder.syllabusThumb.setText(syllabusData.getSubjectName());
        holder.syllabusName.setText(syllabusData.getExamName());
        holder.subjectNameSyllabus.setText(syllabusData.getSubjectName());
        holder.teacherNameSyllabus.setText(syllabusData.getTeacherName());
        holder.publishDateSyllabus.setText(syllabusData.getPublishDate());
        holder.downloadBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(syllabusDataList.get(position).getSyllabusLink(), syllabusDataList.get(position).getSubjectName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return syllabusDataList.size();
    }

    private void downloadFile(String url, String subjectName){
        try {
            DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(Constant.ROOT  + url);

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle(context.getString(R.string.syllabus));
            request.setDescription("Downloading");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            String session = String.valueOf(System.currentTimeMillis());
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, subjectName + "_syllabus_"+session);

            downloadmanager.enqueue(request);
            Utils.showShortToast(context, context.getString(R.string.downloading));
        }catch (Exception e){
            Utils.logPrint(getClass(), "error downloading", Log.getStackTraceString(e));
            Utils.showShortToast(context, context.getString(R.string.download_failed));
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView syllabusThumb,syllabusName,subjectNameSyllabus,teacherNameSyllabus,publishDateSyllabus;
        ImageView downloadBnt;
        int[] androidColors;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            androidColors = itemView.getResources().getIntArray(R.array.androidcolors);
            syllabusThumb = itemView.findViewById(R.id.syllabus_thumb);
            syllabusName = itemView.findViewById(R.id.syllabus_name);
            subjectNameSyllabus = itemView.findViewById(R.id.subjectNameSyllabus);
            teacherNameSyllabus = itemView.findViewById(R.id.teacherNameSyllabus);
            publishDateSyllabus = itemView.findViewById(R.id.publishDateSyllabus);
            downloadBnt = itemView.findViewById(R.id.syllabus_download_btn);
        }
    }
}
