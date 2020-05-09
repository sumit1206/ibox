package com.sumit.ibox.model;

public class StudentsData {
    String studentsName;
    String studentClass;
    String studentsSection;
    String studentsRoll;
    String parentId;
    String studentId;

    public String getStudentsName() {
        return studentsName;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public String getStudentsSection() {
        return studentsSection;
    }

    public String getStudentsRoll() {
        return studentsRoll;
    }

    public String getParentId() {
        return parentId;
    }

    public StudentsData(String studentsName, String studentClass, String studentsSection, String studentsRoll, String parentId, String studentId) {
        this.studentsName = studentsName;
        this.studentClass = studentClass;
        this.studentsSection = studentsSection;
        this.studentsRoll = studentsRoll;
        this.parentId = parentId;
        this.studentId = studentId;
    }
}
