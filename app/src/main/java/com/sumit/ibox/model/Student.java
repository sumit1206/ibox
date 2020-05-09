package com.sumit.ibox.model;

public class Student {
    String id;
    String name;
    String roll;
    String klass;
    String section;
    String imageUri;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRoll() {
        return roll;
    }

    public String getKlass() {
        return klass;
    }

    public String getSection() {
        return section;
    }

    public String getImageUri() {
        return imageUri;
    }

    public Student(String id, String name, String roll, String klass, String section, String imageUri) {
        this.id = id;
        this.name = name;
        this.roll = roll;
        this.klass = klass;
        this.section = section;
        this.imageUri = imageUri;
    }
}
