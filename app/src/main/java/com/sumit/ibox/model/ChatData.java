package com.sumit.ibox.model;

public class ChatData {
    private int msgType;
    private String msgDateTime;
    private String msg;
    private int receiverType;

    public int getMsgType() {
        return msgType;
    }

    public String getMsgDateTime() {
        return msgDateTime;
    }

    public String getMsg() {
        return msg;
    }

    public int getReceiverType() {
        return receiverType;
    }

    public ChatData(int msgType, String msgDateTime, String msg, int receiverType) {
        this.msgType = msgType;
        this.msgDateTime = msgDateTime;
        this.msg = msg;
        this.receiverType = receiverType;
    }
}
