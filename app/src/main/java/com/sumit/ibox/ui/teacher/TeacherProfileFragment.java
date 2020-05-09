package com.sumit.ibox.ui.teacher;


import com.sumit.ibox.model.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;

import com.sumit.ibox.local.CookiesAdapter;
import com.sumit.ibox.model.Student;
import com.sumit.ibox.model.Teacher;
import com.sumit.ibox.ui.LoginActivity;
import com.sumit.ibox.ui.faq.*;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherProfileFragment extends Fragment {

    Context context;
    TextView tvName, tvMail;
    CardView studentCard;
    TextView tvStudentName;
    TextView askQuestionClickTeacher,suggestionTeacherClick,logout;
    ArrayList<Student> students;

    public TeacherProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_profile, container, false);

        init(view);
        int type = Utils.getInt(context, Constant.KEY_USER_TYPE, 99);
        if(type == Constant.TYPE_BOTH){
            showStudentCard();
        }

        showProfileCard();
        return view;
    }

    private void init(View view){
        context = getContext();
        tvName = view.findViewById(R.id.teacher_profile_name);
        tvMail = view.findViewById(R.id.teacher_profile_mail);
        studentCard = view.findViewById(R.id.teacher_profile_student_card);
        tvStudentName = view.findViewById(R.id.teacher_profile_student_name);
        suggestionTeacherClick = view.findViewById(R.id.suggestionTeacherClick);
        suggestionTeacherClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AskQuestionFaq.class));
            }
        });

        askQuestionClickTeacher = view.findViewById(R.id.askQuestionClickTeacher);
        askQuestionClickTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( context, FaqActivity.class));
            }
        });
        logout = view.findViewById(R.id.teacher_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.logout(context, getActivity());
            }
        });
    }

    private void showProfileCard() {
        CookiesAdapter cookiesAdapter = new CookiesAdapter(context);
        cookiesAdapter.openReadable();
        Teacher teacher = cookiesAdapter.getTeacher();
        cookiesAdapter.close();
        if(teacher == null){
            tvMail.setText(Constant.NULL_VALUE);
            tvName.setText(Constant.NULL_VALUE);
        }else {
            String mail = teacher.getEmail();
            mail = mail.equals("null")?Constant.NULL_VALUE:mail;
            tvMail.setText(mail);
            String name = teacher.getName();
            name = name.equals("null")?Constant.NULL_VALUE:name;
            tvName.setText(name);
        }
    }

    private void showStudentCard() {
        CookiesAdapter cookiesAdapter = new CookiesAdapter(context);
        cookiesAdapter.openReadable();
        students = cookiesAdapter.getStudentList();
        cookiesAdapter.close();
        String name = students.get(0).getName();
        if(students.size() > 1){
            name = name + "+" + String.valueOf(students.size()-1);
        }
        tvStudentName.setText(name);
        studentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelector();
            }
        });
    }

    private void showSelector(){
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(context);
        pictureDialog.setTitle("Select Child");
        String[] items = new String[students.size()];
        for(Student student: students){
            items[students.indexOf(student)] = student.getName();
        }
        pictureDialog.setItems(items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.saveString(context, Constant.KEY_STUDENT_ID, students.get(which).getId());
                        Intent intent = new Intent(new Intent(getContext(), LoginActivity.class));
                        intent.putExtra(Constant.KEY_ACTIVITY_TO_LOAD, Constant.TYPE_PARENT);
                        startActivity(intent);
                    }
                });
        pictureDialog.show();
    }

}
