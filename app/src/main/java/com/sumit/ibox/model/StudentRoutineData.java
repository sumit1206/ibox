package com.sumit.ibox.model;

import android.content.Context;

import java.util.List;

public class StudentRoutineData  {

    String routineTimes;
    String classSubectName;
    String classTeacherName;

    public StudentRoutineData(String routineTimes, String classSubectName, String classTeacherName) {
        this.routineTimes = routineTimes;
        this.classSubectName = classSubectName;
        this.classTeacherName = classTeacherName;
    }

    public String getRoutineTimes() {
        return routineTimes;
    }

    public String getClassSubectName() {
        return classSubectName;
    }

    public String getClassTeacherName() {
        return classTeacherName;
    }
}
