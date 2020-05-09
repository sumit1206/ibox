package com.sumit.ibox.model;

public class Teacher {
    String userName;
    String name;
    String email;
    String phone;
    String image_uri;

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public Teacher(String userName, String name, String email, String phone, String image_uri) {
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.image_uri = image_uri;
    }
}
