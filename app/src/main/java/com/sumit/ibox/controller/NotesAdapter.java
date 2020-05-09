package com.sumit.ibox.controller;

import android.app.DownloadManager;
import android.content.Context;
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
import com.sumit.ibox.model.NotesData;

import java.util.ArrayList;
import java.util.Random;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    public ArrayList <NotesData> notesDataList;
    Context context;

    public NotesAdapter(Context context, ArrayList<NotesData> notesDataList) {
        this.notesDataList = notesDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_notes_card, parent, false);
        return new NotesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, final int position) {
        NotesData notesData = notesDataList.get(position);
        int randomAndroidColor = holder.androidColors[new Random().nextInt(holder.androidColors.length)];
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(randomAndroidColor);
        holder.thumbnail.setBackground(shape);

        holder.thumbnail.setText(notesData.getSubjectName());
        holder.subjectNameNotes.setText(notesData.getSubjectName());
        holder.titleNotes.setText(notesData.getSubjectTitle());
        holder.teacherNameNotes.setText(notesData.getTeachersName());
        holder.publishDate.setText(notesData.getPublishDate());
        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(notesDataList.get(position).getNotesLink(), notesDataList.get(position).getSubjectName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView subjectNameNotes,titleNotes,teacherNameNotes,publishDate,thumbnail;
        ImageView downloadBtn;
        int[] androidColors;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            androidColors = itemView.getResources().getIntArray(R.array.androidcolors);
            thumbnail = itemView.findViewById(R.id.notes_thumbnail);
            downloadBtn = itemView.findViewById(R.id.notes_download_btn);
            subjectNameNotes = itemView.findViewById(R.id.subjectNameNotes);
            titleNotes = itemView.findViewById(R.id.subjectTitleNotes);
            teacherNameNotes = itemView.findViewById(R.id.teacherNameNotes);
            publishDate = itemView.findViewById(R.id.publishDateNotes);
        }
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
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, subjectName + "_notes_"+session);

            downloadmanager.enqueue(request);
            Utils.showShortToast(context, context.getString(R.string.downloading));
        }catch (Exception e){
            Utils.logPrint(getClass(), "error downloading", Log.getStackTraceString(e));
            Utils.showShortToast(context, context.getString(R.string.download_failed));
        }
    }

}
