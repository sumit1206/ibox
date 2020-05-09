package com.sumit.ibox.local;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.model.Student;
import com.sumit.ibox.model.Teacher;

import java.io.IOException;
import java.util.ArrayList;

public class CookiesAdapter
{
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase cookiesDb;
    private CookiesHelper cookiesHelper;

    public CookiesAdapter(Context context)
    {
        this.mContext = context;
        cookiesHelper = new CookiesHelper(mContext);
    }

    public CookiesAdapter createDatabase() throws SQLException
    {
        try
        {
            Utils.logPrint(getClass(),"ibox_db","creating triggered");
            cookiesHelper.createDataBase();
        }
        catch (IOException e)
        {
            Utils.logPrint(getClass(),"Error creating db",Log.getStackTraceString(e));
//            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public CookiesAdapter openReadable() throws SQLException
    {
        try
        {
            cookiesHelper.openDataBase();
            cookiesHelper.close();
            cookiesDb = cookiesHelper.getReadableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public CookiesAdapter openWritable() throws SQLException
    {
        try
        {
            cookiesHelper.openDataBase();
            cookiesHelper.close();
            cookiesDb = cookiesHelper.getWritableDatabase();
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }


    public void close()
    {
        cookiesHelper.close();
    }

    public String getFromTeacher(String attribute) throws NullPointerException
    {
        try {
            String sql ="SELECT " + attribute + " FROM " + CookiesAttribute.TABLE_TEACHER;

            Cursor mCur = cookiesDb.rawQuery(sql, null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
//            Utils.logPrint(getClass(),"QUERY",sql);
            try {
                return mCur.getString(0);
            }catch (Exception e){
                return  null;
            }
        }catch (Exception e) {
            Utils.logPrint(getClass(),"Error fetching value",Log.getStackTraceString(e));
            return null;
        }
    }

    public String getFromStudent(String studentId, String attribute) throws NullPointerException
    {
        try {
            String sql ="SELECT " + attribute +
                    " FROM " + CookiesAttribute.TABLE_STUDENT +
                    " WHERE " + CookiesAttribute.student_id + " = '" + studentId +"'";

            Cursor mCur = cookiesDb.rawQuery(sql, null);
            if (mCur!=null)
            {
//                return mCur.getString(0);
                mCur.moveToNext();
            }
//            Utils.logPrint(getClass(),"QUERY",sql);
            try {
//                Utils.logPrint(getClass(),"try",mCur.toString());
                return mCur.getString(0);
            }catch (Exception e){
                Utils.logPrint(getClass(),"error",Log.getStackTraceString(e));
                return  null;
            }
        }catch (Exception e) {
            Utils.logPrint(getClass(),"Error fetching value",Log.getStackTraceString(e));
            return null;
        }
    }

    public void addTeacherData(String name, String email, String phone)
    {
        try {
            String sql = "INSERT INTO TEACHER(_id, name, email, phone, image_uri) " +
                    "VALUES (NULL,'"+name+"','"+email+"','"+phone+"',NULL)";
            cookiesDb.execSQL(sql);
        } catch (SQLException mSQLException) {
            Utils.logPrint(getClass(),"addTeacherData",Log.getStackTraceString(mSQLException));
        }
    }

    public void addStudentData(String studentId, String name, String roll, String klass, String section)
    {
        try {
            String sql = "INSERT INTO STUDENT (student_id, name, roll, class, section) " +
                    "VALUES ('"+studentId+"','"+name+"','"+roll+"','"+klass+"','"+section+"')";
            cookiesDb.execSQL(sql);
            Utils.logPrint(getClass(),"Student",studentId+" added");
        } catch (SQLException mSQLException) {
            Utils.logPrint(getClass(),"addStudentData",Log.getStackTraceString(mSQLException));
        }
    }

    public void updateTeacher(String attribute, String value)
    {
        try
        {
            String sql = "UPDATE "+ CookiesAttribute.TABLE_TEACHER+" SET "+attribute+" = '"+value+"'";
            cookiesDb.execSQL(sql);
        }
        catch (SQLException mSQLException)
        {
            Utils.logPrint(getClass(), "error update", Log.getStackTraceString(mSQLException));
        }
    }

    public void updateStudent(String attribute, String value, String studentId)
    {
        try
        {
            String sql = "UPDATE "+ CookiesAttribute.TABLE_STUDENT+" SET "+attribute+" = '"+value+"'" +
                    " WHERE "+ CookiesAttribute.student_id+" = '"+studentId+"'";
            cookiesDb.execSQL(sql);
        }
        catch (SQLException mSQLException)
        {
            Utils.logPrint(getClass(), "error update", Log.getStackTraceString(mSQLException));
        }
    }

    public void delete(String tableName){
        try
        {
            String sql = "DELETE FROM "+tableName;
            cookiesDb.execSQL(sql);
        }
        catch (SQLException mSQLException)
        {
            Utils.logPrint(getClass(), "error update", Log.getStackTraceString(mSQLException));
        }
    }



    public ArrayList<Student> getStudentList()
    {
        try
        {
            String sql ="SELECT * FROM STUDENT";
            ArrayList<Student> students = null;
            Student student;
            Cursor cr = cookiesDb.rawQuery(sql, null);
            if (cr!=null)
            {
                students = new ArrayList<>();
                while (cr.moveToNext()) {
                    String id = cr.getString(cr.getColumnIndex(CookiesAttribute.student_id));
                    String name = cr.getString(cr.getColumnIndex(CookiesAttribute.student_name));
                    String roll = cr.getString(cr.getColumnIndex(CookiesAttribute.student_roll));
                    String klass = cr.getString(cr.getColumnIndex(CookiesAttribute.student_class));
                    String section = cr.getString(cr.getColumnIndex(CookiesAttribute.student_section));
                    String imageUri = cr.getString(cr.getColumnIndex(CookiesAttribute.student_image_uri));
                    student = new Student(id,name,roll,klass,section,imageUri);
                    students.add(student);
                }
            }
            return students;
        }
        catch (SQLException mSQLException)
        {
            Utils.logPrint(getClass(),"SQLException",Log.getStackTraceString(mSQLException));
            return null;
        }
    }

    public Teacher getTeacher() throws NullPointerException
    {
        try
        {
            String sql ="SELECT * FROM "+CookiesAttribute.TABLE_TEACHER;
            Teacher teacher = null ;
            Cursor cr = cookiesDb.rawQuery(sql, null);
            if (cr!=null)
            {
                while (cr.moveToNext()) {
                    String userName = Utils.getString(mContext, Constant.KEY_USER_ID,null);
                    String name = cr.getString(cr.getColumnIndex(CookiesAttribute.teacher_name));
                    String email = cr.getString(cr.getColumnIndex(CookiesAttribute.teacher_email));
                    String phone = cr.getString(cr.getColumnIndex(CookiesAttribute.teacher_phone));
                    String image_uri = cr.getString(cr.getColumnIndex(CookiesAttribute.teacher_image_uri));
                    teacher = new Teacher(userName,name,email,phone,image_uri);
                }
            }
            return teacher;
        }
        catch (SQLException mSQLException)
        {
            Utils.logPrint(getClass(),"SQLException",Log.getStackTraceString(mSQLException));
            return null;
        }
    }


//    public Cursor getProfileData(String phoneNumber)
//    {
//        try
//        {
//            String sql ="SELECT * FROM PROFILE WHERE " + R.string.attributeCookiesMobileNo + " = "+phoneNumber;
//
//            Cursor mCur = cookiesDb.rawQuery(sql, null);
////            if (mCur!=null)
////            {
////                mCur.moveToNext();
////            }
//            if(mCur.getCount() == 0)
//                return  null;
//            else
//                return mCur;
//        }
//        catch (SQLException mSQLException)
//        {
//            Log.e(TAG, "getStationList >>"+ mSQLException.toString());
//            throw mSQLException;
//        }
//    }

//    public void setNewUser(String phoneNumber, String mailId, String name)
//    {
//        String nameAttribute = "Name";
//        String phoneAttribute = "Phone_number";
//        String mailIdAttribute = "Mail_id";
//        if(getProfileData(phoneNumber) == null) {
//            try {
////            String sql ="INSERT INTO PROFILE (Phone_number,Mail_id) VALUES ("+phoneNumber+",'"+mailId+"')";
//                String sql = "INSERT INTO PROFILE ("+phoneAttribute+","+mailIdAttribute+","+nameAttribute+") VALUES (" + phoneNumber + ",'" + mailId + "','" + name + "')";
//                cookiesDb.execSQL(sql);
//                Toast.makeText(mContext, "Insertion sucessful", Toast.LENGTH_LONG).show();
////            return true;
//            } catch (SQLException mSQLException) {
//                Log.e(TAG, "setNewUser >>" + mSQLException.toString());
////                throw mSQLException;
//            }
//        }else {
//
//        }
//    }

//    public void updateProfileValue(String attribute, String value, String phoneNumber)
//    {
//        try
//        {
//            String sql = "UPDATE PROFILE SET "+attribute+" = '"+value+"' WHERE Phone_number = "+phoneNumber;
//            cookiesDb.execSQL(sql);
//            Toast.makeText(mContext,"update sucessful",Toast.LENGTH_LONG).show();
////            return true;
//        }
//        catch (SQLException mSQLException)
//        {
//            Log.e(TAG, "setNewUser >>"+ mSQLException.toString());
////            throw mSQLException;
//        }
//    }

}