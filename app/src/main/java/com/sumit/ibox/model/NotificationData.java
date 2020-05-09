package com.sumit.ibox.model;

public class NotificationData {

    String notificationTitle;
    String notificationDate;
    String notificationUrl;
    int notificationSender;

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public String getNotificationDate() {
        return notificationDate;
    }

    public String getNotificationUrl() {
        return notificationUrl;
    }

    public int getNotificationSender() {
        return notificationSender;
    }

    public NotificationData(String notificationTitle, String notificationDate, String notificationUrl, int notificationSender) {
        this.notificationTitle = notificationTitle;
        this.notificationDate = notificationDate;
        this.notificationUrl = notificationUrl;
        this.notificationSender = notificationSender;
    }
}
