package com.sumit.ibox.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sumit.ibox.R;
import com.sumit.ibox.model.MyBookLibraryData;

import java.util.List;

public class MyBookLibraryAdapter extends RecyclerView.Adapter<MyBookLibraryAdapter.ViewHolder> {

    private Context context;
    private List<MyBookLibraryData> myBookLibraryDataList;

    public MyBookLibraryAdapter(Context context, List<MyBookLibraryData> myBookLibraryDataList) {
        this.context = context;
        this.myBookLibraryDataList = myBookLibraryDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_library_card, parent, false);
        return new MyBookLibraryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyBookLibraryData myBookLibraryData = myBookLibraryDataList.get(position);
        holder.authorName.setText(myBookLibraryData.getAuthorLibrary());
        holder.bookName.setText(myBookLibraryData.getBookNName());
        holder.issueDateLib.setText(myBookLibraryData.getIssueDateLib());
        holder.submissionDateLib.setText(myBookLibraryData.submissionDateLib);
    }

    @Override
    public int getItemCount() {
        return myBookLibraryDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookName,authorName,issueDateLib,submissionDateLib;
        int[] androidColors;
        View library_card_view_line;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            androidColors = itemView.getResources().getIntArray(R.array.androidcolors);
            bookName = itemView.findViewById(R.id.book_name);
            authorName = itemView.findViewById(R.id.author_name);
            issueDateLib = itemView.findViewById(R.id.issueDateLib);
            submissionDateLib = itemView.findViewById(R.id.submissionDateLib);
            library_card_view_line = itemView.findViewById(R.id.library_card_view_line);
        }
    }
}
