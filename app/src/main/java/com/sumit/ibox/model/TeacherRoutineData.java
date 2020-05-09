package com.sumit.ibox.model;

public class TeacherRoutineData {
    String class_routine_start_time;
    String teacher_subject_name_routine;
    String teacher_class_routine;
    String teacher_section_routine;
    String class_routine_end_time;

    public TeacherRoutineData(String class_routine_start_time, String teacher_subject_name_routine, String teacher_class_routine, String teacher_section_routine, String class_routine_end_time) {
        this.class_routine_start_time = class_routine_start_time;
        this.teacher_subject_name_routine = teacher_subject_name_routine;
        this.teacher_class_routine = teacher_class_routine;
        this.teacher_section_routine = teacher_section_routine;
        this.class_routine_end_time = class_routine_end_time;
    }

    public String getClass_routine_start_time() {
        return class_routine_start_time;
    }

    public String getTeacher_subject_name_routine() {
        return teacher_subject_name_routine;
    }

    public String getTeacher_class_routine() {
        return teacher_class_routine;
    }

    public String getTeacher_section_routine() {
        return teacher_section_routine;
    }

    public String getClass_routine_end_time() {
        return class_routine_end_time;
    }
}
