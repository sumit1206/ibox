package com.sumit.ibox.model;

public class UploadAttandanceStudentListData
{
    int studentImage;
    String studentName;
    String studentClass;
    String studentDiv;
    String studentRoll;
    String studentId;
    int attendence;

    public int getStudentImage() {
        return studentImage;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public String getStudentDiv() {
        return studentDiv;
    }

    public String getStudentRoll() {
        return studentRoll;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getAttendence() {
        return attendence;
    }

    public void setAttendence(int attendence) {
        this.attendence = attendence;
    }

    public UploadAttandanceStudentListData(int studentImage, String studentName, String studentClass, String studentDiv, String studentRoll, String studentId, int attendence) {
        this.studentImage = studentImage;
        this.studentName = studentName;
        this.studentClass = studentClass;
        this.studentDiv = studentDiv;
        this.studentRoll = studentRoll;
        this.studentId = studentId;
        this.attendence = attendence;
    }



}
