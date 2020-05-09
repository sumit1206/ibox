package com.sumit.ibox.model;

public class ResultSubjectWiseData {

    String subject_name_result;
    String total_marks_result;
    String obtained_marks_result;
    String students_grade_result;

    public ResultSubjectWiseData(String subject_name_result, String total_marks_result, String obtained_marks_result, String students_grade_result) {
        this.subject_name_result = subject_name_result;
        this.total_marks_result = total_marks_result;
        this.obtained_marks_result = obtained_marks_result;
        this.students_grade_result = students_grade_result;
    }

    public String getSubject_name_result() {
        return subject_name_result;
    }

    public String getTotal_marks_result() {
        return total_marks_result;
    }

    public String getObtained_marks_result() {
        return obtained_marks_result;
    }

    public String getStudents_grade_result() {
        return students_grade_result;
    }
}

