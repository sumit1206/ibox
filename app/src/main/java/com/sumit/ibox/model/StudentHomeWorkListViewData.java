package com.sumit.ibox.model;

import android.os.Parcelable;

import java.io.Serializable;

public class StudentHomeWorkListViewData implements Serializable {

    String teacherId;
    String subject;
    String title;
    String description;
    String hwDate;
    String submissionDate;
    String image_link;

    public String getTeacherId() {
        return teacherId;
    }

    public String getSubject() {
        return subject;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getHwDate() {
        return hwDate;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public String getImage_link() {
        return image_link;
    }

    public StudentHomeWorkListViewData(String teacherId, String subject, String title,
                                       String description, String hwDate, String submissionDate, String image_link) {
        this.teacherId = teacherId;
        this.subject = subject;
        this.title = title;
        this.description = description;
        this.hwDate = hwDate;
        this.submissionDate = submissionDate;
        this.image_link = image_link;
    }
}
