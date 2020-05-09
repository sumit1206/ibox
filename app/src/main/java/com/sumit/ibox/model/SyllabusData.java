package com.sumit.ibox.model;

public class SyllabusData {
    String examName;
    String subjectName;
    String teacherName;
    String publishDate;
    String syllabusLink;

    public String getExamName() {
        return examName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getSyllabusLink() {
        return syllabusLink;
    }

    public SyllabusData(String examName, String subjectName, String teacherName, String publishDate, String syllabusLink) {
        this.examName = examName;
        this.subjectName = subjectName;
        this.teacherName = teacherName;
        this.publishDate = publishDate;
        this.syllabusLink = syllabusLink;
    }
}
