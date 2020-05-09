package com.sumit.ibox.model;

public class NotesData {

    String subjectName;
    String subjectTitle;
    String teachersName;
    String publishDate;
    String notesLink;

    public NotesData(String subjectName, String subjectTitle, String teachersName, String publishDate, String notesLink) {
        this.subjectName = subjectName;
        this.subjectTitle = subjectTitle;
        this.teachersName = teachersName;
        this.publishDate = publishDate;
        this.notesLink = notesLink;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getTeachersName() {
        return teachersName;
    }

    public void setTeachersName(String teachersName) {
        this.teachersName = teachersName;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getNotesLink() {
        return notesLink;
    }

}
