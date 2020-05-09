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
import com.sumit.ibox.model.RequestBookLibraryData;

import java.util.ArrayList;
public class RequestBookLibraryAdapter extends RecyclerView.Adapter<RequestBookLibraryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<RequestBookLibraryData> requestBookLibrariesList;
    TouchEvents touchEvents = null;

    public RequestBookLibraryAdapter(Context context, ArrayList<RequestBookLibraryData> requestBookLibrariesList, TouchEvents touchEvents) {
        this.context = context;
        this.requestBookLibrariesList = requestBookLibrariesList;
        this.touchEvents = touchEvents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_request_book_library_card, parent, false);
        return new RequestBookLibraryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        RequestBookLibraryData requestBookLibraryData = requestBookLibrariesList.get(position);
        holder.reqBookAuthorName.setText(requestBookLibraryData.getBookAuthor());
        holder.requestBookName.setText(requestBookLibraryData.getBookName());
        holder.requestBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBookLibraryData requestBookLibraryData = requestBookLibrariesList.get(position);
                touchEvents.onClick(requestBookLibraryData);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestBookLibrariesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView requestBookName,reqBookAuthorName;
        ImageView requestBook;
        int[] androidColors;
        View library_card_view_line;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            requestBookName = itemView.findViewById(R.id.req_book_lib_author);
            reqBookAuthorName = itemView.findViewById(R.id.req_book_lib_name);
            requestBook = itemView.findViewById(R.id.req_book_send_request);
        }
    }

    public interface TouchEvents{
        void onClick(RequestBookLibraryData bookData);
    }
}
