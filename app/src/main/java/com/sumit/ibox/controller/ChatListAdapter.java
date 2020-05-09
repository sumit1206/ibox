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
import com.sumit.ibox.model.ChatListData;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    private Context chatContext;
    private List<ChatListData> chatListDataArray;
    private TouchHandler touchHandler = null;

    public ChatListAdapter(Context chatContext, List<ChatListData> chatListDataArray, TouchHandler touchHandler) {
        this.chatContext = chatContext;
        this.chatListDataArray = chatListDataArray;
        this.touchHandler = touchHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_chat_list_card, parent, false);
        return new ChatListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ChatListData chatListData = chatListDataArray.get(position);
        holder.senderName.setText(chatListData.getReceiverName());
        holder.senderThumb.setImageResource(getThumb());
        if(touchHandler != null){
            holder.senderName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatListData chatListData = chatListDataArray.get(position);
                    touchHandler.onClick(chatListData);
                }
            });
        }
    }

    private int getThumb() {
        return R.drawable.avater;
    }

    @Override
    public int getItemCount() {
        return chatListDataArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView senderName;
        ImageView senderThumb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            senderName = itemView.findViewById(R.id.sender_name);
            senderThumb = itemView.findViewById(R.id.sender_thumbnail);
        }
    }

    public interface TouchHandler{
        void onClick(ChatListData chatListData);
    }
}
