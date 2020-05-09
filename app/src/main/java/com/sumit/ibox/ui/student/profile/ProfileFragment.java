package com.sumit.ibox.ui.student.profile;

import com.sumit.ibox.model.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.sumit.ibox.local.CookiesAdapter;
import com.sumit.ibox.model.Student;
import com.sumit.ibox.model.Teacher;
import com.sumit.ibox.ui.LoginActivity;
import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.ui.changenumber.ChangeContactNo;
import com.sumit.ibox.ui.faq.FaqActivity;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private Context context;
    private ProfileViewModel notificationsViewModel;
    private ArrayList<Student> students;
    Toolbar toolbar;
    TextView tvName, tvClass, tvSection;

    CardView teacherSwitch;
    TextView teacherName;
    TextView changePhoneNumber, askQuestionLayout, logout;

    View.OnClickListener teacherSwitchClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(new Intent(getContext(), LoginActivity.class));
            intent.putExtra(Constant.KEY_ACTIVITY_TO_LOAD, Constant.TYPE_TEACHER);
            startActivity(intent);
        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        init(root);
        showStudentCard();
        int type = Utils.getInt(context, Constant.KEY_USER_TYPE, Constant.TYPE_PARENT);
        if(type == Constant.TYPE_BOTH){
            showTeacherCard();
        }
        return root;
    }

    private void init(View root) {
        context = this.getContext();
        toolbar = root.findViewById(R.id.profile_toolbar);
        toolbar.setTitle(R.string.title_profile);
        tvName = root.findViewById(R.id.profile_student_name);
        tvClass = root.findViewById(R.id.profile_student_class);
        tvSection = root.findViewById(R.id.profile_student_section);
        teacherSwitch = root.findViewById(R.id.teacher_switch);
        teacherName = root.findViewById(R.id.student_profile_teacher_name);
        changePhoneNumber = root.findViewById(R.id.change_phone_number_click);
        askQuestionLayout = root.findViewById(R.id.ask_question_click);
        logout = root.findViewById(R.id.student_logout);

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelector();
            }
        });
        teacherSwitch.setOnClickListener(teacherSwitchClicked);
        askQuestionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (context, FaqActivity.class));
            }
        });
        changePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (context, ChangeContactNo.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.logout(context, getActivity());
            }
        });
    }

    private void showStudentCard() {
        CookiesAdapter cookiesAdapter = new CookiesAdapter(context);
        cookiesAdapter.openReadable();
        students = cookiesAdapter.getStudentList();
        cookiesAdapter.close();
        String selectedId = Utils.getString(context, Constant.KEY_STUDENT_ID, "");
        for (Student student: students){
            if(student.getId().equals(selectedId)){
                String name = student.getName();
                name = name.equals("null")?Constant.NULL_VALUE:name;
                tvName.setText(name);
                String klass = student.getKlass();
                klass = klass.equals("null")?Constant.NULL_VALUE:klass;
                tvClass.setText(klass);
                String section = student.getSection();
                section = section.equals("null")?Constant.NULL_VALUE:section;
                tvSection.setText(section);
            }
        }
    }

    private void showSelector(){
        android.app.AlertDialog.Builder pictureDialog = new android.app. AlertDialog.Builder(context);
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
                        showStudentCard();
                    }
                });
        pictureDialog.show();
    }

    private void showTeacherCard() {
        teacherSwitch.setVisibility(View.VISIBLE);
        CookiesAdapter cookiesAdapter = new CookiesAdapter(context);
        cookiesAdapter.openReadable();
        Teacher teacher = cookiesAdapter.getTeacher();
        cookiesAdapter.close();
        if(teacher == null){
            teacherName.setText(Constant.NULL_VALUE);
        }else {
            String name = teacher.getName().equals("null") ? Constant.NULL_VALUE : teacher.getName();
            teacherName.setText(name);
        }
    }


}