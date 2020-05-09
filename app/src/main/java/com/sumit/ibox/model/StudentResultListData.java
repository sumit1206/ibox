package com.sumit.ibox.model;

import java.io.Serializable;

public class StudentResultListData implements Serializable
{
    String examName;
    String percent;
    String grade;
    String total;
    String date;

    public String getExamName() {
        return examName;
    }

    public String getPercent() {
        return percent;
    }

    public String getGrade() {
        return grade;
    }

    public String getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }

    public StudentResultListData(String examName, String percent, String grade, String total, String date) {
        this.examName = examName;
        this.percent = percent;
        this.grade = grade;
        this.total = total;
        this.date = date;
    }
}
