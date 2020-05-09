package com.sumit.ibox.local;

public class CookiesAttribute {
    /**
     * CREATE TABLE STUDENT (
     *     _id        INTEGER PRIMARY KEY AUTOINCREMENT
     *                        NOT NULL,
     *     student_id,
     *     name,
     *     roll,
     *     class,
     *     section,
     *     image_uri
     * );
     * */
    public static String TABLE_STUDENT = "STUDENT";
    public static String student_id = "student_id";
    public static String student_name = "name";
    public static String student_roll = "roll";
    public static String student_class = "class";
    public static String student_section = "section";
    public static String student_image_uri = "image_uri";

    /**
     * CREATE TABLE TEACHER (
     *     _id       INTEGER PRIMARY KEY AUTOINCREMENT
     *                       NOT NULL,
     *     name,
     *     email,
     *     phone,
     *     image_uri
     * );
     * */
    public static String TABLE_TEACHER = "TEACHER";
    public static String teacher_name = "name";
    public static String teacher_email = "email";
    public static String teacher_phone = "phone";
    public static String teacher_image_uri = "image_uri";
}
