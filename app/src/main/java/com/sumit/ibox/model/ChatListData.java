package com.sumit.ibox.model;

import java.io.Serializable;

public class ChatListData implements Serializable {
    private String receiverName;
    private String threadId;
    private String dateTime;
    private int senderType;
    private int hisType;

    public String getReceiverName() {
        return receiverName;
    }

    public String getThreadId() {
        return threadId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getSenderType() {
        return senderType;
    }

    public int getHisType() {
        return hisType;
    }

    public ChatListData(String receiverName, String threadId, String dateTime, int senderType, int hisType) {
        this.receiverName = receiverName;
        this.threadId = threadId;
        this.dateTime = dateTime;
        this.senderType = senderType;
        this.hisType = hisType;
    }
}
